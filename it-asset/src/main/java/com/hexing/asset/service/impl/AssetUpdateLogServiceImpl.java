package com.hexing.asset.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hexing.asset.domain.Asset;
import com.hexing.asset.domain.AssetUpdateLog;
import com.hexing.asset.mapper.AssetUpdateLogMapper;
import com.hexing.asset.service.IAssetUpdateLogService;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资产信息更新日志Service业务层处理
 *
 * @author zxy
 * @date 2022-10-19
 */
@Service
public class AssetUpdateLogServiceImpl extends ServiceImpl<AssetUpdateLogMapper, AssetUpdateLog> implements IAssetUpdateLogService {

    @Autowired
    private AssetUpdateLogMapper assetUpdateLogMapper;

    /**
     * 创建资产信息更新日志
     *
     * @param asset 资产旧信息
     * @param processId 主流程id
     * @return
     */
    @Override
    public boolean saveLog(Asset asset, String processId) {
        AssetUpdateLog log = new AssetUpdateLog();
        BeanUtils.copyProperties(asset, log);

        if (StringUtils.isNotBlank(processId)) {
            log.setProcessId(processId);
        } else {
            // 若processId为空，则说明是在网站上修改了资产信息
//            log.setUpdateBy(SecurityUtils.getUsername());
            log.setUpdateBy("test");
        }

        log.setUpdateTime(DateUtils.getNowDate());
        return this.save(log);
    }

}
