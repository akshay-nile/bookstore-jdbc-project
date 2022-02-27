package com.bookstore.services;

import java.util.List;

import com.bookstore.models.Book;
import com.bookstore.models.User;
import com.bookstore.repos.AdminRepository;
import com.bookstore.repos.impl.AdminRepoImpl;
import com.bookstore.utilities.Printable;

public class AdminService {

	private static AdminRepository adminRepo;

	static {
		adminRepo = new AdminRepoImpl();
	}

	public void addNewBook(Book book) {
		if (adminRepo.addNewBook(book)) {
			System.out.println("\nSuccessfully inserted new book...!\n");
		} else {
			System.out.println("\nSomething went wrong...!\nWe could not add the book.\n");
		}
	}

	public void updateExistingBook(Book book) {
		if (adminRepo.updateExistingBook(book)) {
			System.out.println("\nSuccessfully updated the book...!\n");
		} else {
			System.out.println("\nSomething went wrong...!\nWe could not update the book.\n");
		}
	}

	public void deleteBook(Book book) {
		if (adminRepo.deleteBook(book)) {
			System.out.println("\nSuccessfully deleted the book...!\n");
		} else {
			System.out.println("\nSomething went wrong...!\nWe could not delete the book.\n");
		}
	}

	public List<Printable> fetchAllBookDetails() {
		List<Printable> list = adminRepo.fetchAllBookDetails();
		if (list.isEmpty()) {
			System.out.println("\nSorry...!\nNo book available at the moment.\n");
		}
		return list;
	}

	public List<Printable> fetchTop5MostRatedBooks() {
		List<Printable> list = adminRepo.fetchTop5MostRatedBooks();
		if (list.isEmpty()) {
			System.out.println("\nSorry...!\nNo ratings available at the moment.\n");
		}
		return list;
	}

	public List<Printable> fetchTop5MostRecommendedBooks() {
		List<Printable> list = adminRepo.fetchTop5MostRecommendedBooks();
		if (list.isEmpty()) {
			System.out.println("\nSorry...!\nNo recommendations available at the moment.\n");
		}
		return list;
	}

	public List<Printable> fetchAllUserDetails() {
		List<Printable> list = adminRepo.fetchAllUserDetails();
		if (list.isEmpty()) {
			System.out.println("\nSorry...!\nNo user has registered yet.\n");
		}
		return list;
	}

	public void toggleUserStatus(User user, boolean isActive) {
		adminRepo.toggleUserStatus(user, isActive);
	}

	public List<Printable> fetchTop5MostRequestedBooks() {
		List<Printable> list = adminRepo.fetchTop5MostRequestedBooks();
		if (list.isEmpty()) {
			System.out.println("\nSorry...!\nNo book request is placed by any user yet.\n");
		}
		return list;
	}
}
