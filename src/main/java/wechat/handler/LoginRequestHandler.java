package wechat.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wechat.domain.Session;
import wechat.protocol.packet.LoginRequestPacket;
import wechat.protocol.packet.LoginResponsePacket;
import wechat.util.LoginUtil;
import wechat.util.SessionUtil;

import java.util.UUID;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg)
            throws Exception {
//        ctx.pipeline().writeAndFlush(login(msg));  //跟下面一个效果
        ctx.channel().writeAndFlush(login(ctx, msg));
    }

    private LoginResponsePacket login(ChannelHandlerContext ctx, LoginRequestPacket msg) {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(msg.getVersion());
        //登录校验
        if (valid(msg)) {
            //校验成功
            System.out.println("校验成功");
            responsePacket.setCode(0);
            String userId = UUID.randomUUID().toString().substring(0, 10);
            responsePacket.setUserId(userId);
            SessionUtil.bindSession(new Session(userId, msg.getUsername()), ctx.channel());
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println("校验失败");
            responsePacket.setCode(1);
            responsePacket.setMessage("用户名或密码错误");
        }
        return responsePacket;
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
