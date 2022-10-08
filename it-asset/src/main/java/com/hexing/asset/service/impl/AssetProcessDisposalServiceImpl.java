package com.hexing.asset.service.impl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiProcessinstanceFileUrlGetRequest;
import com.dingtalk.api.response.OapiProcessinstanceFileUrlGetResponse;
import com.hexing.asset.domain.*;
import com.hexing.asset.service.IAssetProcessService;
import com.hexing.common.config.RuoYiConfig;
import com.hexing.common.utils.DateUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.file.FileUploadUtils;
import com.hexing.framework.manager.factory.AsyncFactory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.hexing.asset.mapper.AssetProcessDisposalMapper;
import com.hexing.asset.service.IAssetProcessDisposalService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 * 资产处置流程Service业务层处理
 *
 * @author zxy
 * @date 2022-09-08
 */
@Service
public class AssetProcessDisposalServiceImpl extends ServiceImpl<AssetProcessDisposalMapper, AssetProcessDisposal> implements IAssetProcessDisposalService
{
    @Autowired
    private AssetProcessDisposalMapper assetProcessDisposalMapper;
    @Autowired
    private IAssetProcessService assetProcessService;

    /**
     * 查询资产处置流程
     *
     * @param id 资产处置流程主键
     * @return 资产处置流程
     */
    @Override
    public AssetProcessDisposal selectAssetProcessDisposalById(Long id)
    {
        return assetProcessDisposalMapper.selectAssetProcessDisposalById(id);
    }

