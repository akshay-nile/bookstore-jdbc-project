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
				book.setBookId(rs.getInt("bookid"));
				book.setBookName(rs.getString("bookname"));
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
			String query = "SELECT b.bookid, b.bookname, b.author, b.genre, b.price FROM books b, recommendations r WHERE b.bookid=r.bookid AND r.userid=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setBookId(rs.getInt("bookid"));
				book.setBookName(rs.getString("bookname"));
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
			String query = "INSERT INTO recommendations(bookid, userid) VALUES(?,?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, book.getBookId());
			ps.setInt(2, user.getUserId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Book fetchBookById(int bookId) {
		Book book = null;
		try {
			String query = "SELECT * FROM books WHERE bookid=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, bookId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				book = new Book();
				book.setBookId(rs.getInt("bookid"));
				book.setBookName(rs.getString("bookname"));
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
			String query = "SELECT b.bookid, b.bookname, b.author, b.genre, b.price FROM books b, recommendations r WHERE b.bookid=r.bookid GROUP BY b.bookid";
			Statement ps = conn.createStatement();
			ResultSet rs = ps.executeQuery(query);
			while (rs.next()) {
				Book book = new Book();
				book.setBookId(rs.getInt("bookid"));
				book.setBookName(rs.getString("bookname"));
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
			String query = "SELECT ratingid FROM ratings WHERE bookid=? AND userid=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, bookId);
			ps.setInt(2, user.getUserId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int ratingId = rs.getInt("ratingid");
				ps.close();
				query = "UPDATE ratings SET rating=? WHERE ratingid=?";
				ps = conn.prepareStatement(query);
				ps.setInt(1, rating);
				ps.setInt(2, ratingId);
			} else {
				ps.close();
				query = "INSERT INTO ratings(rating, bookid, userid) VALUES(?,?,?)";
				ps = conn.prepareStatement(query);
				ps.setInt(1, rating);
				ps.setInt(2, bookId);
				ps.setInt(3, user.getUserId());
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
			String query = "SELECT b.bookid, b.bookname, b.author, b.genre, b.price FROM books b, ratings r WHERE b.bookid=r.bookid AND r.userid=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setBookId(rs.getInt("bookid"));
				book.setBookName(rs.getString("bookname"));
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
			String query = "SELECT b.bookid, b.bookname, r.rating FROM books b, ratings r WHERE b.bookid=r.bookid AND r.userid=?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, user.getUserId());
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
