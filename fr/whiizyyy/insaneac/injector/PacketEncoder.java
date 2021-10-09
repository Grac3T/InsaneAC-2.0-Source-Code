package fr.whiizyyy.insaneac.injector;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.util.logging.Level;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.util.SafeReflection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;

public class PacketEncoder
extends ChannelOutboundHandlerAdapter {
    private final PlayerData playerData;

    @SuppressWarnings("rawtypes")
	public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        if (o instanceof PacketPlayOutEntityTeleport) {
            SafeReflection.setOnGround((PacketPlayOutEntityTeleport)o, false);
        } else if (o instanceof PacketPlayOutEntity) {
            SafeReflection.setOnGround((PacketPlayOutEntity)o, false);
        }
        super.write(channelHandlerContext, o, channelPromise);
        try {
            this.playerData.handle((Packet)o, false);
        }
        catch (Throwable var5) {
            InsaneAC.getPlugin().getLogger().log(Level.SEVERE, "Error #IJPE001, Contact Whiizyyy#0681 ", var5);
        }
    }

    public PacketEncoder(PlayerData playerData) {
        this.playerData = playerData;
    }
}

