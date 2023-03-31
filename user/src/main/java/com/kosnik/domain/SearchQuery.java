package com.kosnik.domain;

import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchQuery {
    private List<Filter> filters;
    private Sort sort;
}
