package wechat.consolecommand;

import io.netty.channel.Channel;
import wechat.domain.packet.CreateGroupRequestPacket;

import java.util.Arrays;
import java.util.Scanner;

public class CreateGroupConsoleCommand implements ConsoleCommand {
    private static final String USER_ID_SPLITER = ",";
    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket packet = new CreateGroupRequestPacket();
        System.out.print("【拉人群聊】输入 userId 列表，userId 之间英文逗号隔开：");
        String userIds = scanner.next();
        packet.setUserIdList(Arrays.asList(userIds.split(USER_ID_SPLITER)));
        channel.writeAndFlush(packet);
    }
}
