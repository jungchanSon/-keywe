CREATE SCHEMA IF NOT EXISTS `dev` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `dev`;

create table if not exists users
(
    user_id  bigint       not null
    primary key,
    role     varchar(50)  not null,
    email    varchar(255) not null,
    password varchar(255) null,
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
    phone_number varchar(50)  not null,
    profile_pass varchar(10)  null,
    reg_date     datetime     not null,
    constraint user_profile_fk
    foreign key (user_id) references users (user_id)
    on update cascade on delete cascade
    );

CREATE TABLE `menu` (
                        `menu_id` bigint NOT NULL,
                        `user_id` bigint NOT NULL,
                        `category_id` bigint NOT NULL,
                        `menu_name` varchar(255) NOT NULL,
                        `menu_desc` varchar(255) DEFAULT NULL,
                        `menu_price` int NOT NULL,
                        `created_at` datetime NOT NULL,
                        PRIMARY KEY (`menu_id`),
                        KEY `menu_menu_category_category_id_fk` (`category_id`),
                        KEY `menu_store_store_id_fk` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `menu_category` (
                                 `category_id` bigint NOT NULL,
                                 `user_id` bigint NOT NULL,
                                 `category_name` varchar(100) NOT NULL,
                                 `created_at` datetime NOT NULL,
                                 PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `menu_image` (
                              `image_id` bigint NOT NULL,
                              `menu_id` bigint NOT NULL,
                              `user_id` bigint NOT NULL,
                              `image_bytes` blob NOT NULL,
                              `created_at` datetime NOT NULL,
                              PRIMARY KEY (`user_id`),
                              KEY `menu_image_menu_menu_id_fk` (`image_id`),
                              KEY `fk_menu_image_menu` (`menu_id`),
                              CONSTRAINT `fk_menu_image_menu` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `menu_option` (
                               `option_id` bigint NOT NULL,
                               `option_group_id` bigint NOT NULL,
                               `menu_id` bigint NOT NULL,
                               `option_type` enum('RADIO','CHECKBOX') NOT NULL,
                               `option_name` varchar(255) NOT NULL,
                               `option_value` varchar(255) NOT NULL,
                               `created_at` datetime NOT NULL,
                               PRIMARY KEY (`option_id`),
                               KEY `fk_menu_option_menu` (`menu_id`),
                               CONSTRAINT `fk_menu_option_menu` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`menu_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
