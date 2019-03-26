package wechat.domain.packet;

import wechat.protocol.Command;

import java.util.List;

public class CreateGroupRequestPacket extends Packet{
    private List<String> userIdList;

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    @Override
    public Byte getCommand() {
        return Command.GROUP_REQUEST;
    }
}
