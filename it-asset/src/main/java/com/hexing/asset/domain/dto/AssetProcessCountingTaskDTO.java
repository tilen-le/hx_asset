package com.hexing.asset.domain.dto;

import com.hexing.asset.domain.AssetProcessCountingTask;
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
public class AssetProcessCountingTaskDTO extends AssetProcessCountingTask {
    private List<String> responsiblePersonCodes;
}
