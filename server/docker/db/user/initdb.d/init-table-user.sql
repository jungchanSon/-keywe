CREATE SCHEMA IF NOT EXISTS `users` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE users;

create table if not exists users
(
    user_id  bigint       not null
    primary key,
    role     varchar(50)  not null,
    email    varchar(255) not null,
    password varchar(255) not null,
    salt     varchar(255) not null,
    reg_date datetime     not null
    );

create table if not exists user_profile
(
    profile_id   bigint       not null
    primary key,
    user_id      bigint       not null,
    profile_name varchar(255) not null,
    profile_role varchar(50)  not null,
    phone_number varchar(50)  null,
    profile_pass varchar(10)  null,
    reg_date     datetime     not null,
    constraint user_profile_fk
    foreign key (user_id) references users (user_id)
    on update cascade on delete cascade
    );

create table if not exists profile_image
(
    image_id    bigint     not null
        primary key,
    user_id     bigint     not null,
    profile_id  bigint     not null,
    image_bytes mediumblob not null,
    created_at  datetime   not null
);

