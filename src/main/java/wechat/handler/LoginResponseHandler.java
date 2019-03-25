package wechat.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wechat.domain.Session;
import wechat.domain.packet.LoginResponsePacket;
import wechat.util.SessionUtil;

import java.util.Date;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket responsePacket)
            throws Exception {
        if (responsePacket.getCode() == 0) {
            //登录成功
            System.out.println(new Date() + "客户端登陆成功,userid=" + responsePacket.getUserId() + " username=" + responsePacket.getUserName());
            SessionUtil.bindSession(new Session(responsePacket.getUserId(), responsePacket.getUserName()), ctx.channel());
        } else {
            System.out.println(new Date() + "客户端登陆失败,userid= " + responsePacket.getUserId() + "原因:" + responsePacket.getMessage());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接被关闭");
    }
}