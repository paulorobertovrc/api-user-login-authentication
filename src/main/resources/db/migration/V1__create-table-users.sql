create table users (
    id int not null auto_increment,
    username varchar(255) not null unique,
    password varchar(255) not null,
    roles varchar(255) not null,

    primary key (id)
);
