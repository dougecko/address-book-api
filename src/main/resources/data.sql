DROP TABLE IF EXISTS contact;

CREATE TABLE contact (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(25) DEFAULT NULL
);

INSERT INTO contact (name, phone)
VALUES ('Kermit Frog', '0410 987 654'),
       ('Miss Piggy', '+61 418 123 145'),
       ('Fozzie Bear', '9123 4567'),
       ('The Great Gonzo', '0400 123 567');