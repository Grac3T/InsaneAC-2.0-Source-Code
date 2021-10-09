package fr.whiizyyy.insaneac.util;

public class TimerUtils {

	private long lastMS = 0L;

	public static boolean elapsed(final long from, final long required) {
		return System.currentTimeMillis() - from > required;
	}

	public static long nowlong() {
		return System.currentTimeMillis();
	}

	public static long elapsed(final long time) {
		return Math.abs(System.currentTimeMillis() - time);
	}
	public static long CurrentMS() {
		return System.currentTimeMillis();
	}
	public boolean isDelayComplete(long delay) {
		if (System.currentTimeMillis() - this.lastMS >= delay) {
			return true;
		}
		return false;
	}

	public boolean hasReached(long milliseconds) {
		return getCurrentMS() - this.lastMS >= milliseconds;
	}

	public void setLastMS() {
		this.lastMS = System.currentTimeMillis();
	}

	public int convertToMS(int perSecond) {
		return 1000 / perSecond;
	}

	public static boolean Passed(long from, long required) {
		return System.currentTimeMillis() - from > required;
	}

	public long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public long getLastMS() {
		return this.lastMS;
	}

	public void setLastMS(long lastMS) {
		this.lastMS = lastMS;
	}

	public void reset() {
		this.lastMS = getCurrentMS();
	}
}
