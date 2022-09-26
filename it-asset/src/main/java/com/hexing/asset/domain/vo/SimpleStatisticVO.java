package com.hexing.asset.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SimpleStatisticVO {
    private String name;
    private Object value;
}
