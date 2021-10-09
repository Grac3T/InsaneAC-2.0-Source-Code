package fr.whiizyyy.insaneac.check.speed;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.MovementCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.data.PlayerLocation;
import fr.whiizyyy.insaneac.manager.AlertsManager;

public class SpeedB extends MovementCheck {
    private int lastLadderCheck = 0;
    private boolean ladder = false;
    private int threshold = 0;

    public SpeedB() {
        super(Check.CheckType.SPEEDB, "B", "Speed", Check.CheckVersion.RELEASE);
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.Speed B.Alerts"));
    }

    @Override
    public void handle(Player player, PlayerData playerData, PlayerLocation from, PlayerLocation to, long timestamp) {
        if (!playerData.hasLag() && to.getY() > from.getY() && !to.getOnGround().booleanValue() && !from.getOnGround().booleanValue()) {
            if (this.ladder) {
                double yDiff = to.getY() - from.getY();
                if (yDiff > 0.118) {
                    if (this.threshold++ > 2) {
                        this.threshold = 0;
                        PlayerLocation location = to.clone();
                        World world = player.getWorld();
                        this.run(() -> {
                            Block block = world.getBlockAt((int)Math.floor(location.getX()), (int)Math.floor(location.getY() + 1.0), (int)Math.floor(location.getZ()));
                            Material type = block.getType();
                            if (type != Material.VINE && type != Material.LADDER) {
                                this.ladder = false;
                            } else {
                                AlertsManager.getInstance().handleViolation(playerData, this, String.valueOf(yDiff));
                            }
                        });
                    }
                } else {
                    this.threshold = 0;
                }
            } else if (this.lastLadderCheck++ > 9) {
                this.lastLadderCheck = 0;
                PlayerLocation location = to.clone();
                World world = player.getWorld();
                this.run(() -> {
                    Block block = world.getBlockAt((int)Math.floor(location.getX()), (int)Math.floor(location.getY() + 1.0), (int)Math.floor(location.getZ()));
                    Material type = block.getType();
                    if (type == Material.VINE || type == Material.LADDER) {
                        this.ladder = true;
                    }
                });
            }
        } else {
            this.ladder = false;
        }
    }
}

