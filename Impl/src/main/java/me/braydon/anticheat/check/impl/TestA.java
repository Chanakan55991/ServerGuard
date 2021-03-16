package me.braydon.anticheat.check.impl;

import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.play.in.chat.WrappedPacketInChat;
import me.braydon.anticheat.check.Check;
import me.braydon.anticheat.player.PlayerData;
import me.braydon.api.check.CheckInfo;
import me.braydon.api.check.CheckType;

/**
 * This is just an example check to showcase the capabilities of the framework.
 *
 * @author Braydon
 */
@CheckInfo(name = "Test (A)", type = CheckType.TEST, experimental = true)
public class TestA extends Check {
    public TestA(PlayerData playerData) throws ClassNotFoundException {
        super(playerData);
    }

    @Override
    public void handle(byte packetId, NMSPacket nmsPacket, Object packet) {
        if (packetId == PacketType.Play.Client.CHAT) {
            WrappedPacketInChat wrappedPacketInChat = new WrappedPacketInChat(nmsPacket);
            if (wrappedPacketInChat.getMessage().equalsIgnoreCase("test"))
                flag("message=" + wrappedPacketInChat.getMessage());
            player.sendMessage("You sent the chat message \"" + wrappedPacketInChat.getMessage() + "\"");
        }
    }
}