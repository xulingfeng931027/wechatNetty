package wechat.handler.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 徐凌峰
 * @date 2019/5/11
 * 功能描述
 */
public class   InBoundHandlerA extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelInboundA:"+msg);
        ctx.fireChannelRead(msg);
    }
}
