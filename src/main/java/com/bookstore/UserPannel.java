package com.bookstore;

import static com.bookstore.utilities.Print.title;
import static com.bookstore.utilities.Printable.printTable;

import java.util.List;
import java.util.Scanner;

import com.bookstore.configs.DBConnection;
import com.bookstore.models.User;
import com.bookstore.screens.UserScreen;
import com.bookstore.services.BookService;
import com.bookstore.services.UserService;
import com.bookstore.utilities.Printable;

public class UserPannel {

	private static UserService userService;
	private static BookService bookService;
	private static Scanner sc;
	private static User user;

	static {
		userService = new UserService();
		bookService = new BookService();
		sc = new Scanner(System.in);
	}

	private static void exit() {
		System.out.println("\nGood Bye !!\n");
		DBConnection.closeConnection();
		sc.close();
		System.exit(0);
	}

	public static void main(String[] args) throws Exception {
		while (true) {
			switch (UserScreen.showWelcomeScreen(sc)) {
			case 'G':
				afterGuestLogin();
				break;
			case 'R':
				UserScreen.showRegistrationScreen(sc, userService);
				break;
			case 'O':
				user = UserScreen.showLoginScreen(sc, userService);
				if (user != null) {
					afterLogin();
				}
				break;
			case 'X':
				exit();
			}
		}
	}

	private static void afterLogin() throws Exception {
		List<? extends Printable> list;

		while (user != null) {
			switch (UserScreen.showAfterLoginScreen(sc, false)) {
			case 'A':
				list = bookService.fetchAllBooks();
				if (!list.isEmpty()) {
					title(" Showing all of the " + list.size() + " books ");
					printTable(list, "Id|Name|Author|Genre|Price");
				}
				break;
			case 'B':
				list = bookService.fetchAllRecommendedBooks();
				if (!list.isEmpty()) {
					title(" Showing all the " + list.size() + " recommended books ");
					printTable(list, "Id|Name|Author|Genre|Price");
				}
				break;
			case 'C':
				double r = userService.getCumulativeRatings();
				if (r > 0) {
					System.out.format("\nCumulative ratings of all the books is %.2f stars\n\n", r);
				}
				break;
			case 'E':
				UserScreen.showGetRatingsScreen(sc, bookService, user);
				break;
			case 'Q':
				UserScreen.showBookRequestScreen(sc, userService, user);
				break;
			case 'F':
				UserScreen.showBookRecommendScreen(sc, bookService, user);
				break;
			case 'G':
				list = bookService.fetchRatedBooks(user);
				if (!list.isEmpty()) {
					title(" Books rated by " + user.getUsername() + " ");
					printTable(list, "Id|Name|Author|Genre|Price");
				}
				break;
			case 'R':
				list = userService.fetchRequestedBooks(user);
				if (!list.isEmpty()) {
					title(" Books requested by " + user.getUsername() + " ");
					printTable(list, "Id|Book Name|Author Name");
				}
				break;
			case 'H':
				list = bookService.fetchRecommendedBooks(user);
				if (!list.isEmpty()) {
					title(" Books recommended by " + user.getUsername() + " ");
					printTable(list, "Id|Name|Author|Genre|Price");
				}
				break;
			case 'I':
				list = bookService.getRatingsGivenByUser(user);
				if (!list.isEmpty()) {
					title(" Ratings given by " + user.getUsername() + " ");
					printTable(list, "Id|Book Name|Ratings");
				}
				break;
			case 'P':
				if (UserScreen.showChangePasswordScreen(sc, userService, user)) {
					user = null;
				}
				break;
			case 'D':
				if (UserScreen.showDeleteAccountScreen(sc, userService, user)) {
					user = null;
				}
				break;
			case 'O':
				user = null;
				System.out.println("\nSuccessfully logged-out.\n");
				break;
			case 'X':
				System.out.print("\nYou've been logged-out.");
				exit();
			}
		}
	}

	private static void afterGuestLogin() {
		System.out.println("\nYou've logged-in as guest.");
		System.out.println("You can't use 'User Specific Options' until you register with us.\n");

		List<? extends Printable> list;
		boolean isGuestLoggedIn = true;

		while (isGuestLoggedIn) {
			switch (UserScreen.showAfterLoginScreen(sc, true)) {
			case 'A':
				list = bookService.fetchAllBooks();
				if (!list.isEmpty()) {
					title(" Showing all of the " + list.size() + " books ");
					printTable(list, "Id|Name|Author|Genre|Price");
				}
				break;
			case 'B':
				list = bookService.fetchAllRecommendedBooks();
				if (!list.isEmpty()) {
					title(" Showing all the " + list.size() + " recommended books ");
					printTable(list, "Id|Name|Author|Genre|Price");
				}
				break;
			case 'C':
				double r = userService.getCumulativeRatings();
				if (r > 0) {
					System.out.format("\nCumulative ratings of all the books is %.2f stars\n\n", r);
				}
				break;
			case 'O':
				isGuestLoggedIn = false;
				System.out.println("\nSuccessfully logged-out as guest.\n");
				break;
			case 'X':
				System.out.print("\nYou've been logged-out as guest.");
				exit();
			}
		}
	}

}
