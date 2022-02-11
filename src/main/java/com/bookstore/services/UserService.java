package com.bookstore.services;

import java.util.List;

import com.bookstore.models.User;
import com.bookstore.repositories.UserRepository;
import com.bookstore.repositories.implementations.UserRepoImpl;
import com.bookstore.utilities.Printable;

public class UserService {

	private static UserRepository userRepo;

	static {
		userRepo = new UserRepoImpl();
	}

	public boolean fieldExists(String fieldName, String fieldValue) {
		if (isInvalidField(fieldName, fieldValue)) {
			System.out.println("Invalid Field name or value found...!\n");
			return false;
		}
		return userRepo.fieldExists(fieldName, fieldValue);
	}

	public boolean insertUser(User user) {
		if (isInvalidField(user.getUsername(), user.getPassword(), user.getEmail(), user.getPhone())) {
			System.out.println("\nCan not register a user with any null or blank field");
			return false;
		}

		if (!user.getEmail().matches("^(.+)@(.+)$")) {
			System.out.println("\nInvalid Email Id: " + user.getEmail());
			return false;
		}

		if (!user.getPhone().matches("[+]?\\d{10,12}")) {
			System.out.println("\nInvalid Phone Number: " + user.getPhone());
			return false;
		}

		userRepo.insertUser(user);
		return true;
	}

	private static boolean isInvalidField(String... strings) {
		for (String str : strings) {
			if (str == null || str.isBlank() || str.contains(" "))
				return true;
		}
		return false;
	}

	public User fetchUser(String username, String password) {
		if (isInvalidField(username, password)) {
			System.out.println("\nInvalid Username or Password");
			return null;
		}

		return userRepo.fetchUser(username, password);
	}

	public double getCumulativeRatings() {
		double r = userRepo.getCumulativeRatings();
		if(r == 0) {
			System.out.println("\nSorry...!\nNo ratings available yet.\n");
		}
		return r;
	}

	public boolean deleteUserAccount(User user) {
		return userRepo.deleteUserAccount(user);
	}

	public boolean updateUserPassword(User user) {
		return userRepo.updateUserPassword(user);
	}

	public boolean isUserBlocked(User user) {
		return userRepo.isUserBlocked(user);
	}

	public User fetchUserById(int userId) {
		return userRepo.fetchUserById(userId);
	}

	public boolean addBookRequest(User user, String bookName, String authorName) {
		if(bookName.isBlank() || authorName.isBlank()) {
			System.out.println("\nInvalid book name or author name.\n");
			return false;
		}
		
		if(!userRepo.addBookRequest(user, bookName, authorName)) {
			System.out.println("\nThis book is already requested by you.\n");
			return false;
		}
		
		return true;
	}

	public List<Printable> fetchRequestedBooks(User user) {
		List<Printable> list = userRepo.fetchRequestedBooks(user);
		if(list.isEmpty()) {
			System.out.println("\nYou haven't requested any book yet.\n");
		}
		return list;
	}
}
