package com.bookstore;

import static com.bookstore.utilities.Print.title;
import static com.bookstore.utilities.Printable.printTable;

import java.util.List;
import java.util.Scanner;

import com.bookstore.configs.DBConnection;
import com.bookstore.screens.AdminScreen;
import com.bookstore.services.AdminService;
import com.bookstore.services.BookService;
import com.bookstore.services.UserService;
import com.bookstore.utilities.Printable;

public class AdminPannel {

	private static AdminService adminService;
	private static BookService bookService;
	private static UserService userService;
	private static Scanner sc;

	static {
		adminService = new AdminService();
		bookService = new BookService();
		userService = new UserService();
		sc = new Scanner(System.in);
	}

	public static void main(String[] args) throws Exception {
		List<Printable> list;
		int attempts = 3;

		title(" Welcome to the Admin Pannel ");

		while (attempts-- > 0) {
			System.out.print("Enter admin password: ");
			String password = sc.nextLine();
			if (password.hashCode() == 3506402) {
				System.out.println("\nSuccessfully logged-in...!\n");
				break;
			}
			System.out.println("\nWrong password...!\nOnly " + attempts + " attempts left.\n");
		}

		if (attempts < 0) {
			System.out.println("\nYou've exausted all login attempts with wrong password.");
			System.out.println("Try again next time with the correct admin password.");
		}

		while (attempts >= 0) {
			switch (AdminScreen.showAfterLoginScreen(sc)) {
			case 'A':
				list = adminService.fetchAllBookDetails();
				if (!list.isEmpty()) {
					title(" Showing the details of all " + list.size() + " books ");
					printTable(list, "Id|Book Name|Author|Genre|Price|Avg Rating|Recommended by");
				}
				break;
			case 'B':
				AdminScreen.showAddBookScreen(sc, adminService);
				break;
			case 'C':
				AdminScreen.showUpdateBookScreen(sc, adminService, bookService);
				break;
			case 'D':
				AdminScreen.showDeleteBookScreen(sc, adminService, bookService);
				break;
			case 'E':
				list = adminService.fetchTop5MostRatedBooks();
				if (!list.isEmpty()) {
					title(" Top " + list.size() + " most rated books ");
					printTable(list, "Id|Book Name|Author|Genre|Price|Avg Rating");
				}
				break;
			case 'Q':
				list = adminService.fetchTop5MostRequestedBooks();
				if (!list.isEmpty()) {
					title(" Top " + list.size() + " most requested books ");
					printTable(list, "Book Name|Author Name|Requested by");
				}
				break;
			case 'F':
				list = adminService.fetchTop5MostRecommendedBooks();
				if (!list.isEmpty()) {
					title(" Top " + list.size() + " most recommended books ");
					printTable(list, "Id|Book Name|Author|Genre|Price|Recommended by");
				}
				break;
			case 'G':
				list = adminService.fetchAllUserDetails();
				if (!list.isEmpty()) {
					title(" Showing the details of all " + list.size() + " users ");
					printTable(list, "Id|Username|Email Id|Phone No.|Status");
				}
				break;
			case 'H':
				AdminScreen.showBlockUnblockScreen(sc, adminService, userService);
				break;
			case 'X':
				attempts = -1;
			}
		}

		System.out.println("\nBye bye Mr. Admin!!\nHave a nice day...\n");
		DBConnection.closeConnection();
		sc.close();
	}

}
