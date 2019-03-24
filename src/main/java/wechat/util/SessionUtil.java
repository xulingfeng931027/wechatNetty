package wechat.util;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import wechat.domain.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {
    /**
     * userid->channel的映射
     */
    private static Map<String, Channel> idChannelMap = new ConcurrentHashMap<>();
    private static final AttributeKey<Session> SESSION = AttributeKey.valueOf("session");


    public static void bindSession(Session session, Channel channel) {
        idChannelMap.put(session.getUserId(), channel);
        channel.attr(SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            idChannelMap.remove(getSession(channel).getUserId());
            channel.attr(SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(SESSION);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(SESSION).get();
    }

    public static Channel getChannel(String userId) {

        return idChannelMap.get(userId);
    }
}
