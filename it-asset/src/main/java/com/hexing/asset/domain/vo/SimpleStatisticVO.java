package com.hexing.asset.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SimpleStatisticVO {
    private String name;
    private Object value;
}
