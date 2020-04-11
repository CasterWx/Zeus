package com.antzuhl.zeus.core.util;

public class SleepUtil {

	private SleepUtil() {
	}

	public static void sleep(long timeout) {
		try {
			Thread.sleep(timeout);
		} catch (Throwable t) {
		}
	}
}