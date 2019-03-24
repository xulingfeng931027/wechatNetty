package wechat.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wechat.protocol.packet.LoginRequestPacket;
import wechat.protocol.packet.LoginResponsePacket;
import wechat.util.LoginUtil;

import java.util.Date;
import java.util.UUID;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket responsePacket)
            throws Exception {
        if (responsePacket.getCode() == 0) {
            //登录成功
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + "客户端登陆成功");
        } else {
            System.out.println(new Date() + "客户端登陆失败,原因:" + responsePacket.getMessage());
        }
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(UUID.randomUUID().toString());
        packet.setUsername("flash");
        packet.setPassword("pwd");
        ctx.channel().writeAndFlush(packet);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接被关闭");
    }
}