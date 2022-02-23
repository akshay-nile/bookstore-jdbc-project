package com.bookstore.models;

import java.util.Map;

import com.bookstore.utilities.Printable;

public class User implements Printable {

	private int id;
	private String username;
	private String password;
	private String email;
	private String phone;

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String username, String password, String email, String phone) {
		this(username, password);
		this.email = email;
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "User [userId=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", phone=" + phone + "]";
	}

	@Override
	public void loadCellValues(Map<String, String> row) {
		row.put("Id", "" + id);
		row.put("Username", username);
		row.put("Email Id", email);
		row.put("Phone No.", phone);
	}

}
