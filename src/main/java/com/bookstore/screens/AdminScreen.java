package com.bookstore.screens;

import java.util.Scanner;

import com.bookstore.models.Book;
import com.bookstore.models.Genre;
import com.bookstore.models.User;
import com.bookstore.services.AdminService;
import com.bookstore.services.BookService;
import com.bookstore.services.UserService;
import com.bookstore.utilities.Print;
import com.bookstore.utilities.Scan;

public interface AdminScreen {

	public static char showAfterLoginScreen(Scanner sc) {
		Print.title(" What would you like to do? ");

		System.out.println("   Book CRUD Options:\n");
		System.out.println("\t[A] Show all the books");
		System.out.println("\t[B] Add a new book");
		System.out.println("\t[C] Update a book");
		System.out.println("\t[D] Delete a book");

		System.out.println("\n   Book Report Options:\n");
		System.out.println("\t[E] Show most rated books");
		System.out.println("\t[Q] Show most requested books");
		System.out.println("\t[F] Show most recommended books");

		System.out.println("\n   User Specific Options:\n");
		System.out.println("\t[G] Show all the users");
		System.out.println("\t[H] Block or Unblock a user");

		System.out.println("\n   Other Options:\n");
		System.out.println("\t[X] Exit");

		return Scan.input(sc, "ABCDEFGHQX", null);
	}

	public static Genre showGenreSelectionScreen(Scanner sc) {
		int choice;
		System.out.println("Select the Genre of the book:\n");
		Genre[] genres = Genre.values();
		for (int i = 0; i < genres.length; i++) {
			System.out.format("  [%d] %s\n", i + 1, genres[i]);
		}
		while (true) {
			System.out.print("\nEnter the index of Genre here: ");
			try {
				choice = Integer.parseInt(sc.nextLine());
				if (choice >= 1 && choice <= genres.length) {
					break;
				}
				System.out.println("Invalid index...!");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input...!");
			}
		}
		return genres[choice - 1];
	}

	private static Book getBookDetailsFromUser(Scanner sc, Book oldBook) {
		System.out.println();
		if(oldBook != null) {
			System.out.println("Current book name is \"" + oldBook.getBookName() + "\"");
		}
		System.out.print("Enter the book name: ");
		String bookName = sc.nextLine();
		if (bookName.isBlank()) {
			return null;
		}
		
		if(oldBook != null) {
			System.out.println("\nCurrent author name is \"" + oldBook.getAuthor() + "\"");
		}
		System.out.print("Enter the author name: ");
		String author = sc.nextLine();
		if (author.isBlank()) {
			return null;
		}

		if(oldBook != null) {
			System.out.println("\nCurrent book genre is \"" + oldBook.getGenre() + "\"");
		}
		Genre genre = showGenreSelectionScreen(sc);

		if(oldBook != null) {
			System.out.format("\nCurrent book price is ₹ %.2f\n",oldBook.getPrice());
		}
		System.out.print("Enter the book price: ");
		Double price;
		try {
			price = Double.parseDouble(sc.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("\nInvalid input...!\nWe can't accept this book price.");
			return null;
		}
		
		return new Book(-1, bookName, author, genre, price);
	}

	public static void showAddBookScreen(Scanner sc, AdminService adminService) {
		Print.title(" Adding a new book ");
		Book book = getBookDetailsFromUser(sc, null);
		if (book == null) {
			System.out.println("\nYou cancelled the attempt...!\n");
			return;
		}
		adminService.addNewBook(book);
	}

	public static void showUpdateBookScreen(Scanner sc, AdminService adminService, BookService bookService) {
		Print.title(" Updating an existing book ");

		System.out.print("Enter Id of the book you want to update: ");
		String val = sc.nextLine();
		int bookId = Integer.parseInt(val.matches("\\d+") ? val : "0");
		Book book = bookService.fetchBookById(bookId);
		if (book == null) {
			System.out.println("\nInvalid Book Id: " + val + "\nNo such Book exists.\n");
			return;
		}

		book = getBookDetailsFromUser(sc, book);
		if (book == null) {
			System.out.println("\nYou cancelled the attempt...!\n");
			return;
		}
		
		book.setBookId(bookId);
		adminService.updateExistingBook(book);
	}

	public static void showDeleteBookScreen(Scanner sc, AdminService adminService, BookService bookService) {
		Print.title(" Deleting a book ");

		System.out.print("Enter Id of the book you want to delete: ");
		String val = sc.nextLine();
		int bookId = Integer.parseInt(val.matches("\\d+") ? val : "0");
		Book book = bookService.fetchBookById(bookId);
		if (book == null) {
			System.out.println("\nInvalid Book Id: " + val + "\nNo such Book exists.\n");
			return;
		}

		adminService.deleteBook(book);
	}

	public static void showBlockUnblockScreen(Scanner sc, AdminService adminService, UserService userService) {
		Print.title(" Blocking or Unblocking a user ");

		System.out.print("Enter Id of the user here: ");
		String val = sc.nextLine();
		int userId = Integer.parseInt(val.matches("\\d+") ? val : "0");
		User user = userService.fetchUserById(userId);
		if (user == null) {
			System.out.println("\nInvalid User Id: " + userId + "\nNo such user exists.\n");
			return;
		}

		boolean isActive = !userService.isUserBlocked(user);
		if (isActive) {
			System.out.println("\n" + user.getUsername() + "'s account is currently Active.");
			System.out.print("Do you want to Block it?  Yes/No:\t");
		} else {
			System.out.println("\n" + user.getUsername() + "'s account is currently Blocked.");
			System.out.print("Do you want to Unblock it?  Yes/No:\t");
		}

		if (sc.nextLine().equalsIgnoreCase("yes")) {
			adminService.toggleUserStatus(user, isActive);
			System.out.println("\nSuccessfully " + (isActive ? "Blocked" : "Activated") + "...!\n");
		} else {
			System.out.println("\nUser account status is not changed.\n");
		}
	}

}
