package fr.whiizyyy.insaneac.check.killaura;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class KillAuraG
extends PacketCheck {
    private boolean attack;
    private boolean place;

    public KillAuraG() {
        super(Check.CheckType.KILL_AURAG, "G", "KillAura", Check.CheckVersion.RELEASE);
        this.violations = -1.0;
        this.attack = false;
        this.place = false;
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.KillAura G.Alerts"));
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.place && this.attack) {
                AlertsManager.getInstance().handleViolation(playerData, this, "", 1.0, true);
            }
            this.place = false;
            this.attack = false;
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
            if (packetPlayInUseEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.attack = true;
            }
        }
        else {
            final BlockPosition blockPosition;
            if (packet instanceof PacketPlayInBlockPlace && (blockPosition = ((PacketPlayInBlockPlace)packet).a()).getX() != -1 && blockPosition.getY() != -1 && blockPosition.getZ() != -1) {
                this.place = true;
            }
        }
    }
}