    /**
     * 查询资产处置流程列表
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 资产处置流程
     */
    @Override
    public List<AssetProcessDisposal> selectAssetProcessDisposalList(AssetProcessDisposal assetProcessDisposal)
    {
        LambdaQueryWrapper<AssetProcessDisposal> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(assetProcessDisposal.getUserCode())) {
            wrapper.eq(AssetProcessDisposal::getUserCode, assetProcessDisposal.getUserCode());
        }
        if (StringUtils.isNotBlank(assetProcessDisposal.getAssetCode())) {
            wrapper.eq(AssetProcessDisposal::getAssetCode, assetProcessDisposal.getAssetCode());
        }
        if (StringUtils.isNotBlank(assetProcessDisposal.getInstanceId())) {
            wrapper.eq(AssetProcessDisposal::getInstanceId, assetProcessDisposal.getInstanceId());
        }
        return assetProcessDisposalMapper.selectList(wrapper);
    }

    /**
     * 新增资产处置流程
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 结果
     */
    @Override
    public int insertAssetProcessDisposal(AssetProcessDisposal assetProcessDisposal)
    {
        return assetProcessDisposalMapper.insert(assetProcessDisposal);
    }

    @Override
    public int downLoadFile(AssetProcessDisposal assetProcessDisposal)
    {
        String dingToken = AsyncFactory.getDingToken_one();
        AssetProcessDisposal as = assetProcessDisposalMapper.selectById(assetProcessDisposal.getId());
        JSONArray jsonArray = JSONArray.parseArray(as.getFileInfo());
        String instanceId = as.getInstanceId();

        for(Object o:jsonArray){
            JSON parse = JSONUtil.parse(o);
            String fileId = parse.getByPath("fileId").toString();
            String fileType = parse.getByPath("fileType").toString();
            String fileName = parse.getByPath("fileName").toString();
            String spaceId = parse.getByPath("spaceId").toString();
            try {
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/file/url/get");
                OapiProcessinstanceFileUrlGetRequest req = new OapiProcessinstanceFileUrlGetRequest();
                OapiProcessinstanceFileUrlGetRequest.GrantCspaceRequest obj1 = new OapiProcessinstanceFileUrlGetRequest.GrantCspaceRequest();
                obj1.setProcessInstanceId(instanceId);
                obj1.setFileId(fileId);
                req.setRequest(obj1);
                OapiProcessinstanceFileUrlGetResponse rsp = client.execute(req, dingToken);
                if (ObjectUtil.isNotNull(rsp)&&rsp.getErrcode()==0&&ObjectUtil.isNotNull(rsp.getResult())){
                    String downloadUri = rsp.getResult().getDownloadUri();
                    MultipartFile multipartFile = urlToMultipartFile(downloadUri, fileName);
                    String upload = FileUploadUtils.upload(RuoYiConfig.getProfile()+"/upload",multipartFile);
                    System.out.println("upload :"+upload);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return 1;
    }

    public static String downLoadFile(String fileInfo,String instanceId){
        String dingToken = AsyncFactory.getDingToken_one();
        JSONArray jsonArray = JSONArray.parseArray(fileInfo);
        JSONArray result =new JSONArray();
        for(Object o:jsonArray){
            JSON parse = JSONUtil.parse(o);
            String fileId = parse.getByPath("fileId").toString();
            String fileType = parse.getByPath("fileType").toString();
            String fileName = parse.getByPath("fileName").toString();
            String spaceId = parse.getByPath("spaceId").toString();
            try {
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/processinstance/file/url/get");
                OapiProcessinstanceFileUrlGetRequest req = new OapiProcessinstanceFileUrlGetRequest();
                OapiProcessinstanceFileUrlGetRequest.GrantCspaceRequest obj1 = new OapiProcessinstanceFileUrlGetRequest.GrantCspaceRequest();
                obj1.setProcessInstanceId(instanceId);
                obj1.setFileId(fileId);
                req.setRequest(obj1);
                OapiProcessinstanceFileUrlGetResponse rsp = client.execute(req, dingToken);
                if (ObjectUtil.isNotNull(rsp)&&rsp.getErrcode()==0&&ObjectUtil.isNotNull(rsp.getResult())){
                    String downloadUri = rsp.getResult().getDownloadUri();
                    MultipartFile multipartFile = urlToMultipartFile(downloadUri, fileName);
                    String upload = FileUploadUtils.upload(RuoYiConfig.getProfile()+"/upload",multipartFile);
                    JSONObject jsonObject =new JSONObject();
                    jsonObject.put("name",fileName);
                    jsonObject.put("url",upload);
                    result.add(jsonObject);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("result :"+result);
        return result.toString();

    }

    @Override
    public int disposalAssets(JSONObject params){
        JSONObject data = params.getObject("data", JSONObject.class);
        String instanceId = data.getString("instanceId");
        String userCode = data.getString("userCode");
        String userName = data.getString("userName");
        String processType = data.getString("processType");
        JSONArray assetList = data.getJSONArray("assets");
        if (data.containsKey("fileInfo")){
            String fileInfo = data.getJSONArray("fileInfo").toString();
            fileInfo =downLoadFile(fileInfo,instanceId);
            for (Object o : assetList) {
                String assetCode = JSONUtil.parseObj(o).getStr("assetCode");
                AssetProcess assetProcess = new AssetProcess();
                assetProcess.setAssetCode(assetCode);
                assetProcess.setUserCode(userCode);
                assetProcess.setUserName(userName);
                assetProcess.setProcessType(processType);
                assetProcess.setCreateTime(new Date());
                assetProcessService.insertAssetProcess(assetProcess);

                AssetProcessDisposal entity = new AssetProcessDisposal();
                entity.setAssetCode(assetCode);
                entity.setProcessId(assetProcess.getId());
                entity.setUserCode(userCode);
                entity.setUserName(userName);
                entity.setType(processType);
                entity.setFileInfo(fileInfo);
                entity.setStatus("处置中");
                entity.setInstanceId(instanceId);
                entity.setCreateTime(new Date());
                insertAssetProcessDisposal(entity);
            }
        }else {
            String fileInfoAdd = data.getJSONArray("fileInfoAdd").toString();
            fileInfoAdd =downLoadFile(fileInfoAdd,instanceId);
            LambdaQueryWrapper<AssetProcessDisposal> w = new LambdaQueryWrapper<>();
            for (Object o : assetList) {
                String assetCode = JSONUtil.parseObj(o).getStr("assetCode");
                w.eq(AssetProcessDisposal::getAssetCode, assetCode).eq(AssetProcessDisposal::getInstanceId, instanceId);
                AssetProcessDisposal entity = getOne(w);
                entity.setFileInfoAdd(fileInfoAdd);
                entity.setStatus("完成");
                entity.setUpdateTime(new Date());
                updateById(entity);
            }
        }

        return 1;
    }


    public static MultipartFile urlToMultipartFile(String url, String fileName){
        HttpURLConnection httpUrl = null;
        MultipartFile multipartFile = null;
        try {
            System.out.println("urlToMultipartFile文件转换中，url为："+url+"文件名称为："+fileName);
            httpUrl = (HttpURLConnection) new URL(url).openConnection();
            httpUrl.connect();
            System.out.println("成功建立httpUrl连接" + httpUrl);
            FileItem fileItem = createFileItem(httpUrl.getInputStream(), fileName);
            multipartFile= new CommonsMultipartFile(fileItem);

        } catch (IOException e) {
            System.out.println("url解析失败，抛出url解析异常");

        } finally {
            httpUrl.disconnect();
        }

        return multipartFile;
    }

    public static FileItem createFileItem(InputStream inputStream, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        OutputStream os = null;
        //使用输出流输出输入流的字节
        try {
            os = item.getOutputStream();
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Stream copy exception"+ e);
            throw new IllegalArgumentException("文件上传失败");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.out.println("Stream close exception"+ e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Stream close exception"+ e);
                }
            }
        }

        return item;
    }

    /**
     * 修改资产处置流程
     *
     * @param assetProcessDisposal 资产处置流程
     * @return 结果
     */
    @Override
    public int updateAssetProcessDisposal(AssetProcessDisposal assetProcessDisposal)
    {
        assetProcessDisposal.setUpdateTime(DateUtils.getNowDate());
        return assetProcessDisposalMapper.updateAssetProcessDisposal(assetProcessDisposal);
    }

    /**
     * 批量删除资产处置流程
     *
     * @param ids 需要删除的资产处置流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessDisposalByIds(Long[] ids)
    {
        return assetProcessDisposalMapper.deleteAssetProcessDisposalByIds(ids);
    }

    /**
     * 删除资产处置流程信息
     *
     * @param id 资产处置流程主键
     * @return 结果
     */
    @Override
    public int deleteAssetProcessDisposalById(Long id)
    {
        return assetProcessDisposalMapper.deleteAssetProcessDisposalById(id);
    }
}
