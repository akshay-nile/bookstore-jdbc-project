package com.bookstore.models;

import java.util.Map;

import com.bookstore.exceptions.InvalidBookNameException;
import com.bookstore.exceptions.InvalidBookPriceException;
import com.bookstore.utilities.Printable;

public class Book implements Printable {

	private int id;
	private String name;
	private String author;
	private Genre genre;
	private double price;

	public Book() {
	}

	public Book(int id, String name, String author, Genre genre, double price) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.genre = genre;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int bookId) {
		this.id = bookId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new InvalidBookNameException("Book name can't be null or empty.");
		}
		this.name = name;
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
		return String.format("Book( id=%d, name='%s', price=Rs. %.2f, rating=%.2f, genre=%s )", id, name, price, genre);
	}

	@Override
	public int hashCode() {
		return id + name.hashCode();
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
		return id == other.id && name.equalsIgnoreCase(other.name);
	}

	@Override
	public void loadCellValues(Map<String, String> row) {
		row.put("Id", "" + id);
		row.put("Name", name);
		row.put("Author", author);
		row.put("Genre", "" + genre);
		row.put("Price", String.format("₹ %.2f", price));
	}

}
