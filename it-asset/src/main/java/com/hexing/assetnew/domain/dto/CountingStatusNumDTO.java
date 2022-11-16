package com.hexing.assetnew.domain.dto;

import lombok.Data;

@Data
public class CountingStatusNumDTO {
    Integer total = 0;
    Integer notCounted = 0;
    Integer counted = 0;
    Integer abnormal = 0;
}
