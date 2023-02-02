package com.hexing.asset.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class SimpleOuterDTO<T> {
    private T data;
}
