package com.bookstore.repos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.configs.DBConnection;
import com.bookstore.models.User;
import com.bookstore.repos.UserRepository;
import com.bookstore.utilities.Printable;

public class UserRepoImpl implements UserRepository {

	private static Connection conn;

	static {
		conn = DBConnection.getConnection();
	}

	public boolean fieldExists(String fieldName, String fieldValue) {
		boolean exist = false;
		try {
			String query = "SELECT id FROM users WHERE " + fieldName + "=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, fieldValue);
			ResultSet rs = ps.executeQuery();
			exist = rs.next();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exist;
	}

	public void insertUser(User user) {
		try {
			String query = "INSERT INTO users(username, password, email, phone) VALUES(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, user.getUsername());
			ps.setInt(2, user.getPassword().hashCode());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPhone());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User fetchUser(String username, String password) {
		User user = null;
		try {
			String query = "SELECT id, email, phone FROM users WHERE username=? AND password=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setInt(2, password.hashCode());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User(username, password);
				user.setId(rs.getInt("id"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public double getCumulativeRatings() {
		double avgRating = 0.0;
		try {
			String query = "SELECT SUM(r.rating)/COUNT(b.id) AS avg_rating FROM books AS b LEFT JOIN ratings AS r ON b.id=r.id";
			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);
			if (rs.next()) {
				avgRating = rs.getDouble("avg_rating");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return avgRating;
	}

	public boolean deleteUserAccount(User user) {
		try {
			String query = "DELETE FROM users WHERE id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateUserPassword(User user) {
		try {
			String query = "UPDATE users SET password=? WHERE id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getPassword().hashCode());
			ps.setInt(2, user.getId());
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isUserBlocked(User user) {
		boolean blocked = false;
		try {
			String query = "SELECT user_id FROM blocklist WHERE user_id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			blocked = rs.next();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return blocked;
	}

	public User fetchUserById(int userId) {
		User user = null;
		try {
			String query = "SELECT * FROM users WHERE id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean addBookRequest(User user, String bookName, String authorName) {
		try {
			String query = "SELECT user_id FROM requests WHERE bookname=? AND authorname=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, bookName);
			ps.setString(2, authorName);
			ResultSet rs = ps.executeQuery();
			if (rs.next() && rs.getInt("user_id") == user.getId()) {
				rs.close();
				ps.close();
				return false;
			}
			query = "INSERT INTO requests(bookname, authorname, user_id) VALUES(?,?,?)";
			ps = conn.prepareStatement(query);
			ps.setString(1, bookName);
			ps.setString(2, authorName);
			ps.setInt(3, user.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public List<Printable> fetchRequestedBooks(User user) {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "SELECT id, bookname, authorname FROM requests WHERE user_id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String requestId = "" + rs.getInt("id");
				String bookName = rs.getString("bookname");
				String authorName = rs.getString("authorname");
				list.add((row) -> {
					row.put("Id", requestId);
					row.put("Book Name", bookName);
					row.put("Author Name", authorName);
				});
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
