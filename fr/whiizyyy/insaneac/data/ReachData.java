package fr.whiizyyy.insaneac.data;

public class ReachData {
    private final PlayerData playerData;
    private final PlayerLocation location;
    private final DistanceData distanceData;
    private final double movement;
    private final double horizontal;
    private final double extra;
    private final double vertical;
    private final double reach;

    public ReachData(PlayerData playerData, PlayerLocation location, DistanceData distanceData, double movement, double horizontal, double extra, double vertical, double reach) {
        this.playerData = playerData;
        this.location = location;
        this.distanceData = distanceData;
        this.movement = movement;
        this.horizontal = horizontal;
        this.extra = extra;
        this.vertical = vertical;
        this.reach = reach;
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public PlayerLocation getLocation() {
        return this.location;
    }

    public DistanceData getDistanceData() {
        return this.distanceData;
    }

    public double getMovement() {
        return this.movement;
    }

    public double getHorizontal() {
        return this.horizontal;
    }

    public double getExtra() {
        return this.extra;
    }

    public double getVertical() {
        return this.vertical;
    }

    public double getReach() {
        return this.reach;
    }
}

