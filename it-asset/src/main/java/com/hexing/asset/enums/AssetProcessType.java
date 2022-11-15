package com.hexing.asset.enums;

import com.hexing.asset.domain.Process;
import com.hexing.asset.domain.dto.AssetProcessCountingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetProcessType {
    /**
     * 资产盘点流程
     */
    ASSET_COUNTING(AssetProcessCountingDTO.class, "100"),
    ;

    private final Class<? extends Process> clazz;
    private final String code;

    public static String getValue(Process process) {
        for (AssetProcessType entry : values()) {
            if (entry.getClazz() == process.getClass()) {
                return entry.getCode();
            }
        }
        return null;
    }

}
