package com.hhd.multisocket514.utils;

import android.os.Handler;

public class HandlerManager {
	private static ThreadLocal<Handler> threadLocal = new ThreadLocal<Handler>();

	public static Handler getHandler() {
		return threadLocal.get();
	}

	public static void putHandler(Handler value) {
		threadLocal.set(value);//UiThread  id
	}
}
