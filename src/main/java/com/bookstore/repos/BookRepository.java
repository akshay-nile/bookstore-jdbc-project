package com.bookstore.repos;

import java.util.List;

import com.bookstore.models.Book;
import com.bookstore.models.User;
import com.bookstore.utilities.Printable;

public interface BookRepository {

	public List<Printable> fetchAllBooks();

	public List<Printable> fetchAllRecommendedBooks();

	public List<Printable> fetchRecommendedBooks(User user);

	public List<Printable> fetchRatedBooks(User user);

	public void recommendBook(Book book, User user);

	public void insertRating(int bookId, int rating, User user);

	public List<Printable> getRatingsGivenByUser(User user);

	public Book fetchBookById(int bookId);

}
