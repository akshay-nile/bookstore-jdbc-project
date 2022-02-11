package com.bookstore.repositories.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bookstore.configs.DBConnection;
import com.bookstore.models.Book;
import com.bookstore.models.User;
import com.bookstore.repositories.AdminRepository;
import com.bookstore.utilities.Printable;

public class AdminRepoImpl implements AdminRepository {

	private static Connection conn;

	static {
		conn = DBConnection.getConnection();
	}

	public List<Printable> fetchAllBookDetails() {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "";
			query += "SELECT b.bookid, b.bookname, b.author, b.genre, b.price, AVG(rt.rating) AS avg_rating, COUNT(rm.userid) AS user_count ";
			query += "FROM books AS b ";
			query += "LEFT JOIN ratings AS rt ON b.bookid=rt.bookid ";
			query += "LEFT JOIN recommendations AS rm ON b.bookid=rm.bookid ";
			query += "GROUP BY b.bookid";

			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				String id = "" + rs.getInt("b.bookid");
				String bookName = rs.getString("b.bookname");
				String author = rs.getString("b.author");
				String genre = String.valueOf(rs.getString("b.genre"));
				String price = String.format("₹ %.2f", rs.getDouble("b.price"));
				int r = rs.getInt("avg_rating");
				String avgRating = r > 0 ? "✶ ".repeat(r).trim() : "no rating";
				r = rs.getInt("user_count");
				String userCount = r > 0 ? r + (r > 1 ? " users" : " user") : "not recommended";
				list.add((row) -> {
					row.put("Id", id);
					row.put("Book Name", bookName);
					row.put("Author", author);
					row.put("Genre", genre);
					row.put("Price", price);
					row.put("Avg Rating", avgRating);
					row.put("Recommended by", userCount);
				});
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean deleteBook(Book book) {
		try {
			String query = "DELETE FROM books WHERE bookid=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, book.getBookId());
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean addNewBook(Book book) {
		try {
			String query = "INSERT INTO books(bookname, author, genre, price) VALUES(?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, book.getBookName());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getGenre().toString());
			ps.setDouble(4, book.getPrice());
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateExistingBook(Book book) {
		try {
			String query = "UPDATE books SET bookname=?, author=?, genre=?, price=? WHERE bookid=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, book.getBookName());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getGenre().toString());
			ps.setDouble(4, book.getPrice());
			ps.setInt(5, book.getBookId());
			ps.executeUpdate();
			ps.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Printable> fetchTop5MostRatedBooks() {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "";
			query += "SELECT b.bookid, b.bookname, b.author, b.genre, b.price, AVG(rt.rating) AS avg_rating ";
			query += "FROM books AS b ";
			query += "LEFT JOIN ratings AS rt ON b.bookid=rt.bookid ";
			query += "GROUP BY b.bookid ";
			query += "ORDER BY (avg_rating) DESC ";
			query += "LIMIT 5";

			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				String id = "" + rs.getInt("b.bookid");
				String bookName = rs.getString("b.bookname");
				String author = rs.getString("b.author");
				String genre = String.valueOf(rs.getString("b.genre"));
				String price = String.format("₹ %.2f", rs.getDouble("b.price"));
				int r = rs.getInt("avg_rating");
				if (r == 0) {
					continue;
				}
				String avgRating = "✶ ".repeat(r).trim();
				list.add((row) -> {
					row.put("Id", id);
					row.put("Book Name", bookName);
					row.put("Author", author);
					row.put("Genre", genre);
					row.put("Price", price);
					row.put("Avg Rating", avgRating);
				});
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Printable> fetchTop5MostRecommendedBooks() {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "";
			query += "SELECT b.bookid, b.bookname, b.author, b.genre, b.price, COUNT(rm.userid) AS user_count ";
			query += "FROM books AS b ";
			query += "LEFT JOIN recommendations AS rm ON b.bookid=rm.bookid ";
			query += "GROUP BY b.bookid ";
			query += "ORDER BY (user_count) DESC ";
			query += "LIMIT 5";

			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				String id = "" + rs.getInt("b.bookid");
				String bookName = rs.getString("b.bookname");
				String author = rs.getString("b.author");
				String genre = String.valueOf(rs.getString("b.genre"));
				String price = String.format("₹ %.2f", rs.getDouble("b.price"));
				int r = rs.getInt("user_count");
				if (r == 0) {
					continue;
				}
				String userCount = r + (r > 1 ? " users" : " user");
				list.add((row) -> {
					row.put("Id", id);
					row.put("Book Name", bookName);
					row.put("Author", author);
					row.put("Genre", genre);
					row.put("Price", price);
					row.put("Recommended by", userCount);
				});
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Printable> fetchAllUserDetails() {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "";
			query += "SELECT u.userid, u.username, u.email, u.phone, s.userid AS status ";
			query += "FROM users AS u ";
			query += "LEFT JOIN blocklist AS s ON u.userid=s.userid";

			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				String id = "" + rs.getInt("u.userid");
				String username = rs.getString("u.username");
				String email = rs.getString("u.email");
				String phone = rs.getString("u.phone");
				int r = rs.getInt("status");
				String status = r == 0 ? "active" : "blocked";
				list.add((row) -> {
					row.put("Id", id);
					row.put("Username", username);
					row.put("Email Id", email);
					row.put("Phone No.", phone);
					row.put("Status", status);
				});
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void toggleUserStatus(User user, boolean isActive) {
		String query = isActive ? "INSERT INTO blocklist VALUES(?)" : "DELETE FROM blocklist WHERE userid=?";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getUserId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Printable> fetchTop5MostRequestedBooks() {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "SELECT bookname, authorname, COUNT(bookname) AS book_count FROM requests GROUP BY bookname ORDER BY book_count DESC LIMIT 5";
			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				String bookName = rs.getString("bookname");
				String authorName = rs.getString("authorname");
				int c = rs.getInt("book_count");
				String userCount = c + (c > 1 ? " users" : " user");
				list.add((row) -> {
					row.put("Book Name", bookName);
					row.put("Author Name", authorName);
					row.put("Requested by", userCount);
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
