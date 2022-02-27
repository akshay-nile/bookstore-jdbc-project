package com.bookstore.repos;

import java.util.List;

import com.bookstore.models.User;
import com.bookstore.utilities.Printable;

public interface UserRepository {

	public double getCumulativeRatings();

	public void insertUser(User user);

	public boolean fieldExists(String fieldName, String fieldValue);

	public boolean deleteUserAccount(User user);
	
	public boolean updateUserPassword(User user);

	public boolean isUserBlocked(User user);

	public User fetchUser(String username, String password);

	public User fetchUserById(int userId);

	public boolean addBookRequest(User user, String bookName, String authorName);

	public List<Printable> fetchRequestedBooks(User user);
	
}
