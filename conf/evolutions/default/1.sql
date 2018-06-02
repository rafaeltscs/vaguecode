# --- First database schema

# --- !Ups

create table users (
  id                        bigint auto_increment,
  name                      varchar(255) not null,
  role                      varchar(255) not null,
  CONSTRAINT UC_users UNIQUE (name)
);

# --- !Downs

drop table users;