DROP TABLE IF EXISTS book_contact;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS contact;

CREATE TABLE contact
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    phone VARCHAR(25)  NOT NULL
);

CREATE TABLE book
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE book_contact
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    bookId    INT NOT NULL,
    contactId INT NOT NULL
);

-- INSERT INTO contact (name, phone)
-- VALUES ('Kermit Frog', '0410 987 654'),
--        ('Miss Piggy', '+61 418 123 145'),
--        ('Fozzie Bear', '9123 4567'),
--        ('The Great Gonzo', '0400 123 567');