package com.hexing.asset.domain.vo;

import com.hexing.asset.domain.AssetCountingTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 盘点任务VO类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssetCountingTaskVO extends AssetCountingTask {
    private List<String> responsiblePersonCodes;
}
