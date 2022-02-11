package com.bookstore.services;

import java.util.List;

import com.bookstore.models.Book;
import com.bookstore.models.User;
import com.bookstore.repositories.BookRepository;
import com.bookstore.repositories.implementations.BookRepoImpl;
import com.bookstore.utilities.Printable;

public class BookService {

	private static BookRepository bookRepo;

	static {
		bookRepo = new BookRepoImpl();
	}

	public List<Printable> fetchAllBooks() {
		List<Printable> list = bookRepo.fetchAllBooks();
		if (list.isEmpty()) {
			System.out.println("\nSorry...!\nNo books are available at the moment.\n");
		}
		return list;
	}

	public boolean recommendBook(Book book, User user) {
		List<Printable> recommendedBooks = bookRepo.fetchRecommendedBooks(user);
		if (recommendedBooks.contains(book)) {
			return false;
		}
		bookRepo.recommendBook(book, user);
		return true;
	}

	public Book fetchBookById(int bookId) {
		return bookRepo.fetchBookById(bookId);
	}

	public List<Printable> fetchRecommendedBooks(User user) {
		List<Printable> list = bookRepo.fetchRecommendedBooks(user);
		if (list.isEmpty()) {
			System.out.println("\nYou haven't recommended any book yet.\n");
		}
		return list;
	}

	public List<Printable> fetchAllRecommendedBooks() {
		List<Printable> list = bookRepo.fetchAllRecommendedBooks();
		if (list.isEmpty()) {
			System.out.println("\nSorry...!\nNo book is recommended by any user yet.\n");
		}
		return list;
	}

	public void rateBook(int bookId, int rating, User user) {
		Book book = bookRepo.fetchBookById(bookId);
		if (book == null) {
			System.err.println("\nInvalid Book Id: " + bookId + "\nNo such book exists.\n");
			return;
		}
		bookRepo.insertRating(bookId, rating, user);
		System.out.println("\nSuccessfully rated the Book...!");
	}

	public List<Printable> getRatingsGivenByUser(User user) {
		List<Printable> list = bookRepo.getRatingsGivenByUser(user);
		if (list.isEmpty()) {
			System.out.println("\nYou haven't rated any book yet.\n");
		}
		return list;
	}

	public List<Printable> fetchRatedBooks(User user) {
		List<Printable> list = bookRepo.fetchRatedBooks(user);
		if (list.isEmpty()) {
			System.out.println("\nYou haven't rated any book yet.\n");
		}
		return list;
	}
}
