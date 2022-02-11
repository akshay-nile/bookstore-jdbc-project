package com.bookstore.utilities;

import java.util.Scanner;

public interface Scan {

	public static char input(Scanner sc, String valid, String msg) {
		char choice = '\0';
		while (true) {
			try {
				System.out.print(msg == null ? "\nEnter the index of desired option: " : msg);
				choice = sc.nextLine().toUpperCase().charAt(0);
				if (valid.contains(choice + "")) {
					break;
				}
				System.out.println("Invalid index...!");
			} catch (StringIndexOutOfBoundsException e) {
				break;
			}
		}
		return choice;
	}
}
