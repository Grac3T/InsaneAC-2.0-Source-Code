 package fr.whiizyyy.insaneac.check.autoclicker;

import java.util.ArrayList;

import java.util.List;

import org.bukkit.entity.Player;

import fr.whiizyyy.insaneac.InsaneAC;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.PacketCheck;
import fr.whiizyyy.insaneac.data.PlayerData;
import fr.whiizyyy.insaneac.manager.AlertsManager;
import net.minecraft.server.v1_8_R3.*;

public class AutoClickerB
        extends PacketCheck {
	  private long lastflying;
	    private int ticks;
	    private int vls;
	    private List<Integer> last;
	    
    public AutoClickerB() {
        super(Check.CheckType.AUTO_CLICKERB, "B", "AutoClicker", Check.CheckVersion.RELEASE);
        last = new ArrayList<>();
        this.setMaxViolation(InsaneAC.getInstance().getAlerts().getInt("CheckTypes.AutoClicker B.Alerts"));
        this.violations = -1.0;
    }

    @SuppressWarnings({ "rawtypes", "unused" })
	@Override
    public void handle(final Player player, final PlayerData playerData, final Packet packet, final long timestamp) {
    	if(packet instanceof PacketPlayInArmAnimation) {
            long delta = System.currentTimeMillis() - lastflying;
            last.add(ticks);
            if(last.size() == 4) {
                if(last.get(0) == last.get(3) && last.get(1) == last.get(2) && last.get(0) != last.get(1) && last.get(2) != last.get(3)
                        || last.get(0) == last.get(2) && last.get(1) == last.get(3) && last.get(0) != last.get(1) && last.get(2) != last.get(3)) {
                    if(vls++ > 20) {
                        AlertsManager.getInstance().handleViolation(playerData, this, "");
                    }
                } else {
                    vls -= vls > 0 ? 1 : 0;
                }
                last.clear();
            }
            ticks = 0;
        }
        if(packet instanceof PacketPlayInFlying) {
            ticks++;
        }
    }
}

