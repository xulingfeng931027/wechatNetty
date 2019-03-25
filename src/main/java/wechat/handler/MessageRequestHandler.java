package wechat.handler;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wechat.domain.Session;
import wechat.domain.packet.MessageRequestPacket;
import wechat.domain.packet.MessageResponsePacket;
import wechat.util.SessionUtil;

/**
 * @author paul
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket)
            throws Exception {
        //1.拿到消息所属的session
        Session session = SessionUtil.getSession(ctx.channel());
        //2.构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUsername(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());
        //3.拿到消息接收方的channel
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());
        //4.将消息发送给消息接收方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.err.println("[" + messageRequestPacket.getToUserId() + "] 不在线，发送失败!");
        }

    }

}
