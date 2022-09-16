package com.hexing.framework.manager.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.hexing.common.core.text.Convert;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hexing.common.constant.Constants;
import com.hexing.common.utils.LogUtils;
import com.hexing.common.utils.ServletUtils;
import com.hexing.common.utils.StringUtils;
import com.hexing.common.utils.ip.AddressUtils;
import com.hexing.common.utils.ip.IpUtils;
import com.hexing.common.utils.spring.SpringUtils;
import com.hexing.system.domain.SysLogininfor;
import com.hexing.system.domain.SysOperLog;
import com.hexing.system.service.ISysLogininforService;
import com.hexing.system.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.util.CollectionUtils;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
@Slf4j
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
            final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }


    public static TimerTask sendDingNotice(final List<String> userList, final String title) {
        return new TimerTask() {
            @Override
            public void run() {
                if (CollectionUtils.isEmpty(userList)) {
                    return;
                }
                List<String> userIds = new ArrayList<>();
                for (String userId : userList) {
                    if (!userId.startsWith("S")) {
                        userIds.add("S" + userId);
                    } else {
                        userIds.add(userId);
                    }
                }
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
                OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
                request.setAgentId(878069237L);
                request.setUseridList(userIds.stream().collect(Collectors.joining(",")));
                request.setToAllUser(false);
                OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
                msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
                msg.getActionCard().setTitle("盘点流程提醒");
                msg.getActionCard().setMarkdown("## 盘点流程等待处理   \n  " + title);
                msg.getActionCard().setSingleTitle("查看详情");
//                String singleUrl = "dingtalk://dingtalkclient/page/link?url=" + URLUtil.encode(detailUrl + matureId) + "&pc_slide=false";
                String singleUrl ="dingtalk://dingtalkclient/action/openapp?app_id=-4&container_type=work_platform&corpid=ding389cf0d9a2e1cc2f&ddtab=true&redirect_type=jump&redirect_url=https%3A%2F%2Faflow.dingtalk.com%2Fdingtalk%2Fmobile%2Fhomepage.htm%3Fback_control%3Dfalse%26backcontrol%3Dfalse%26corpid%3Dding389cf0d9a2e1cc2f%26dd_progress%3Dfalse%26dd_share%3Dfalse%26ddtab%3Dtrue%26showmenu%3Dfalse%23%2Fcustom%3Fpcredirect%3Dself%26processCode%3DPROC-82D0501E-705A-4DDF-9274-C257BFFE4866";

                msg.getActionCard().setSingleUrl(singleUrl);
                msg.setMsgtype("action_card");
                request.setMsg(msg);
                String dingToken = getDingToken();
                if (StringUtils.isBlank(dingToken)) {
                    log.error("token获取失败");
                    return;
                }
                OapiMessageCorpconversationAsyncsendV2Response rsp = null;
                try {
                    rsp = client.execute(request, dingToken);
                } catch (Exception e) {
                    log.error("钉钉发生通知异常" + userList.toString());
                    e.printStackTrace();
                }
                log.info(rsp.getBody());
            }
        };
    }

    public static String getDingToken() {
        try {
            DingTalkClient getToeknClient = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey("dingdg5xw8bs25xrhc3x");
            req.setAppsecret("9kJHco5VvVDQ9bmLojiPOpLNQW_B_3vwFtc4eUIzh0lj8rw1H1cpz_RrabnUf9T_");
            req.setHttpMethod("GET");
            OapiGettokenResponse rsp = getToeknClient.execute(req);
            String token = rsp.getAccessToken();
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
