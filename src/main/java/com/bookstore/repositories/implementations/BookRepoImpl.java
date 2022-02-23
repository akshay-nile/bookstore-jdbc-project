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
import com.bookstore.models.Genre;
import com.bookstore.models.User;
import com.bookstore.repositories.BookRepository;
import com.bookstore.utilities.Printable;

public class BookRepoImpl implements BookRepository {

	private static Connection conn;

	static {
		conn = DBConnection.getConnection();
	}

	public List<Printable> fetchAllBooks() {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "SELECT * FROM books";
			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("id"));
				book.setName(rs.getString("name"));
				book.setAuthor(rs.getString("author"));
				book.setGenre(Genre.valueOf(rs.getString("genre")));
				book.setPrice(rs.getDouble("price"));
				list.add(book);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Printable> fetchRecommendedBooks(User user) {
		List<Printable> books = new ArrayList<>();
		try {
			String query = "SELECT b.id, b.name, b.author, b.genre, b.price FROM books b ";
			query += "JOIN recommendations r ON b.id=r.book_id AND r.user_id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("id"));
				book.setName(rs.getString("name"));
				book.setAuthor(rs.getString("author"));
				book.setGenre(Genre.valueOf(rs.getString("genre")));
				book.setPrice(rs.getDouble("price"));
				books.add(book);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}

	public void recommendBook(Book book, User user) {
		try {
			String query = "INSERT INTO recommendations(book_id, user_id) VALUES(?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, book.getId());
			ps.setInt(2, user.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Book fetchBookById(int bookId) {
		Book book = null;
		try {
			String query = "SELECT * FROM books WHERE id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, bookId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				book = new Book();
				book.setId(rs.getInt("id"));
				book.setName(rs.getString("name"));
				book.setAuthor(rs.getString("author"));
				book.setGenre(Genre.valueOf(rs.getString("genre")));
				book.setPrice(rs.getDouble("price"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
	}

	public List<Printable> fetchAllRecommendedBooks() {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "SELECT b.id, b.name, b.author, b.genre, b.price FROM books b ";
			query += "JOIN recommendations r ON b.id=r.book_id GROUP BY b.id";
			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("id"));
				book.setName(rs.getString("name"));
				book.setAuthor(rs.getString("author"));
				book.setGenre(Genre.valueOf(rs.getString("genre")));
				book.setPrice(rs.getDouble("price"));
				list.add(book);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void insertRating(int bookId, int rating, User user) {
		try {
			String query = "SELECT id FROM ratings WHERE book_id=? AND user_id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, bookId);
			ps.setInt(2, user.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int ratingId = rs.getInt("id");
				ps.close();
				query = "UPDATE ratings SET rating=? WHERE id=?";
				ps = conn.prepareStatement(query);
				ps.setInt(1, rating);
				ps.setInt(2, ratingId);
			} else {
				ps.close();
				query = "INSERT INTO ratings(rating, book_id, user_id) VALUES(?,?,?)";
				ps = conn.prepareStatement(query);
				ps.setInt(1, rating);
				ps.setInt(2, bookId);
				ps.setInt(3, user.getId());
			}
			ps.executeUpdate();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Printable> fetchRatedBooks(User user) {
		List<Printable> books = new ArrayList<>();
		try {
			String query = "SELECT b.id, b.name, b.author, b.genre, b.price FROM books b ";
			query += "JOIN ratings r ON b.id=r.book_id AND r.user_id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setId(rs.getInt("id"));
				book.setName(rs.getString("name"));
				book.setAuthor(rs.getString("author"));
				book.setGenre(Genre.valueOf(rs.getString("genre")));
				book.setPrice(rs.getDouble("price"));
				books.add(book);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}

	public List<Printable> getRatingsGivenByUser(User user) {
		List<Printable> list = new ArrayList<>();
		try {
			String query = "SELECT b.id, b.name, r.rating FROM books b JOIN ratings r ON b.id=r.book_id AND r.user_id=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int bookId = rs.getInt(1);
				String bookName = rs.getString(2);
				String ratings = "✶ ".repeat(rs.getInt(3)).trim();
				list.add((row) -> {
					row.put("Id", "" + bookId);
					row.put("Book Name", bookName);
					row.put("Ratings", ratings);
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
