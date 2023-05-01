CREATE TABLE authors (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);

CREATE TABLE books (
    id UUID PRIMARY KEY,
    author UUID NOT NULL,
    isbn VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE INDEX idx_books_title ON books (title);
CREATE INDEX idx_books_isbn_title ON books (isbn, title);
