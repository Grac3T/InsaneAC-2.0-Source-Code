package fr.whiizyyy.insaneac.data;


import com.google.common.collect.EvictingQueue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import fr.whiizyyy.insaneac.check.AimCheck;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.EventCheck;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.check.ReachCheck;
import fr.whiizyyy.insaneac.check.alert.Alert;
import fr.whiizyyy.insaneac.injector.PacketDecoder;
import fr.whiizyyy.insaneac.injector.PacketEncoder;
import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import fr.whiizyyy.insaneac.manager.OptionsManager;
import fr.whiizyyy.insaneac.manager.PlayerManager;
import fr.whiizyyy.insaneac.util.Cuboid;
import fr.whiizyyy.insaneac.util.CustomBoundingBox;
import fr.whiizyyy.insaneac.util.CustomLocation;
import fr.whiizyyy.insaneac.util.MathUtil;
import fr.whiizyyy.insaneac.util.SafeReflection;

import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketHandshakingInSetProtocol;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity.EnumEntityUseAction;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_8_R3.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R3.PacketPlayOutKeepAlive;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerData {
    private final Player player;
    private final EntityPlayer entityPlayer;
    @Deprecated
    private final Queue<PlayerLocation> recentMoveList = new ConcurrentLinkedQueue<PlayerLocation>();
    private final Map<Integer, Deque<PlayerLocation>> recentMoveMap = new ConcurrentHashMap<Integer, Deque<PlayerLocation>>();
    private final Queue<PlayerLocation> teleportList = new ConcurrentLinkedQueue<PlayerLocation>();
    private final List<Check> checks;
    private final List<PacketCheck> packetChecks;
    private final List<MovementCheck> movementChecks;
    private final List<AimCheck> aimChecks;
    private final List<EventCheck> eventChecks;
    private final List<ReachCheck> reachChecks;
    private final Queue<CustomBoundingBox> boundingBoxes;
    private final PlayerLocation location;
    private PlayerLocation lastLocation;
    private PlayerLocation lastLastLocation;
    private CustomBoundingBox lastPosition1;
    private CustomLocation lastMovePacket;
    private long lastDelayedMovePacket;
    private boolean onGround;
    private long lastFlying;
    private long lastDelayed;
    private long lastFast;
    private long lastWindowClick;
    private PlayerData lastAttacked;
    private int lastAttackedId;
    private boolean receivedKeepAlive = false;
    private boolean digging = false;
    private boolean swingDigging = false;
    private boolean abortedDigging = false;
    private boolean stoppedDigging = false;
    private BlockPosition diggingBlock = null;
    private EnumDirection diggingBlockFace = null;
    private boolean banned = false;
    private boolean enabled = true;
    private int lastAttackTicks = 600;
    private int packets = 0;
    private int nonMoveTicks = 0;
    private Queue<Alert> queuedAlerts = new ConcurrentLinkedQueue<Alert>();
    private Queue<Alert> spoofedAlerts = new ConcurrentLinkedQueue<Alert>();
    private boolean checkSpoofing = false;
    private boolean spoofBan = false;
    private Check spoofBanCheck = null;
    private long lastVapeString = 0L;
    private final Map<Integer, Long> keepAliveMap = new ConcurrentHashMap<Integer, Long>();
    private int ping;
    private int averagePing;
    private final Queue<BiConsumer<Integer, Double>> pingQueue = new ConcurrentLinkedQueue<BiConsumer<Integer, Double>>();
    private final Queue<Integer> connectionFrequency = new ConcurrentLinkedQueue<Integer>();
    private final Queue<Double> recentConnectionFrequencies = new ConcurrentLinkedQueue<Double>();
    private long lastPosition = 0L;
    private int teleportTicks = 0;
    private int lastTeleportTicks = 0;
    private int flyingTicks = 0;
    private int groundTicks = 0;
    private int velocityTicks = 0;
    private int verticalVelocityTicks = -20;
    private int horizontalVelocityTicks = 0;
    private int horizontalSpeedTicks;
    private int totalTicks = 0;
    private int keepAliveTicks = 0;
    private boolean inventoryOpen;
    private int lastInventoryTick = 0;
    private int lastInventoryOutTick = 0;
    private int steerTicks = 0;
    private int suffocationTicks = 100;
    private int lastSetSlot = 0;
    private int cps;
    private boolean isBlocking = false;
    private boolean spawnedIn = false;
    private Boolean sprinting = null;
    private Boolean sneaking = null;
    //private BadPacketsC keepAliveCheck;
    //private MiscB pingSpoofCheck;
    private ClientVersion clientVersion;
    private int lastFakeEntityTicks = 0;
    private int lastFakeEntityDamageTicks = 0;
    private StaticFakeEntity reachEntity = null;
    private double lastVelY;
    private double velY;
    private double lastVelX;
    private double velX;
    private double lastVelZ;
    private double velZ;
    private boolean alerts;
    private boolean debug;
    private boolean bypass;
    private PacketDecoder packetDecoder;
    private PacketEncoder packetEncoder;
    private GolemEntity golemEntity = null;
    private boolean t2;
    private Integer sendT2 = null;
    private String brand;
    private long lastAttackPacket;
    private double velocityY;
    private boolean underBlock;
    private boolean inLiquid;
    private long lastVelocity;
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PlayerData(Player player, List<Check> checks) {
        this.player = player;
        this.entityPlayer = ((CraftPlayer)player).getHandle();
        this.boundingBoxes = EvictingQueue.create(8);
        this.checks = checks;
        this.packetChecks = ImmutableList.copyOf((Collection)checks.stream().filter(check -> check instanceof PacketCheck).map(check -> (PacketCheck)check).collect(Collectors.toList()));
        this.movementChecks = ImmutableList.copyOf((Collection)checks.stream().filter(check -> check instanceof MovementCheck).map(check -> (MovementCheck)check).collect(Collectors.toList()));
        this.aimChecks = ImmutableList.copyOf((Collection)checks.stream().filter(check -> check instanceof AimCheck).map(check -> (AimCheck)check).collect(Collectors.toList()));
        this.eventChecks = ImmutableList.copyOf((Collection)checks.stream().filter(check -> check instanceof EventCheck).map(check -> (EventCheck)check).collect(Collectors.toList()));
        this.reachChecks = ImmutableList.copyOf((Collection)checks.stream().filter(check -> check instanceof ReachCheck).map(check -> (ReachCheck)check).collect(Collectors.toList()));
        //this.keepAliveCheck = (BadPacketsC) checks.stream().filter(check -> check instanceof BadPacketsC).findFirst().orElse(null);
        //this.pingSpoofCheck = (MiscB) checks.stream().filter(check -> check instanceof MiscB).findFirst().orElse(null);
        this.location = new PlayerLocation(System.currentTimeMillis(), this.totalTicks, this.entityPlayer.locX, this.entityPlayer.locY, this.entityPlayer.locZ, this.entityPlayer.yaw, this.entityPlayer.pitch, this.entityPlayer.onGround);
        this.lastLocation = this.location.clone();
        this.lastLastLocation = this.location.clone();
        this.alerts = player.hasMetadata("IAC_ALERTS") && player.hasPermission(OptionsManager.getInstance().getModPermission());
        //if (player.hasMetadata("client_version")) {
          //  this.clientVersion = (ClientVersion)((Object)((MetadataValue)player.getMetadata("client_version").stream().findFirst().get()).value());
        //}
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public void handle(Packet packet, boolean serverBound) {
        if (this.enabled) {
            if (serverBound) {
                long timestamp = System.currentTimeMillis();
                if (packet instanceof PacketPlayInFlying) {
                    int lastPing;
                    PlayerLocation teleport;
                    final PacketPlayInFlying packetPlayInFlying = (PacketPlayInFlying)packet;
                    final CustomLocation customLocation = new CustomLocation(packetPlayInFlying.a(), packetPlayInFlying.b(), packetPlayInFlying.c(), packetPlayInFlying.d(), packetPlayInFlying.e());
                    final CustomLocation lastLocation5 = this.getLastMovePacket();
                    if (lastLocation5 != null) {
                    if (!packetPlayInFlying.g()) {
                        customLocation.setX(lastLocation5.getX());
                        customLocation.setY(lastLocation5.getY());
                        customLocation.setZ(lastLocation5.getZ());
                    }
                    if (!packetPlayInFlying.h()) {
                        customLocation.setYaw(lastLocation5.getYaw());
                        customLocation.setPitch(lastLocation5.getPitch());
                    }
                    if (System.currentTimeMillis() - lastLocation.getTimestamp() > 110L) {
                        this.setLastDelayedMovePacket(System.currentTimeMillis());
                    }
                    }
                    this.setLastMovePacket(customLocation);
                    final CustomBoundingBox boundingBox = new CustomBoundingBox(customLocation.getX(), customLocation.getZ());
                    this.setLastPosition(boundingBox);
                    this.onGround = packetPlayInFlying.g();
                    this.location.setTimestamp(timestamp);
                    if (packetPlayInFlying.g()) {
                        this.location.setX(packetPlayInFlying.a());
                        this.location.setY(packetPlayInFlying.b());
                        this.location.setZ(packetPlayInFlying.c());
                    }
                    if (packetPlayInFlying.h()) {
                        this.location.setYaw(packetPlayInFlying.d());
                        this.location.setPitch(packetPlayInFlying.e());
                    }
                    this.location.setOnGround(packetPlayInFlying.f());
                    final PlayerLocation clonedLocation = this.location.clone();
                    new BukkitRunnable(){

                        public void run() {
                            PlayerData.this.processPosition(clonedLocation, packetPlayInFlying.g() && PlayerData.this.packets < 20);
                        }
                    }.runTask((Plugin)InsaneAC.getPlugin());
                    this.packets = this.player.getVehicle() == null && this.steerTicks > this.getPingTicks() ? (packetPlayInFlying.g() ? 0 : ++this.packets) : 0;
                    this.nonMoveTicks = packetPlayInFlying.g() ? 0 : ++this.nonMoveTicks;
                    long diff = timestamp - this.lastFlying;
                    if (diff > 80L) {
                        this.lastDelayed = timestamp;
                    }
                    if (diff < 25L) {
                        this.lastFast = timestamp;
                    }
                    this.lastFlying = timestamp;
                    this.teleportTicks = this.isTeleporting() ? 0 : ++this.teleportTicks;
                    this.connectionFrequency.add(50 - (int)diff);
                    if (this.abortedDigging) {
                        this.abortedDigging = false;
                        this.swingDigging = false;
                        this.digging = false;
                    }
                    if (this.stoppedDigging) {
                        this.stoppedDigging = false;
                        this.digging = false;
                    }
                    if ((teleport = this.teleportList.peek()) != null) {
                        lastPing = this.totalTicks - teleport.getTickTime();
                        if (packetPlayInFlying.g() && lastPing >= this.getMoveTicks() && teleport.sameLocation(this.location)) {
                            this.teleportList.poll();
                            this.lastVelY = 0.0;
                            this.velY = 0.0;
                            this.lastVelX = 0.0;
                            this.velX = 0.0;
                            this.lastVelZ = 0.0;
                            this.velZ = 0.0;
                            this.lastLocation = this.location.clone();
                            this.lastLastLocation = this.location.clone();
                            this.lastTeleportTicks = this.totalTicks;
                            this.spawnedIn = false;
                            return;
                        }
                        if (lastPing > (packetPlayInFlying.g() ? this.getPingTicks() * 2 : this.getPingTicks() * 4)) {
                            this.teleportList.poll();
                        }
                    }
                    if (this.lastVelY == 0.0 && this.velY != 0.0) {
                        if (packetPlayInFlying.f()) {
                            this.velY = 0.0;
                        } else {
                            this.velY -= 0.08;
                            this.velY *= 0.98;
                        }
                    }
                    if (packetPlayInFlying.f() && this.teleportTicks > this.getPingTicks()) {
                        this.spawnedIn = true;
                    }
                    ++this.totalTicks;
                    ++this.lastAttackTicks;
                    this.flyingTicks = this.player.isFlying() ? 0 : ++this.flyingTicks;
                    this.groundTicks = packetPlayInFlying.f() ? 0 : ++this.groundTicks;
                    ++this.steerTicks;
                    ++this.velocityTicks;
                    ++this.horizontalSpeedTicks;
                    ++this.verticalVelocityTicks;
                    ++this.horizontalVelocityTicks;
                    ++this.keepAliveTicks;
                    ++this.suffocationTicks;
                    if (!this.lastLocation.sameLocation(this.location)) {
                        this.movementChecks.forEach(movementCheck -> movementCheck.handle(this.player, this, this.lastLocation, this.location, timestamp));
                    }
                    if (!this.lastLocation.sameDirection(this.location)) {
                        this.aimChecks.forEach(aimCheck -> aimCheck.handle(this.player, this, this.lastLocation, this.location, timestamp));
                    }
                    this.lastLastLocation = this.lastLocation.clone();
                    this.lastLocation = this.location.clone();
                    lastPing = this.ping;
                    if (!(this.getLastAttackTicks() > 1 || this.lastAttacked == null || this.hasLag() || this.hasFast() || this.teleportTicks <= this.getPingTicks() + 2 || this.lastAttacked.getTeleportTicks() <= this.lastAttacked.getPingTicks() + this.getPingTicks() + 2 || this.lastAttacked.hasLag(timestamp - (long)this.getPing()) || this.lastAttacked.hasFast(timestamp - (long)this.getPing()) || this.player.getGameMode() == GameMode.CREATIVE)) {
                        PlayerLocation location = this.location.clone();
                        PlayerLocation lastLocation = this.lastLastLocation.clone();
                        Deque<PlayerLocation> recentMovesQueue = (Deque)this.recentMoveMap.get(this.lastAttackedId);
                        if (recentMovesQueue != null && !recentMovesQueue.isEmpty() && recentMovesQueue.size() > 10) {
                            List<PlayerLocation> recentMoves = (List)recentMovesQueue.stream().map(PlayerLocation::clone).collect(Collectors.toList());
                            this.pingQueue.add((ping, connection) -> {
                                List<PlayerLocation> possibleMoves = (List)recentMoves.stream().filter((move) -> {
                                    return (double)(timestamp - move.getTimestamp() - (long)ping.intValue() - 75L) <= 25.0D + connection.doubleValue();
                                }).collect(Collectors.toList());
                                if (!possibleMoves.isEmpty()) {
                                    PlayerLocation earliestLocation = (PlayerLocation)possibleMoves.stream().min(Comparator.comparingLong(PlayerLocation::getTimestamp)).get();
                                    int index = recentMoves.indexOf(earliestLocation);
                                    if (index > 0) {
                                        possibleMoves.add(recentMoves.get(index - 1));
                                    }

                                    List<DistanceData> distanceList = (List)possibleMoves.stream().map(PlayerLocation::hitbox).map((hitbox) -> {
                                        return hitbox.expand(0.0325D, 0.0D, 0.0325D);
                                    }).map((hitbox) -> {
                                        double distanceNow = hitbox.distanceXZ(location.getX(), location.getZ());
                                        double distanceBefore = hitbox.distanceXZ(lastLocation.getX(), lastLocation.getZ());
                                        double x1 = location.getX() - hitbox.cX();
                                        double x2 = lastLocation.getX() - hitbox.cX();
                                        double z1 = location.getZ() - hitbox.cZ();
                                        double z2 = lastLocation.getZ() - hitbox.cZ();
                                        double y1 = location.getY() - hitbox.cY();
                                        double y2 = lastLocation.getY() - hitbox.cY();
                                        return new DistanceData(hitbox, Math.abs(x1) < Math.abs(x2) ? x1 : x2, Math.abs(z1) < Math.abs(z2) ? z1 : z2, Math.abs(y1) < Math.abs(y2) ? y1 : y2, Math.min(distanceBefore, distanceNow));
                                    }).collect(Collectors.toList());
                                    List<Double> reachList = (List)distanceList.stream().map(DistanceData::getDist).collect(Collectors.toList());
                                    PlayerLocation[] locationArray = (PlayerLocation[])((PlayerLocation[])possibleMoves.toArray(new PlayerLocation[0]));
                                    DistanceData horizontalData = (DistanceData)distanceList.stream().min(Comparator.comparingDouble(DistanceData::getDist)).get();
                                    double horizontal = horizontalData.getDist();
                                    double movement = MathUtil.highest(reachList) - horizontal;
                                    double angle = Math.toRadians(Math.min(MathUtil.getMinimumAngle(location, locationArray).doubleValue(), MathUtil.getMinimumAngle(lastLocation, locationArray).doubleValue()));
                                    double extra = Math.abs(Math.sin(angle)) * horizontal;
                                    double vertical = Math.abs(Math.sin(Math.toRadians((double)Math.abs(location.getPitch()))) * horizontal);
                                    double reach = MathUtil.hypot(new double[]{horizontal, vertical, extra}) - 0.01D;
                                    ReachData reachData = new ReachData(this, location, horizontalData, movement, horizontal, extra, vertical, reach);
                                    Iterator var28 = this.reachChecks.iterator();

                                    while(var28.hasNext()) {
                                        ReachCheck reachCheck = (ReachCheck)var28.next();
                                        reachCheck.handle(this.player, this, reachData, System.currentTimeMillis());
                                    }
                                }

                            });
                        }
                    }
                } else if (packet instanceof PacketPlayInUseEntity) {
                    PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity)packet;
                    if (packetPlayInUseEntity.a() == EnumEntityUseAction.ATTACK) {
                        if (this.horizontalVelocityTicks > this.getPingTicks() / 2 - 2) {
                            this.velX *= 0.6;
                            this.velZ *= 0.6;
                        }
                        this.lastAttackedId = SafeReflection.getAttackedEntity(packetPlayInUseEntity);
                        net.minecraft.server.v1_8_R3.Entity entity = packetPlayInUseEntity.a(this.entityPlayer.getWorld());
                        if (entity != null) {
                            if (entity instanceof EntityPlayer) {
                                if (this.spoofBan && ThreadLocalRandom.current().nextDouble() < 0.25) {
                                    AlertsManager.getInstance().handleBan(this, this.spoofBanCheck, false);
                                }
                                if (this.checkSpoofing && ThreadLocalRandom.current().nextDouble() < 0.25) {
                                    while (this.checkSpoofing) {
                                        Alert alert = this.spoofedAlerts.poll();
                                        if (alert == null) {
                                            this.checkSpoofing = false;
                                            continue;
                                        }
                                        if (timestamp - alert.getTimestamp() >= TimeUnit.SECONDS.toMillis(15L)) continue;
                                        AlertsManager.getInstance().getExecutorService().submit(() -> AlertsManager.getInstance().handleAlert(this, alert.getCheck(), alert.getVl()));
                                        break;
                                    }
                                }
                                EntityPlayer entityPlayer = (EntityPlayer)entity;
                                this.lastAttacked = entityPlayer.playerConnection != null ? PlayerManager.getInstance().getPlayers().get(entityPlayer.getBukkitEntity().getPlayer().getUniqueId()) : null;
                            } else {
                                this.lastAttacked = null;
                            }
                        } else {
                            this.lastAttacked = null;
                        }
                        this.lastAttackTicks = 0;
                    }
                } else if (!(packet instanceof PacketPlayInKeepAlive)) {
                    if (packet instanceof PacketPlayInEntityAction) {
                    	PacketPlayInEntityAction packetPlayInEntityAction = (PacketPlayInEntityAction)packet;
                        if (packetPlayInEntityAction.b() == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING) {
                            this.sprinting = true;
                        } else if (packetPlayInEntityAction.b() == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                            this.sprinting = false;
                        } else if (packetPlayInEntityAction.b() == PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING) {
                            this.sneaking = true;
                        } else if (packetPlayInEntityAction.b() == PacketPlayInEntityAction.EnumPlayerAction.STOP_SNEAKING) {
                            this.sneaking = false;
                        }
                    } else if (packet instanceof PacketPlayInClientCommand) {
                        PacketPlayInClientCommand packetPlayInClientCommand = (PacketPlayInClientCommand)packet;
                        if (packetPlayInClientCommand.a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
                            this.inventoryOpen = true;
                            this.lastInventoryTick = this.totalTicks;
                        }
                    } else if (packet instanceof PacketPlayInCloseWindow) {
                        this.inventoryOpen = false;
                    } else if (packet instanceof PacketPlayInWindowClick) {
                        this.lastWindowClick = timestamp;
                    } else if (packet instanceof PacketHandshakingInSetProtocol) {
                        PacketHandshakingInSetProtocol packetHandshakingInSetProtocol = (PacketHandshakingInSetProtocol)packet;
                        int protocol = SafeReflection.getProtocolVersion(packetHandshakingInSetProtocol);
                        this.clientVersion = protocol == 47 ? ClientVersion.VERSION1_7 : (protocol == 5 ? ClientVersion.VERSION1_8 : ClientVersion.VERSION_UNSUPPORTED);
                        this.player.setMetadata("client_version", (MetadataValue)new FixedMetadataValue((Plugin)InsaneAC.getPlugin(), (Object)this.clientVersion));
                    } else if (packet instanceof PacketPlayInBlockDig) {
                    	PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig)packet;
                        if (this.player.getGameMode() == GameMode.CREATIVE) {
                            this.digging = false;
                            this.swingDigging = false;
                        } else if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                            this.digging = true;
                            this.diggingBlock = packetPlayInBlockDig.a();
                            this.diggingBlockFace = packetPlayInBlockDig.b();
                            this.swingDigging = true;
                            this.abortedDigging = false;
                            this.stoppedDigging = false;
                        } else if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                            this.abortedDigging = true;
                        } else if (packetPlayInBlockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                            this.stoppedDigging = true;
                        }
                    } else if (packet instanceof PacketPlayInSteerVehicle) {
                        this.steerTicks = 0;
                    }
                } else {
                    PacketPlayInKeepAlive packetPlayInKeepAlive = (PacketPlayInKeepAlive)packet;
                    final int protocol = packetPlayInKeepAlive.a();
                    this.receivedKeepAlive = true;
                    Long sent = this.keepAliveMap.remove(protocol);
                    if (sent != null) {
                        int newPing;
                        BiConsumer<Integer, Double> pingConsumer;
                        this.ping = newPing = (int)(timestamp - sent);
                        this.averagePing = (this.averagePing * 3 + this.ping) / 4;
                        double deviation = MathUtil.variance(0, this.connectionFrequency);
                        this.recentConnectionFrequencies.add(deviation);
                        if (this.recentConnectionFrequencies.size() > 4) {
                            this.recentConnectionFrequencies.poll();
                        }
                        while ((pingConsumer = this.pingQueue.poll()) != null) {
                            pingConsumer.accept(this.ping, deviation);
                        }
                        this.connectionFrequency.clear();
                    }
                    this.keepAliveTicks = 0;
                }
                this.packetChecks.forEach(check -> check.handle(this.player, this, packet, timestamp));
            } else {
                long timestamp = System.currentTimeMillis();
                if (packet instanceof PacketPlayOutPosition) {
                    timestamp = System.currentTimeMillis();
                    PacketPlayOutPosition packetPlayOutPosition = (PacketPlayOutPosition)packet;
                    PlayerLocation clonedLocation = SafeReflection.getLocation(timestamp, this.totalTicks, packetPlayOutPosition);
                    this.teleportList.add(clonedLocation);
                    this.teleportTicks = 0;
                    this.lastPosition = timestamp;
                } else if (packet instanceof PacketPlayOutKeepAlive) {
                    PlayerConnection playerConnection = this.entityPlayer.playerConnection;
                    SafeReflection.setNextKeepAliveTime(playerConnection, SafeReflection.getNextKeepAliveTime(playerConnection) + 36);
                    PacketPlayOutKeepAlive packetPlayOutKeepAlive = (PacketPlayOutKeepAlive)packet;
                    int keepAliveNumber = SafeReflection.getKeepAliveId(packetPlayOutKeepAlive);
                    this.keepAliveMap.put(keepAliveNumber, timestamp);
                } else if (packet instanceof PacketPlayOutEntityVelocity) {
                    PacketPlayOutEntityVelocity packetPlayOutEntityVelocity = (PacketPlayOutEntityVelocity)packet;
                    VelocityPacket velocityPacket = SafeReflection.getVelocity(packetPlayOutEntityVelocity);
                    if (velocityPacket.getEntityId() == this.player.getEntityId()) {
                        this.velY = (double)velocityPacket.getY() / 8000.0;
                        this.velX = (double)velocityPacket.getX() / 8000.0;
                        this.velZ = (double)velocityPacket.getZ() / 8000.0;
                        this.horizontalVelocityTicks = 0;
                        int currentTicks = this.totalTicks;
                        Cuboid cuboid = new Cuboid(this.location).move(0.0, 1.5, 0.0).expand(0.5, 1.0, 0.5);
                        org.bukkit.World world = this.player.getWorld();
                        final Runnable runnable = () -> {
                            int ticksOffset = currentTicks - this.totalTicks;
                            boolean blockAbove = false;
                            if (this.velY > 0.0) {
                                for (Block block : cuboid.getBlocks(world)) {
                                    if (block.isEmpty()) continue;
                                    blockAbove = true;
                                    break;
                                }
                            }
                            if (!blockAbove && this.lastVelY == 0.0 && this.ping > 0) {
                                this.lastVelY = this.velY;
                                this.verticalVelocityTicks = ticksOffset;
                            } else {
                                this.lastVelY = 0.0;
                            }
                        };
                        new BukkitRunnable(){

                            public void run() {
                                runnable.run();
                            }
                        }.runTask((Plugin)InsaneAC.getPlugin());
                        this.velocityTicks = Math.min(this.velocityTicks, 0) - (int)Math.ceil(Math.pow(MathUtil.hypotSquared(this.velX, this.velY, this.velZ) * 2.0, 1.75) * 4.0);
                        this.horizontalSpeedTicks = Math.min(this.horizontalSpeedTicks, 0) - (int)Math.ceil(Math.pow(MathUtil.hypotSquared(this.velX, this.velZ) * 2.0, 2.0) * 8.0);
                    }
                } else if (packet instanceof PacketPlayOutExplosion) {
                    this.velocityTicks = 0;
                    this.horizontalSpeedTicks = 0;
                } else if (packet instanceof PacketPlayOutOpenWindow) {
                    this.digging = false;
                    this.lastInventoryOutTick = this.totalTicks;
                } else if (packet instanceof PacketPlayOutEntityTeleport) {
                    PacketPlayOutEntityTeleport packetPlayOutEntityTeleport = (PacketPlayOutEntityTeleport)packet;
                    int entityId = SafeReflection.getEntityId(packetPlayOutEntityTeleport);
                    Deque<PlayerLocation> recentMoveQueue = this.recentMoveMap.get(entityId);
                    if (recentMoveQueue == null) {
                        recentMoveQueue = new ConcurrentLinkedDeque<PlayerLocation>();
                        this.recentMoveMap.put(entityId, recentMoveQueue);
                    }
                    recentMoveQueue.add(SafeReflection.getLocation(System.currentTimeMillis(), this.totalTicks, packetPlayOutEntityTeleport));
                    if (recentMoveQueue.size() > 20) {
                        recentMoveQueue.poll();
                    }
                } else if (!(packet instanceof PacketPlayOutEntity.PacketPlayOutRelEntityMove) && !(packet instanceof PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook)) {
                    if (packet instanceof PacketPlayOutEntityDestroy) {
                        PacketPlayOutEntityDestroy packetPlayOutEntityDestroy = (PacketPlayOutEntityDestroy)packet;
                        this.recentMoveMap.keySet().removeAll(Arrays.stream(SafeReflection.getEntities(packetPlayOutEntityDestroy)).boxed().collect(Collectors.toList()));
                    } else if (packet instanceof PacketPlayOutSetSlot) {
                        this.lastSetSlot = this.totalTicks;
                    }
                } else {
                    PacketPlayOutEntity packetPlayOutEntity = (PacketPlayOutEntity)packet;

                    int entityId = SafeReflection.getEntityId(packetPlayOutEntity);
                    Deque<PlayerLocation> recentMoveQueue = this.recentMoveMap.get(entityId);
                    if (recentMoveQueue != null && !recentMoveQueue.isEmpty()) {
                        Vector movement = SafeReflection.getMovement(packetPlayOutEntity);
                        PlayerLocation location = recentMoveQueue.peekLast().clone().add(movement.getX(), movement.getY(), movement.getZ());
                        location.setTickTime(this.totalTicks);
                        location.setTimestamp(System.currentTimeMillis());
                        recentMoveQueue.add(location);
                        if (recentMoveQueue.size() > 20) {
                            recentMoveQueue.poll();
                        }
                    }
                }
                if (this.golemEntity != null) {
                    this.golemEntity.handle(this, packet);
                }
            }
        }
    }

    public int getMoveTicks() {
        return (int)Math.floor((double)Math.min(this.ping, this.averagePing) / 125.0);
    }

    @EventHandler
    public void handle(Event event) {
        long timestamp = System.currentTimeMillis();
        if (event instanceof EntityDamageEvent) {
            EntityDamageEvent entityDamageEvent = (EntityDamageEvent)event;
            if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                this.suffocationTicks = 0;
            }
        } else if (event instanceof PlayerRespawnEvent) {
            this.inventoryOpen = false;
        }
        for (EventCheck eventCheck : this.eventChecks) {
            eventCheck.handle(this.player, this, event, timestamp);
        }
    }
    
    private void processPosition(PlayerLocation location, boolean hasMove) {
        if (this.enabled) {
            long timestamp = System.currentTimeMillis();
            if (this.recentMoveList.size() > 20) {
                this.recentMoveList.poll();
            }
            location.setTimestamp(timestamp);
            this.recentMoveList.add(location);
            if (!hasMove) {
                for (PlayerData playerData : PlayerManager.getInstance().getPlayers().values()) {
                    Deque<PlayerLocation> recentMoveQueue = playerData.getRecentMoveMap().get(this.entityPlayer.getId());
                    if (recentMoveQueue == null || recentMoveQueue.isEmpty() || (location = recentMoveQueue.peekLast()) == null) continue;
                    location = location.clone();
                    location.setTickTime(playerData.getTotalTicks());
                    location.setTimestamp(timestamp);
                    recentMoveQueue.add(location);
                    if (recentMoveQueue.size() <= 20) continue;
                    recentMoveQueue.poll();
                }
            }
        }
    }

    public int getPingTicks() {
        return (this.receivedKeepAlive ? (int)Math.ceil((double)this.getPing() / 50.0) : 20) + 1;
    }

    public int getMaxPingTicks() {
        return (this.receivedKeepAlive ? (int)Math.ceil((double)Math.max(this.ping, this.averagePing) / 50.0) : 20) + 1;
    }

    public PlayerLocation getLocation(int ticks) {
        int size = this.recentMoveList.size() - 1;
        return size > ticks ? (PlayerLocation)Iterables.get(this.recentMoveList, (int)(size - ticks)) : null;
    }

    public boolean hasLag(long timestamp) {
        return this.lastFlying != 0L && this.lastDelayed != 0L && timestamp - this.lastDelayed < 150L || System.currentTimeMillis() - this.lastFlying > 90;
    }

    public boolean hasLag() {
        return this.hasLag(this.lastFlying);
    }

    public boolean hasFast(long timestamp) {
        return this.lastFlying != 0L && this.lastFast != 0L && timestamp - this.lastFast < 110L;
    }

    public boolean hasFast() {
        return this.hasFast(this.lastFlying);
    }

    public boolean isTeleporting() {
        return !this.teleportList.isEmpty();
    }

    public long getTimeDifference(PlayerLocation move, long timestamp, int ping) {
        return Math.abs(timestamp - move.getTimestamp() - (long)ping - 50L);
    }

    public void fuckOff() {
        Channel channel = (Channel)SafeReflection.getLocalField(this.getEntityPlayer().playerConnection.networkManager.getClass(), (Object)this.getEntityPlayer().playerConnection.networkManager, "m");
        channel.unsafe().closeForcibly();
        this.enabled = false;
        throw new IllegalArgumentException();
    }

    public Player getPlayer() {
        return this.player;
    }

    public EntityPlayer getEntityPlayer() {
        return this.entityPlayer;
    }

    @Deprecated
    public Queue<PlayerLocation> getRecentMoveList() {
        return this.recentMoveList;
    }

    public Map<Integer, Deque<PlayerLocation>> getRecentMoveMap() {
        return this.recentMoveMap;
    }

    public Queue<PlayerLocation> getTeleportList() {
        return this.teleportList;
    }

    public List<Check> getChecks() {
        return this.checks;
    }

    public List<PacketCheck> getPacketChecks() {
        return this.packetChecks;
    }

    public List<MovementCheck> getMovementChecks() {
        return this.movementChecks;
    }

    public List<AimCheck> getAimChecks() {
        return this.aimChecks;
    }

    public List<EventCheck> getEventChecks() {
        return this.eventChecks;
    }

    public List<ReachCheck> getReachChecks() {
        return this.reachChecks;
    }

    public PlayerLocation getLocation() {
        return this.location;
    }

    public PlayerLocation getLastLocation() {
        return this.lastLocation;
    }

    public PlayerLocation getLastLastLocation() {
        return this.lastLastLocation;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public long getLastFlying() {
        return this.lastFlying;
    }

    public long getLastDelayed() {
        return this.lastDelayed;
    }

    public long getLastFast() {
        return this.lastFast;
    }

    public long getLastWindowClick() {
        return this.lastWindowClick;
    }

    public PlayerData getLastAttacked() {
        return this.lastAttacked;
    }

    public int getLastAttackedId() {
        return this.lastAttackedId;
    }

    public boolean isReceivedKeepAlive() {
        return this.receivedKeepAlive;
    }

    public boolean isDigging() {
        return this.digging;
    }

    public boolean isSwingDigging() {
        return this.swingDigging;
    }

    public boolean isAbortedDigging() {
        return this.abortedDigging;
    }

    public boolean isStoppedDigging() {
        return this.stoppedDigging;
    }

    public BlockPosition getDiggingBlock() {
        return this.diggingBlock;
    }

    public EnumDirection getDiggingBlockFace() {
        return this.diggingBlockFace;
    }

    public boolean isBanned() {
        return this.banned;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getLastAttackTicks() {
        return this.lastAttackTicks;
    }

    public int getPackets() {
        return this.packets;
    }

    public int getNonMoveTicks() {
        return this.nonMoveTicks;
    }

    public Queue<Alert> getQueuedAlerts() {
        return this.queuedAlerts;
    }

    public Queue<Alert> getSpoofedAlerts() {
        return this.spoofedAlerts;
    }

    public boolean isCheckSpoofing() {
        return this.checkSpoofing;
    }

    public long getLastDelayedMovePacket() {
        return this.lastDelayedMovePacket;
    }
    
    public boolean isSpoofBan() {
        return this.spoofBan;
    }

    public Check getSpoofBanCheck() {
        return this.spoofBanCheck;
    }

    public long getLastVapeString() {
        return this.lastVapeString;
    }

    public Map<Integer, Long> getKeepAliveMap() {
        return this.keepAliveMap;
    }

    public int getPing() {
        return this.ping;
    }

    public int getAveragePing() {
        return this.averagePing;
    }

    public Queue<BiConsumer<Integer, Double>> getPingQueue() {
        return this.pingQueue;
    }

    public Queue<Integer> getConnectionFrequency() {
        return this.connectionFrequency;
    }

    public Queue<Double> getRecentConnectionFrequencies() {
        return this.recentConnectionFrequencies;
    }

    public long getLastPosition() {
        return this.lastPosition;
    }
    
    public CustomBoundingBox getLastPosition2() {
        return this.lastPosition1;
    }

    public int getTeleportTicks() {
        return this.teleportTicks;
    }

    public int getLastTeleportTicks() {
        return this.lastTeleportTicks;
    }

    public int getFlyingTicks() {
        return this.flyingTicks;
    }

    public int getGroundTicks() {
        return this.groundTicks;
    }

    public int getVelocityTicks() {
        return this.velocityTicks;
    }

    public int getVerticalVelocityTicks() {
        return this.verticalVelocityTicks;
    }

    public int getHorizontalVelocityTicks() {
        return this.horizontalVelocityTicks;
    }

    public int getHorizontalSpeedTicks() {
        return this.horizontalSpeedTicks;
    }

    public int getTotalTicks() {
        return this.totalTicks;
    }

    public int getKeepAliveTicks() {
        return this.keepAliveTicks;
    }

    public boolean isInventoryOpen() {
        return this.inventoryOpen;
    }

    public int getLastInventoryTick() {
        return this.lastInventoryTick;
    }

    public int getLastInventoryOutTick() {
        return this.lastInventoryOutTick;
    }

    public int getSteerTicks() {
        return this.steerTicks;
    }

    public int getSuffocationTicks() {
        return this.suffocationTicks;
    }

    public int getLastSetSlot() {
        return this.lastSetSlot;
    }

    public boolean isBlocking() {
        return this.isBlocking;
    }

    public boolean isSpawnedIn() {
        return this.spawnedIn;
    }

    public Boolean getSprinting() {
        return this.sprinting;
    }

    public Boolean getSneaking() {
        return this.sneaking;
    }

    /**public BadPacketsC getKeepAliveCheck() {
        return this.keepAliveCheck;
    }

    public MiscB getPingSpoofCheck() {
        return this.pingSpoofCheck;
    }**/

    public ClientVersion getClientVersion() {
        return this.clientVersion;
    }

    public int getLastFakeEntityTicks() {
        return this.lastFakeEntityTicks;
    }

    public int getLastFakeEntityDamageTicks() {
        return this.lastFakeEntityDamageTicks;
    }

    public StaticFakeEntity getReachEntity() {
        return this.reachEntity;
    }

    public double getLastVelY() {
        return this.lastVelY;
    }

    public double getVelY() {
        return this.velY;
    }

    public double getLastVelX() {
        return this.lastVelX;
    }

    public double getVelX() {
        return this.velX;
    }

    public double getLastVelZ() {
        return this.lastVelZ;
    }

    public double getVelZ() {
        return this.velZ;
    }

    public Queue<CustomBoundingBox> getBoundingBoxes() {
        return this.boundingBoxes;
    }
    
    public PacketDecoder getPacketDecoder() {
        return this.packetDecoder;
    }

    public void setLastMovePacket(final CustomLocation lastMovePacket) {
        this.lastMovePacket = lastMovePacket;
    }
    
    public PacketEncoder getPacketEncoder() {
        return this.packetEncoder;
    }

    public boolean isT2() {
        return this.t2;
    }

    public Integer getSendT2() {
        return this.sendT2;
    }

    public String getBrand() {
        return this.brand;
    }

    public CustomLocation getLastMovePacket() {
        return this.lastMovePacket;
    }
    
    public void setDigging(boolean digging) {
        this.digging = digging;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setCheckSpoofing(boolean checkSpoofing) {
        this.checkSpoofing = checkSpoofing;
    }

    public void setSpoofBan(boolean spoofBan) {
        this.spoofBan = spoofBan;
    }

    public void setSpoofBanCheck(Check spoofBanCheck) {
        this.spoofBanCheck = spoofBanCheck;
    }

    public void setInventoryOpen(boolean inventoryOpen) {
        this.inventoryOpen = inventoryOpen;
    }

    public void setLastFakeEntityTicks(int lastFakeEntityTicks) {
        this.lastFakeEntityTicks = lastFakeEntityTicks;
    }

    public void setLastFakeEntityDamageTicks(int lastFakeEntityDamageTicks) {
        this.lastFakeEntityDamageTicks = lastFakeEntityDamageTicks;
    }

    public void setReachEntity(StaticFakeEntity reachEntity) {
        this.reachEntity = reachEntity;
    }

    public void setLastDelayedMovePacket(final long lastDelayedMovePacket) {
        this.lastDelayedMovePacket = lastDelayedMovePacket;
    }
    
    public void setLastVelY(double lastVelY) {
        this.lastVelY = lastVelY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public void setLastVelX(double lastVelX) {
        this.lastVelX = lastVelX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setLastVelZ(double lastVelZ) {
        this.lastVelZ = lastVelZ;
    }

    public void setVelZ(double velZ) {
        this.velZ = velZ;
    }

    public boolean isAlerts() {
        return this.alerts;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public boolean isBypass() {
        return this.bypass;
    }

    public void setAlerts(boolean alerts) {
        this.alerts = alerts;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }

    public void setPacketDecoder(PacketDecoder packetDecoder) {
        this.packetDecoder = packetDecoder;
    }

    public void setPacketEncoder(PacketEncoder packetEncoder) {
        this.packetEncoder = packetEncoder;
    }

    public GolemEntity getGolemEntity() {
        return this.golemEntity;
    }

    public void setGolemEntity(GolemEntity golemEntity) {
        this.golemEntity = golemEntity;
    }

    public void setCps(final int cps) {
        this.cps = cps;
    }
    
    public int getCps() {
        return this.cps;
    }
    
    public void setLastPosition(final CustomBoundingBox lastPosition1) {
        this.lastPosition1 = lastPosition1;
    }
    
    public void setT2(boolean t2) {
        this.t2 = t2;
    }

    public void setSendT2(Integer sendT2) {
        this.sendT2 = sendT2;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    static EntityPlayer access$200(PlayerData x0) {
        return x0.entityPlayer;
    }

    public long getLastAttackPacket() {
        return this.lastAttackPacket;
    }
    
    public void setLastAttackPacket(final long lastAttackPacket) {
        this.lastAttackPacket = lastAttackPacket;
    }
    
    public double getVelocityY() {
        return this.velocityY;
    }
    
    public void setVelocityY(final double velocityY) {
        this.velocityY = velocityY;
    }
    
    public boolean isUnderBlock() {
        return this.underBlock;
    }
    
    public boolean isInLiquid() {
        return this.inLiquid;
    }
    
    public long getLastVelocity() {
        return this.lastVelocity;
    }
}