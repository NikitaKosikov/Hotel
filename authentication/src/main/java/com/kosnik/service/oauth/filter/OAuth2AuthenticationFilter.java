package com.kosnik.service.oauth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.kosnik.service.jwt.JwtService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2AuthenticationFilter extends GenericFilterBean {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String REQUIRE_URI= "/gift-certificates";
    private static final String BASE_URI = "/auth/realms";
    private static final String CLIENT_ID = "springboot-keycloak";
    private static final String AUTHORIZATION_CODE_PARAMETER_NAME = "code";
    private static final String REDIRECT_URI = "http://localhost:8081/gift-certificates";
    private static final String NONCE_PARAMETER_NAME = "nonce";
    private static final String ACCESS_TOKEN_HEADER = "ACCESS_TOKEN";
    private static final String OAUTH2_ROLES_NAME = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final JwtService jwtService;
    private AuthenticationManager authenticationManager;
    @Override
    protected void initFilterBean(){
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        OidcUserService userService = new OidcUserService();
        OidcAuthorizationCodeAuthenticationProvider authenticationProvider = new OidcAuthorizationCodeAuthenticationProvider(accessTokenResponseClient, userService);
        authenticationManager = new ProviderManager(authenticationProvider);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        initFilterBean();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (!requireAuthentication(httpServletRequest)){
            chain.doFilter(request, response);
            return;
        }

        try {
            OAuth2AuthenticationToken authentication = authenticate(httpServletRequest, httpServletResponse);
            successfulAuthentication(httpServletResponse, authentication);
        }catch (Exception e){
            unsuccessfulAuthentication(httpServletResponse, e);
        }
    }

    private boolean requireAuthentication(HttpServletRequest request){
        return request.getServletPath().equals(REQUIRE_URI);
    }


    private OAuth2AuthenticationToken authenticate(HttpServletRequest request, HttpServletResponse response){

        String code = request.getParameter(AUTHORIZATION_CODE_PARAMETER_NAME);
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(CLIENT_ID);

        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, BASE_URI);
        authorizationRequestResolver.setAuthorizationRequestCustomizer(req -> req
                .redirectUri(REDIRECT_URI)
                .additionalParameters(param -> param.remove(NONCE_PARAMETER_NAME))
                .attributes(attr -> attr.remove(NONCE_PARAMETER_NAME))

        );

        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestResolver.resolve(request, CLIENT_ID);

        OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse
                .success(code)
                .redirectUri(REDIRECT_URI)
                .state(authorizationRequest.getState())
                .build();

        OAuth2LoginAuthenticationToken authenticationRequest =
                new OAuth2LoginAuthenticationToken(clientRegistration, new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));

        OAuth2LoginAuthenticationToken authenticationResult = (OAuth2LoginAuthenticationToken) authenticationManager.authenticate(authenticationRequest);

        OAuth2AuthenticationToken oauth2Authentication = new OAuth2AuthenticationToken(
                authenticationResult.getPrincipal(),
                authenticationResult.getAuthorities(),
                authenticationResult.getClientRegistration().getRegistrationId());

        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
                authenticationResult.getClientRegistration(),
                oauth2Authentication.getName(),
                authenticationResult.getAccessToken(),
                authenticationResult.getRefreshToken());

        authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request, response);
        return oauth2Authentication;
    }

    private void successfulAuthentication(HttpServletResponse response, OAuth2AuthenticationToken authentication) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        response.addHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON_VALUE);
        response.addHeader(ACCESS_TOKEN_HEADER, token);
        response.getWriter().println(token);
    }

    private void unsuccessfulAuthentication(HttpServletResponse response, Exception exception) throws IOException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getLocalizedMessage());
    }
}