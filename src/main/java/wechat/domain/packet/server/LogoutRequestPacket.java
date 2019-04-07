package wechat.domain.packet.server;

import wechat.domain.packet.Packet;
import wechat.protocol.Command;

/**
 * @author 徐凌峰
 * @date 2019/4/7
 * 功能描述
 */
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.LOG_OUT_REQUEST;
    }
}
