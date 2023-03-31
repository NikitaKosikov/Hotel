package com.kosnik.service.converter;


import com.kosnik.domain.Filter;
import com.kosnik.web.UserApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class SearchQueryConverterTest {

    @Test
    void filterParseSuccessfullyResult() {
        List<Filter> expectedFilterList = new ArrayList<>(
                List.of(
                        new Filter("email", "=", "example@mail.ru")
                )
        );

        List<Filter> actualFilters = SearchQueryConverter.filterParse("email[eq]=example@mail.ru");

        actualFilters.forEach(filter->assertTrue(expectedFilterList.contains(filter)));
    }

    @Test
    void filterParseBadIncomingFilterFieldName() {
        assertThrows(InvalidParameterException.class,() ->
                SearchQueryConverter.filterParse("[eq]=example@mail.ru"));
    }

    @Test
    void filterParseBadIncomingFilterOperation() {
        assertThrows(InvalidParameterException.class,() ->
                        SearchQueryConverter.filterParse("email][=example@mail.ru"));
    }

    @Test
    void filterParseBadIncomingFilterValue() {
        assertThrows(InvalidParameterException.class,() ->
                        SearchQueryConverter.filterParse("email[eq]example@mail.ru"));
    }
    
    @Test
    void sortingParseSuccessfullyResult() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.asc("email"));
        orders.add(Sort.Order.desc("price"));
        Sort expectedSort = Sort.by(orders);

        Sort actualSort = SearchQueryConverter.sortingParse("sort=email.asc,price.desc");

        assertEquals(expectedSort, actualSort);
    }

    @Test
    void sortingParseIncorrectDirection() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.asc("email"));
        Sort expectedSort = Sort.by(orders);

        Sort actualSort = SearchQueryConverter.sortingParse("sort=email.asc,price.dec");

        assertEquals(expectedSort, actualSort);
    }
}