package fr.whiizyyy.insaneac.injector;

import java.util.logging.Level;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.data.PlayerData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.server.v1_8_R3.Packet;

public class PacketDecoder
extends ChannelInboundHandlerAdapter {
    private final PlayerData playerData;
    private ChannelHandlerContext lastContext;

    public PacketDecoder(PlayerData playerData) {
        this.playerData = playerData;
        playerData.setPacketDecoder(this);
    }

    @SuppressWarnings("rawtypes")
	public void sendFakePacket(Packet packet) {
        try {
            super.channelRead(this.lastContext, (Object)packet);
        }
        catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
	public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        super.channelRead(channelHandlerContext, o);
        try {
            this.lastContext = channelHandlerContext;
            this.playerData.handle((Packet)o, true);
        }
        catch (Throwable var4) {
            InsaneAC.getPlugin().getLogger().log(Level.SEVERE, "Error #IJPD001, Contact Whiizyyy#0681 ");
        }
    }
}

