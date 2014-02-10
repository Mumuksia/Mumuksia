# --- First database schema

# --- !Ups

CREATE TABLE `comment` (
    id int NOT NULL AUTO_INCREMENT,
    text TEXT NOT NULL,
    username varchar(128) NOT NULL,
    `date` TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

create sequence comment_seq start with 1000;

# --- !Downs

drop table if exists `comment`;
