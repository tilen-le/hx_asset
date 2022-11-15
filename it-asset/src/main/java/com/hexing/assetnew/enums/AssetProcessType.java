package com.hexing.assetnew.enums;

import com.hexing.asset.domain.Process;
import com.hexing.assetnew.domain.AssetProcessCountingDomain;
import com.hexing.assetnew.domain.AssetsProcess;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetProcessType {
    /**
     * 资产盘点流程
     */
    COUNTING_PROCESS(AssetProcessCountingDomain.class, "100"),
    ;

    private final Class<? extends AssetsProcess> clazz;
    private final String code;

    public static String getValue(AssetsProcess process) {
        for (AssetProcessType entry : values()) {
            if (entry.getClazz() == process.getClass()) {
                return entry.getCode();
            }
        }
        return null;
    }

}
