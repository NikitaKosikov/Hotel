package com.kosnik.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Filter {
    private String field;
    private String operation;
    private String value;
}
