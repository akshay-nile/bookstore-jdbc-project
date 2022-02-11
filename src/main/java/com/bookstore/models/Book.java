package com.bookstore.models;

import java.util.Map;

import com.bookstore.exceptions.InvalidBookNameException;
import com.bookstore.exceptions.InvalidBookPriceException;
import com.bookstore.utilities.Printable;

public class Book implements Printable {

	private int bookId;
	private String bookName;
	private String author;
	private Genre genre;
	private double price;

	public Book() {
	}

	public Book(int bookId, String bookName, String author, Genre genre, double price) {
		this.bookId = bookId;
		this.bookName = bookName;
		this.author = author;
		this.genre = genre;
		this.price = price;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		if (bookName == null || bookName.trim().isEmpty()) {
			throw new InvalidBookNameException("Book name can't be null or empty.");
		}
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price <= 0 || price > 1000) {
			throw new InvalidBookPriceException("Book price should be between 1 to 1000 Rs only.");
		}
		this.price = price;
	}

	@Override
	public String toString() {
		return String.format("Book( id=%d, name='%s', price=Rs. %.2f, rating=%.2f, genre=%s )", bookId, bookName, price,
				genre);
	}

	@Override
	public int hashCode() {
		return bookId + bookName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Book other = (Book) obj;
		return bookId == other.bookId && bookName.equalsIgnoreCase(other.bookName);
	}

	@Override
	public void loadCellValues(Map<String, String> row) {
		row.put("Id", "" + bookId);
		row.put("Name", bookName);
		row.put("Author", author);
		row.put("Genre", "" + genre);
		row.put("Price", String.format("₹ %.2f", price));
	}

}
