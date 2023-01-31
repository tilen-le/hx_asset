package com.hexing.assetNew.enums;

public enum UIPcodeEnum {


    /**
     * 将资产信息与保管人关联关系同步到SAP
     */
    pushAssetInfoToSAP("ZFMFICOZC002", "将资产信息与保管人关联关系同步到SAP"),
    /**
     * 将通过Excel导入的新资产数据的平台资产编码同步到SAP
     */
    pushSingleFdCodeToSAP("ZFMFICOZC003", "将通过Excel导入的新资产数据的平台资产编码同步到SAP"),
    ;


    private String code;
    private String msg;

    UIPcodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
