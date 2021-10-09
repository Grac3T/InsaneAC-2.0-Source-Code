package fr.whiizyyy.insaneac.check.speed;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.util.BukkitUtils;
import fr.whiizyyy.insaneac.util.MaterialList;
import fr.whiizyyy.insaneac.util.MathUtil;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;

public class SpeedD extends PacketCheck {
    private boolean placed;
    private Double lastX;
    private Double lastZ;
    private int threshold;
    private boolean lastOnGround;
    private int lastPlaceTick;

    public SpeedD() {
        super(Check.CheckType.SPEEDD, "D", "Speed", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Speed D.Alerts"));
        this.placed = false;
        this.lastX = null;
        this.lastZ = null;
        this.threshold = 0;
        this.lastOnGround = false;
        this.lastPlaceTick = 0;
        this.violations = -2.5;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.placed && this.lastPlaceTick > playerData.getLastTeleportTicks() && this.lastPlaceTick > MathUtil.highest(playerData.getLastInventoryOutTick(), playerData.getLastInventoryTick(), playerData.getLastSetSlot())) {
                final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
                if (packetPlayInFlying.g()) {
                    final double x = packetPlayInFlying.a();
                    final double z = packetPlayInFlying.c();
                    if (this.lastX != null && this.lastZ != null && packetPlayInFlying.f() && this.lastOnGround) {
                        final double distance = MathUtil.hypot(this.lastX - x, this.lastZ - z);
                        double limit = 0.065;
                        limit += BukkitUtils.getPotionLevel(player, PotionEffectType.SPEED) * 0.012;
                        if (player.getWalkSpeed() > 0.2) {
                            limit *= player.getWalkSpeed() / 0.2;
                        }
                        if (distance > limit) {
                            if (this.threshold++ > 10) {
                                AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(distance - limit));
                            }
                        }
                        else {
                            this.threshold = 0;
                            this.violations -= Math.min(this.violations + 5.0, 0.05);
                        }
                    }
                    this.lastX = x;
                    this.lastZ = z;
                }
                this.lastOnGround = packetPlayInFlying.f();
            }
            else {
                this.lastX = null;
                this.lastZ = null;
            }
        }
        else if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
            if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM || packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.DROP_ITEM || packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.DROP_ALL_ITEMS) {
                this.placed = false;
                this.threshold = 0;
            }
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace)packet;
            if (packetPlayInBlockPlace.a().getX() == -1 && (packetPlayInBlockPlace.a().getY() == -1 || packetPlayInBlockPlace.a().getY() == 255) && packetPlayInBlockPlace.a().getZ() == -1 && packetPlayInBlockPlace.d() == 0.0f && packetPlayInBlockPlace.e() == 0.0f && packetPlayInBlockPlace.f() == 0.0f && packetPlayInBlockPlace.getFace() == 255 && packetPlayInBlockPlace.getItemStack() != null && MaterialList.canPlace(packetPlayInBlockPlace.getItemStack())) {
                this.placed = true;
                this.lastPlaceTick = playerData.getTotalTicks();
                this.threshold = 0;
            }
        }
        else if (packet instanceof PacketPlayInHeldItemSlot) {
            this.placed = false;
        }
    }
}

