package com.bookstore.utilities;

public interface Print {

	public static String center(String msg, int len) {
		if (msg.length() >= len) {
			return msg;
		}

		int buffer = len - msg.length();
		int left = buffer / 2;
		int right = buffer - left;

		return "-".repeat(left) + msg + "-".repeat(right);
	}

	public static void title(String msg) {
		String str = "\n" + center(msg, 50) + "\n";
		System.out.println(str);
	}
}
