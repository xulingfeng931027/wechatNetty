package wechat.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import wechat.handler.LoginResponseHandler;
import wechat.handler.MessageResponseHandler;
import wechat.protocol.packet.LoginRequestPacket;
import wechat.protocol.packet.MessageRequestPacket;
import wechat.util.LoginUtil;
import wechat.util.PacketDecoder;
import wechat.util.PacketEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WechatClient {
    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch)
                            throws Exception {
//                        ch.pipeline().addLast(new LifeCycleTestHandler());
//                        ch.pipeline().addLast(new Spliter(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });
        connect(bootstrap, "localhost", 8088, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }


    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
                try {
                    if (LoginUtil.hasLogin(channel)) {
                        String toUserId = br.readLine();
                        String message = br.readLine();
                        channel.writeAndFlush(new MessageRequestPacket(toUserId, message));
                    } else {
                        //如果没登录就要求登录
                        System.out.println("输入用户名登录");
                        String username = br.readLine();
                        //默认密码
                        loginRequestPacket.setUsername(username);
                        loginRequestPacket.setPassword("pwd");
                        //发送登录数据包
                        channel.writeAndFlush(loginRequestPacket);
                        waitForLoginResponse();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }

    private static void waitForLoginResponse() {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
