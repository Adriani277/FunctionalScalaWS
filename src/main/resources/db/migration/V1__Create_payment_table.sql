create table notes (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title varchar(100) not null UNIQUE,
    content TEXT,
    tags TEXT
);