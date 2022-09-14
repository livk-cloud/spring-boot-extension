package com.livk.socket.provider;

import com.livk.socket.config.ServerConfigurator;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * WebSocketServer
 * </p>
 *
 * @author livk
 * @date 2022/2/14
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/{sid}", configurator = ServerConfigurator.class)
public class WebSocketServer {

    private static final CopyOnWriteArraySet<WebSocketServer> websocketSet = new CopyOnWriteArraySet<>();

    private final AtomicInteger onlineCount = new AtomicInteger();

    private Session session;

    private String sid;

    /**
     * 异步推送消息
     *
     * @param message
     */
    public static void sendWholeAsyncMessage(String message) {
        for (WebSocketServer item : websocketSet) {
            try {
                item.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error("websocket异步发送消息异常{0}", e);
            }
        }
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(@PathParam("sid") String sid, String message) {
        log.error("推送消息到窗口" + sid + "，推送内容:" + message);
        for (WebSocketServer item : websocketSet) {
            try {
                // 这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sid") String sid) {
        this.session = session;
        websocketSet.add(this); // 加入set中
        addOnlineCount(); // 在线数加1
        this.sid = sid;
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        log.info("cid ---->>>{}", sid);

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        websocketSet.remove(this); // 从set中删除
        subOnlineCount(); // 在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        // 群发消息
        for (WebSocketServer item : websocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket发生错误");
        error.printStackTrace();
    }

    /**
     * 当前客户端发送消息 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    private int getOnlineCount() {
        return onlineCount.get();
    }

    private void addOnlineCount() {
        onlineCount.incrementAndGet();
    }

    private void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

}
