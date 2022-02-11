package com.bookstore.utilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface Printable {

	public static final Map<String, String> row = new HashMap<>();
	public static final int LEFT_MARGIN = 1;
	public static final int TOP_MARGIN = 0;
	public static final int BOTTOM_MARGIN = 0;

	public void loadCellValues(Map<String, String> map);

	public static void printTable(Collection<? extends Printable> data, String columnsToPrint) {
		String[] headers = columnsToPrint.split("(\\s+)?\\|(\\s+)?");

		Map<String, Integer> widths = new HashMap<>();
		Arrays.stream(headers).forEach(head -> widths.put(head, head.length()));

		if (!data.isEmpty()) {
			for (Printable obj : data) {
				obj.loadCellValues(row);
				for (String key : headers) {
					int oldValue = widths.get(key);
					int newValue = row.get(key).length();
					widths.put(key, Math.max(oldValue, newValue));
				}
			}
		}

		String formatter = " ".repeat(LEFT_MARGIN);
		String saperator = formatter;
		for (String key : headers) {
			formatter += "| %-" + widths.get(key) + "s ";
			saperator += "+-" + "-".repeat(widths.get(key)) + "-";
		}
		formatter += "|";
		saperator += "+";

		System.out.println("\n".repeat(TOP_MARGIN));
		System.out.println(saperator);
		System.out.format(formatter + "\n", (Object[]) headers);
		System.out.println(saperator);
		for (Printable obj : data) {
			obj.loadCellValues(row);
			System.out.format(formatter + "\n", Arrays.stream(headers).map(key -> row.get(key)).toArray());
		}
		System.out.println(saperator);
		System.out.println("\n".repeat(BOTTOM_MARGIN));
	}
}
