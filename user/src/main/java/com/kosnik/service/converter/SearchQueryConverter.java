package com.kosnik.service.converter;

import com.kosnik.domain.Filter;
import com.kosnik.domain.SearchQuery;
import org.springframework.data.domain.Sort;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchQueryConverter {

    private static final String FILTER_PARAMS_NAME = "filter";
    private static final String SORT_PARAMS_NAME = "sort";
    private static final String SEARCH_FIELD_NAME_PATTERN = "\\[";
    private static final String SEARCH_FILTER_VALUE_PATTERN = "=";
    private static final String SEARCH_OPERATION_PATTERN = "/\\[(.+?)]/";
    private static final String SORT_VALUE_ORDER_SEPARATOR = ".";
    private static final String FILTER_SEPARATOR_PARAMETERS = ",";
    private static final String SORT_SEPARATOR_PARAMETERS = ",";
    public static SearchQuery convert(Map<String, String> params){
        if (params.isEmpty()){
            return SearchQuery.builder().build();
        }
        List<Filter> filters = filterParse(params.get(FILTER_PARAMS_NAME));
        Sort sort = sortingParse(params.get(SORT_PARAMS_NAME));
        return SearchQuery
                .builder()
                .sort(sort)
                .filters(filters)
                .build();
    }

    //filter=email[gte]=100,price[lte]=100
    public static List<Filter> filterParse(String filtersParams){
        List<Filter> filterList  = new ArrayList<>();
        Pattern pattern = Pattern.compile(FILTER_SEPARATOR_PARAMETERS);
        Matcher matcher = pattern.matcher(filtersParams);
        if (matcher.find()){
            String[] filters = filtersParams.split(FILTER_SEPARATOR_PARAMETERS);
            filterList = filterBuild(filters);
        } else if (!filtersParams.isEmpty()) {
            filterList = filterBuild(filtersParams);
        }
        return filterList;
    }

    private static List<Filter> filterBuild(String... filters){
        List<Filter> filterList  = new ArrayList<>();
        for (String filter : filters) {
            try {
                String field = extractFieldName(filter);
                String operation = extractOperation(filter);
                String value = extractValue(filter);
                filterList.add(new Filter(field, operation, value));
            }catch (InvalidParameterException ignored){

            }
        }
        return filterList;
    }

    //TODO ok? start end?
    private static String extractFieldName(String filter){
        Pattern pattern = Pattern.compile(SEARCH_FIELD_NAME_PATTERN);
        Matcher matcher = pattern.matcher(filter);
        if (matcher.find()){
            return filter.substring(matcher.start(), matcher.end());
        }
        //TODO InvalidParameterException
        throw new InvalidParameterException("Parameter's field name is invalid");
    }
    //TODO ok? group?
    private static String extractOperation(String filter){
        Pattern pattern = Pattern.compile(SEARCH_OPERATION_PATTERN);
        Matcher matcher = pattern.matcher(filter);
        if (matcher.find()){
            return matcher.group(2);
        }
        throw new InvalidParameterException("Parameter's operation is invalid");
    }
    //TODO ok? start end?
    private static String extractValue(String filter){
        Pattern pattern = Pattern.compile(SEARCH_FILTER_VALUE_PATTERN);
        Matcher matcher = pattern.matcher(filter);
        if (matcher.find()){
            return filter.substring(matcher.start(), matcher.end());
        }
        throw new InvalidParameterException("Parameter's value is invalid");
    }

    // sort=email.asc,price.desc
    public static Sort sortingParse(String sortsParams){
        List<Sort.Order> orders = new ArrayList<>();
        Pattern pattern = Pattern.compile(SORT_SEPARATOR_PARAMETERS);
        Matcher matcher = pattern.matcher(sortsParams);
        if (matcher.find()){
            String[] sorts = sortsParams.split(SORT_SEPARATOR_PARAMETERS);
            orders = buildOrders(sorts);
        } else if (!sortsParams.isEmpty()) {
            orders = buildOrders(sortsParams);
        }
        return Sort.by(orders);
    }

    private static List<Sort.Order> buildOrders(String... sorts) {
        List<Sort.Order> orders = new ArrayList<>();
        Pattern pattern = Pattern.compile(SORT_VALUE_ORDER_SEPARATOR);
        for (String sort : sorts) {
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()){
                String field = sort.substring(matcher.start(), matcher.end());
                String direction = sort.substring(matcher.end()+1);
                Sort.Order order  = setUpDirection(field, direction);
                if (order!=null){
                    orders.add(order);
                }
            }
        }
        return orders;
    }
    private static Sort.Order setUpDirection(String field, String order) {
        if (Sort.Direction.ASC.name().equalsIgnoreCase(order.trim())){
            return new Sort.Order(Sort.Direction.ASC, field);
        }else if (Sort.Direction.DESC.name().equals(order.trim())){
            return new Sort.Order(Sort.Direction.DESC, field);
        }
        return null;
    }
}
