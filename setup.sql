CREATE DATABASE IF NOT EXISTS bookstore;
USE bookstore;

DROP TABLE IF EXISTS recommendations;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS blocklist;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS books;

CREATE TABLE users(
	id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(20) NOT NULL UNIQUE,
	password INT NOT NULL,
	email VARCHAR(20) NOT NULL UNIQUE,
	phone VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE books(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(40) NOT NULL UNIQUE,
	author VARCHAR(20) NOT NULL,
	genre VARCHAR(20) NOT NULL,
	price DOUBLE NOT NULL CHECK (price >= 1 AND price <= 1000)
);

INSERT INTO books(name, author, genre, price) VALUES('Harry Potter','J.K. Rowling','FICTION',300.50);
INSERT INTO books(name, author, genre, price) VALUES('Interstellar','Christopher Nolan','FANTACY',780);
INSERT INTO books(name, author, genre, price) VALUES('Jokes on Fire','Johny Liver','COMEDY',450.20);
INSERT INTO books(name, author, genre, price) VALUES('Wings of Fire','APJ Abdul Kalam','INSPIRATIONAL',500.90);
INSERT INTO books(name, author, genre, price) VALUES('Deep Dive','Josh Stolberg','MYSTERY',120.70);
INSERT INTO books(name, author, genre, price) VALUES('Thunderstorm','Mary Thompson','HORROR',670.20);
INSERT INTO books(name, author, genre, price) VALUES('The Man Who Knew Infinity','Tim Berner','INSPIRATIONAL',900.30);
INSERT INTO books(name, author, genre, price) VALUES('Sherlock Holmes','Arthur Doyle','MYSTERY',420.70);
INSERT INTO books(name, author, genre, price) VALUES('Hunger Games','Edgar Poe','FANTACY',400.50);
INSERT INTO books(name, author, genre, price) VALUES('Fearless','Jean Leckie','HORROR',310.30);
INSERT INTO books(name, author, genre, price) VALUES('The Edge of Laughter','Charly Dikken','COMEDY',100.60);
INSERT INTO books(name, author, genre, price) VALUES('Swindel','Agatha Cristine','FANTACY',600);
INSERT INTO books(name, author, genre, price) VALUES("Don't Turn Arround",'Oscar Windley','HORROR',540);
INSERT INTO books(name, author, genre, price) VALUES('The Missing Child','Luice Hawk','MYSTERY',230.40);
INSERT INTO books(name, author, genre, price) VALUES('Holocost','Canon Bruice','INSPIRATIONAL',880.20);
INSERT INTO books(name, author, genre, price) VALUES('Dunes','Hellen King','FICTION',930.60);

CREATE TABLE recommendations(
	id INT AUTO_INCREMENT PRIMARY KEY,
	book_id INT NOT NULL, 
	user_id INT NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE ratings(
	id INT AUTO_INCREMENT PRIMARY KEY,
	rating INT NOT NULL CHECK(rating >= 1 AND rating <= 5),
	book_id INT NOT NULL, 
	user_id INT NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE blocklist(
	user_id INT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE requests(
	id INT AUTO_INCREMENT PRIMARY KEY,
	bookname VARCHAR(40) NOT NULL,
	authorname VARCHAR(20) NOT NULL,
	user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

