package fr.whiizyyy.insaneac.manager;

import fr.whiizyyy.insaneac.gui.impl.CheckGui;
import fr.whiizyyy.insaneac.gui.impl.MainGui;
import fr.whiizyyy.insaneac.gui.impl.MovementGui;
import fr.whiizyyy.insaneac.gui.impl.OtherGui;

public class GuiManager {
    private static GuiManager instance;
    private MainGui mainGui;
    private CheckGui checkGui;
    private MovementGui mouvementGui;
    private OtherGui otherGui;

    public void enable() {
        this.mainGui = new MainGui();
        this.checkGui = new CheckGui();
        this.mouvementGui = new MovementGui();
        this.otherGui = new OtherGui();
    }

    public static GuiManager getInstance() {
        GuiManager guiManager = instance == null ? (instance = new GuiManager()) : instance;
        return guiManager;
    }

    public MainGui getMainGui() {
        return this.mainGui;
    }

    public CheckGui getCheckGui() {
        return this.checkGui;
    }
    
    public MovementGui getMovGui() {
        return this.mouvementGui;
    }
    
    public OtherGui getOtherGui() {
        return this.otherGui;
    }

	public void disable() {
		this.mainGui = null;
		this.checkGui = null;
		this.mouvementGui = null;
		this.otherGui = null;
		instance = null;
		
		
	}
}

