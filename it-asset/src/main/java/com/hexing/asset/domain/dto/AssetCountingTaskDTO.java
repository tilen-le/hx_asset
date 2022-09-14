package com.hexing.asset.domain.dto;

import com.hexing.asset.domain.AssetCountingTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 盘点任务DTO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssetCountingTaskDTO extends AssetCountingTask {
    private List<String> responsiblePersonCodes;
}
