package com.bookstore.screens;

import java.util.Arrays;
import java.util.Scanner;

import com.bookstore.models.Book;
import com.bookstore.models.User;
import com.bookstore.services.BookService;
import com.bookstore.services.UserService;
import com.bookstore.utilities.Print;
import com.bookstore.utilities.Printable;
import com.bookstore.utilities.Scan;

public interface UserScreen {

	public static char showWelcomeScreen(Scanner sc) {
		char choice;

		Print.title(" Welcome to our Book Store App ");
		System.out.println("\t[G] Login as guest");
		System.out.println("\t[R] Register");
		System.out.println("\t[O] Login");
		System.out.println("\t[X] Exit");

		while ((choice = Scan.input(sc, "GROX", null)) == '\0') {
			System.out.println("Please provide some valid response.");
		}

		return choice;
	}

	public static void showRegistrationScreen(Scanner sc, UserService userService) {
		Print.title(" Register a new User ");

		String value;
		String[] messages = { "Choose a unique Username: ", "Choose a strong Password: ", "Enter your Email Id: ",
				"Enter your Phone No: " };
		String[] fields = { "username", "password", "email", "phone" };

		for (int i = 0; i < 4; i++) {
			System.out.print(messages[i]);
			value = sc.nextLine().replace(" ", "");

			if (value.isEmpty()) {
				System.out.println("\nRegistration attempt cancelled...!\n");
				return;
			}

			if (i != 1 && userService.fieldExists(fields[i], value)) {
				System.out.println("\nThis " + fields[i] + " is already taken by another user.");
				System.out.println("Please try a different one.\n");
				i--;
				continue;
			}

			fields[i] = value;
		}

		User user = new User(fields[0], fields[1], fields[2], fields[3]);
		if (userService.insertUser(user)) {
			System.out.println("\nSuccessfully registered...!\nYou can now try login.\n");
		} else {
			System.out.println("Registration failed...!\n");
		}
	}

	public static User showLoginScreen(Scanner sc, UserService userService) {
		Print.title(" Login to an existing account ");

		System.out.print("Enter your Username: ");
		String username = sc.nextLine().replace(" ", "");
		if (username.isBlank()) {
			System.out.println("\nLogin attempt cancelled...!\n");
			return null;
		}

		System.out.print("Enter your Password: ");
		String password = sc.nextLine().replace(" ", "");
		if (password.isBlank()) {
			System.out.println("\nLogin attempt cancelled...!\n");
			return null;
		}

		User user = userService.fetchUser(username, password);
		if (user != null) {
			if (userService.isUserBlocked(user)) {
				System.out.println("\nYour account is blocked...!\nPlease contact the Admin to unblock it.\n");
				user = null;
			} else {
				System.out.println("\nSuccessfully logged-in...!\n");
			}
		} else {
			System.out.println("\nIncorrect username or password...!\n");
		}
		return user;
	}

	public static char showAfterLoginScreen(Scanner sc, boolean isGuest) {
		Print.title(" What would you like to do? ");

		System.out.println("   General Options:\n");
		System.out.println("\t[A] Show all Books");
		System.out.println("\t[B] Show all the recommended Books");
		System.out.println("\t[C] Show cumulative rating of all the Books");

		if (!isGuest) {
			System.out.println("\n   User Specific Options:\n");
			System.out.println("\t[E] Rate a Book");
			System.out.println("\t[Q] Request a Book");
			System.out.println("\t[F] Recommend a Book");
			System.out.println("\t[G] Show all Books rated by you");
			System.out.println("\t[R] Show all Books requested by you");
			System.out.println("\t[H] Show all Books recommended by you");
			System.out.println("\t[I] Show ratings of the Books you rated");
		}

		System.out.println("\n   Profile Options:\n");
		if (!isGuest) {
			System.out.println("\t[P] Change password");
			System.out.println("\t[D] Delete account");
		}
		System.out.println("\t[O] Logout");
		System.out.println("\t[X] Exit");

		return Scan.input(sc, !isGuest ? "ABCDEFGHIQROPX" : "ABCOX", null);
	}

