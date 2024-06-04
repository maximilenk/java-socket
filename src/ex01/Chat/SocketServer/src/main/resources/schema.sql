drop table if exists users cascade;

CREATE TABLE IF NOT EXISTS users(
    id serial PRIMARY KEY,
    email varchar(50) NOT NULL,
    password varchar(100) NOT NULL
);

drop table if exists messages cascade;

CREATE TABLE IF NOT EXISTS messages(
    id serial PRIMARY KEY,
    text varchar(50) NOT NULL,
    sender bigint,
	date timestamp default current_timestamp,
    foreign key(sender) references users(id)
);

