package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;


public class KillAuraK
extends PacketCheck {
	private boolean place;

    public KillAuraK() {
        super(Check.CheckType.KILL_AURAK, "K", "KillAura", Check.CheckVersion.RELEASE);
        this.place = false;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura K.Alerts"));
        this.violations = -1.0;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            this.place = false;
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace)packet;
            if (packetPlayInBlockPlace.a().getX() == -1 && (packetPlayInBlockPlace.a().getY() == -1 || packetPlayInBlockPlace.a().getY() == 255) && packetPlayInBlockPlace.a().getZ() == -1 && packetPlayInBlockPlace.d() == 0.0f && packetPlayInBlockPlace.e() == 0.0f && packetPlayInBlockPlace.f() == 0.0f && packetPlayInBlockPlace.getFace() == 255) {
                this.place = true;
            }
        }
        else {
            if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM && this.place && !playerData.hasLag()) {
                AlertsManager.getInstance().handleViolation(playerData, this, "", 1.0, false);
            }
        }
    }
}

