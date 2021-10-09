package fr.whiizyyy.insaneac.type;

import java.util.ArrayList;


import java.util.List;
import fr.whiizyyy.insaneac.check.Check;
import fr.whiizyyy.insaneac.check.aimassist.*;
import fr.whiizyyy.insaneac.check.autoclicker.*;
import fr.whiizyyy.insaneac.check.fly.*;
import fr.whiizyyy.insaneac.check.killaura.*;
import fr.whiizyyy.insaneac.check.reach.*;
import fr.whiizyyy.insaneac.check.speed.*;
import fr.whiizyyy.insaneac.check.timer.*;
import fr.whiizyyy.insaneac.check.velocity.*;

public class LoaderCheck {
    private List<Class<? extends Check>> checkClasses = null;

    private List<Class<? extends Check>> getCheckClasses() {
        if (this.checkClasses == null) {
            this.checkClasses = new ArrayList<Class<? extends Check>>();
            this.checkClasses.add(AimA.class);
            this.checkClasses.add(AimB.class);
            this.checkClasses.add(AimC.class);
            this.checkClasses.add(AimD.class);
            this.checkClasses.add(AimE.class);
            this.checkClasses.add(AimF.class);
            this.checkClasses.add(VelocityA.class);
            this.checkClasses.add(VelocityB.class);
            this.checkClasses.add(VelocityC.class);
            this.checkClasses.add(ReachA.class);
            this.checkClasses.add(ReachB.class);
            this.checkClasses.add(ReachC.class);
            this.checkClasses.add(ReachD.class);
            this.checkClasses.add(ReachE.class);
            this.checkClasses.add(ReachF.class);
            this.checkClasses.add(FlyA.class);
            this.checkClasses.add(FlyB.class);
            this.checkClasses.add(FlyC.class);
            this.checkClasses.add(FlyD.class);
            this.checkClasses.add(FlyE.class);
            this.checkClasses.add(FlyF.class);
            this.checkClasses.add(TimerA.class);
            this.checkClasses.add(TimerB.class);
            this.checkClasses.add(SpeedA.class);
            this.checkClasses.add(SpeedB.class);
            this.checkClasses.add(SpeedC.class);
            this.checkClasses.add(SpeedD.class);
            this.checkClasses.add(SpeedE.class);
            this.checkClasses.add(AutoClickerA.class);
            this.checkClasses.add(AutoClickerB.class);
            this.checkClasses.add(AutoClickerA.class);
            this.checkClasses.add(AutoClickerD.class);
            this.checkClasses.add(AutoClickerE.class);
            this.checkClasses.add(AutoClickerF.class);
            this.checkClasses.add(AutoClickerG.class);
            this.checkClasses.add(AutoClickerH.class);
            this.checkClasses.add(AutoClickerI.class);
            this.checkClasses.add(AutoClickerJ.class);
            this.checkClasses.add(KillAuraA.class);
            this.checkClasses.add(KillAuraB.class);
            this.checkClasses.add(KillAuraA.class);
            this.checkClasses.add(KillAuraD.class);
            this.checkClasses.add(KillAuraE.class);
            this.checkClasses.add(KillAuraF.class);
            this.checkClasses.add(KillAuraG.class);
            this.checkClasses.add(KillAuraH.class);
            this.checkClasses.add(KillAuraI.class);
            this.checkClasses.add(KillAuraJ.class);
            this.checkClasses.add(KillAuraK.class);
            this.checkClasses.add(KillAuraL.class);
            this.checkClasses.add(KillAuraM.class);
            this.checkClasses.add(KillAuraN.class);
            this.checkClasses.add(KillAuraO.class);
            
        }
        return this.checkClasses;
    }

    public List<Check> loadChecks() {
        ArrayList<Check> checks = new ArrayList<Check>();
        for (Class<? extends Check> clazz : this.getCheckClasses()) {
            try {
                checks.add(clazz.newInstance());
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return checks;
    }

    public void setCheckClasses(List<Class<? extends Check>> checkClasses) {
        this.checkClasses = checkClasses;
    }
}