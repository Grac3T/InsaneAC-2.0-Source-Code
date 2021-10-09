package fr.whiizyyy.insaneac.check.alert;

import fr.whiizyyy.insaneac.check.Check;

public class Alert {
	
	public static Alert instance;
    private final Check check;
    private final String data;
    private final int vl;
    private final long timestamp = System.currentTimeMillis();

    public Alert(Check check, String data, int vl) {
        this.check = check;
        this.data = data;
        this.vl = vl;
    }

    public Check getCheck() {
        return this.check;
    }

    public String getData() {
        return this.data;
    }

    public int getVl() {
        return this.vl;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}

