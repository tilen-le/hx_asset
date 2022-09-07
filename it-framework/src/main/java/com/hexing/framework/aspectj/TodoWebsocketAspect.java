package com.hexing.framework.aspectj;

import com.hexing.common.annotation.TodoWebsocket;
import com.hexing.common.constant.HttpStatus;
import com.hexing.common.core.domain.AjaxResult;
import com.hexing.common.core.domain.entity.SysUser;
import com.hexing.system.service.ISysUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Aspect
//@Component
public class TodoWebsocketAspect
{
    private static final Logger log = LoggerFactory.getLogger(TodoWebsocketAspect.class);

    @Resource
    @Lazy
    private ISysUserService userService;

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(todoWebsocket)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, TodoWebsocket todoWebsocket, Object jsonResult)
    {
        try {
            if (jsonResult instanceof AjaxResult) {
                AjaxResult responseResult = (AjaxResult) jsonResult;
                if (HttpStatus.SUCCESS == (int)responseResult.get(AjaxResult.CODE_TAG)) {
                    //获取Request对象
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    HttpServletRequest request = attributes.getRequest();
                    Object roles = request.getAttribute("roles");
                    Object message = request.getAttribute("message");
                    if (Objects.isNull(roles)) {
                        return;
                    }
                    List<SysUser> userList = new ArrayList<>();
                    if (roles instanceof String) {
                        String role = (String) roles;
                        userList = userService.getUserListByRoleCodes(new String[]{role});
                    } else if (roles instanceof String[]) {
                        String[] roleArray = (String[]) roles;
                        userList = userService.getUserListByRoleCodes(roleArray);
                    }
                    if (!CollectionUtils.isEmpty(userList) && Objects.nonNull(message)) {
                        List<Long> userIdList = userList.stream().map(SysUser::getUserId).collect(Collectors.toList());
                        //通知message
                        message.toString();
                    }
                }
            }
        } catch (Exception e) {
            log.error("切面websocket通知失败");
            e.printStackTrace();
        }

    }




}
