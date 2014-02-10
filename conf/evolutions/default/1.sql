# --- First database schema

# --- !Ups

CREATE TABLE entry (
    id int NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    content TEXT NOT NULL,
    `date` TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

create sequence entry_seq start with 1000;

# --- !Downs

drop table if exists entry;