	public static void showBookRecommendScreen(Scanner sc, BookService bookService, User user) {
		Print.title(" Recommending a Book ");
		System.out.print("Enter Id of the Book you wish to recommend: ");
		String val = sc.nextLine();
		int bookId = Integer.parseInt(val.matches("\\d+") ? val : "0");
		Book book = bookService.fetchBookById(bookId);
		if (book == null) {
			System.out.println("\nInvalid Book Id: " + val + "\nNo such Book exists.\n");
			return;
		}
		System.out.println();
		Printable.printTable(Arrays.asList(book), "Id|Name|Author|Genre|Price");
		if (bookService.recommendBook(book, user)) {
			System.out.println("\nYou just recommended the book shown above.\n");
		} else {
			System.out.println("\nYou've already recommended this book.\n");
		}
	}

	public static void showGetRatingsScreen(Scanner sc, BookService bookService, User user) {
		Print.title(" Rating a Book ");
		System.out.print("Enter Id of the Book you want to rate: ");
		String val = sc.nextLine();
		int bookId = Integer.parseInt(val.matches("\\d+") ? val : "0");
		Book book = bookService.fetchBookById(bookId);
		if (book == null) {
			System.out.println("\nInvalid Book Id: " + val + "\nNo such Book exists.\n");
			return;
		}
		int rating;
		while (true) {
			System.out.print("Enter the rating you want to give: ");
			try {
				rating = Integer.parseInt(sc.nextLine());
				if (rating >= 1 && rating <= 5) {
					break;
				}
				System.out.println("\nInvalid rating: " + rating + "\nRating should be between 1 and 5 stars.\n");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input...!\n");
			}
		}
		bookService.rateBook(bookId, rating, user);
	}

	public static boolean showDeleteAccountScreen(Scanner sc, UserService userService, User user) {
		Print.title(" Deleting your account ");

		System.out.print("Confirm your password: ");
		String password = sc.nextLine();

		if (password.equals(user.getPassword())) {
			System.out.print("Do you really want to delete your account?  Yes/No:\t");
			if (sc.nextLine().equalsIgnoreCase("yes")) {
				if (userService.deleteUserAccount(user)) {
					System.out.println("\nSuccessfully deleted your account...!\n");
					return true;
				} else {
					System.out.println("\nSomething went wrong...!\nWe couldn't delete your account.\n");
				}
			} else {
				System.out.println("\nYou cancelled the delete operation...!\n");
			}
		} else {
			System.out.println("\nIncorrect password...!\nYou've been logged-out as a security measure.\n");
			return true;
		}
		return false;
	}

	public static boolean showChangePasswordScreen(Scanner sc, UserService userService, User user) {
		Print.title(" Changing your account password ");

		System.out.print("Confirm your current password: ");
		String password = sc.nextLine();

		if (password.equals(user.getPassword())) {
			while (true) {
				System.out.print("Choose your new password: ");
				password = sc.nextLine();
				if (!password.isBlank() && !password.contains(" ")) {
					user.setPassword(password);
					break;
				}
				System.out.println("\nThis password is not acceptable.\nPlease choose a different one.\n");
			}
			if (userService.updateUserPassword(user)) {
				System.out.println("\nSuccessfully changed your password...!");
				System.out.println("Please login again with your new password\n");
				return true;
			} else {
				System.out.println("\nSomething went wrong...!\nWe couldn't update your password.\n");
			}
		} else {
			System.out.println("\nIncorrect password...!\nYou've been logged-out as a security measure.\n");
			return true;
		}
		return false;
	}

	public static void showBookRequestScreen(Scanner sc, UserService userService, User user) {
		Print.title(" Placing a book request ");

		System.out.print("Enter the book name: ");
		String bookName = sc.nextLine();
		if (bookName.isBlank()) {
			System.out.println("\nRequest attempt cancelled...!\n");
			return;
		}

		System.out.print("Enter the author name: ");
		String authorName = sc.nextLine();
		if (authorName.isBlank()) {
			System.out.println("\nRequest attempt cancelled...!\n");
			return;
		}

		if (userService.addBookRequest(user, bookName, authorName)) {
			System.out.println("\nSuccessfully placed your book request...!\n");
		}
	}

}
