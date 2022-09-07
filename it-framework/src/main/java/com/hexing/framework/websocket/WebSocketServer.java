package com.hexing.framework.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * websocket 消息处理
 *
 * @author ruoyi
 */
@Component
@ServerEndpoint("/websocket/message")
public class WebSocketServer {
    /**
     * WebSocketServer 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 默认最多允许同时在线人数100
     */
    public static int socketMaxOnlineCount = 10000;

    private static Semaphore socketSemaphore = new Semaphore(socketMaxOnlineCount);

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {
        WebSocketUsers.put(session.getId(), session);
    }

    /**
     * 连接关闭时处理
     */
    @OnClose
    public void onClose(Session session) {
        WebSocketUsers.remove(session.getId());
    }

    /**
     * 抛出异常时处理
     */
    @OnError
    public void onError(Session session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            // 关闭连接
            session.close();
        }
        String sessionId = session.getId();
        // 移出用户
        WebSocketUsers.remove(sessionId);
    }

    /**
     * 服务器接收到客户端消息时调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        return;
    }


    public void sendBillTodoMessage(List<String> userIdList, String message) {
        Map<String, Session> users = WebSocketUsers.getUsers();
        for (Map.Entry<String, Session> entry : users.entrySet()) {
            if (userIdList.contains(entry.getKey())) {
                entry.getValue();
                //fas
            }
        }
    }

}
