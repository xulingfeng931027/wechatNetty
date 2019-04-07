package wechat.domain.packet.client;

import wechat.domain.packet.Packet;
import wechat.protocol.Command;

public class JoinGroupResponsePacket extends Packet {
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }
}
