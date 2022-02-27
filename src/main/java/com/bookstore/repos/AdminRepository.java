package com.bookstore.repos;

import java.util.List;

import com.bookstore.models.Book;
import com.bookstore.models.User;
import com.bookstore.utilities.Printable;

public interface AdminRepository {

	public List<Printable> fetchAllBookDetails();
	
	public boolean deleteBook(Book book);
	
	public boolean addNewBook(Book book);
	
	public boolean updateExistingBook(Book book);
	
	public List<Printable> fetchTop5MostRatedBooks();
	
	public List<Printable> fetchTop5MostRequestedBooks();
	
	public List<Printable> fetchTop5MostRecommendedBooks();
	
	public List<Printable> fetchAllUserDetails() ;
	
	public void toggleUserStatus(User user, boolean isActive);
	
}
