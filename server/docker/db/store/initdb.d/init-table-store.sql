drop database store;
CREATE SCHEMA IF NOT EXISTS `store` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `store`;

create table menu
(
    menu_id          bigint       not null
        primary key,
    user_id          bigint       not null,
    category_id      bigint       not null,
    menu_name        varchar(255) not null,
    menu_description varchar(255) null,
    menu_recipe      varchar(255) null,
    menu_price       int          not null,
    created_at       datetime     not null
)
    charset = utf8mb4;

create index  menu_menu_category_category_id_fk
    on menu (category_id);

create index menu_store_store_id_fk
    on menu (user_id);

create table if NOT EXISTS menu_category
(
    category_id   bigint       not null
        primary key,
    user_id       bigint       not null,
    category_name varchar(100) not null,
    created_at    datetime     not null
)
    charset = utf8mb4;

create table if NOT EXISTS menu_image
(
    image_id    bigint   not null,
    menu_id     bigint   not null,
    user_id     bigint   not null
        primary key,
    image_bytes blob     not null,
    created_at  datetime not null,
    constraint fk_menu_image_menu
        foreign key (menu_id) references menu (menu_id)
            on delete cascade
)
    charset = utf8mb4;

create index menu_image_menu_menu_id_fk
    on menu_image (image_id);

create table if NOT EXISTS menu_option
(
    option_id       bigint                   not null
        primary key,
    option_group_id bigint                   not null,
    menu_id         bigint                   not null,
    option_type     enum ('Common', 'Extra') not null,
    option_name     varchar(255)             not null,
    option_value    varchar(255)             not null,
    option_price    int                      not null,
    created_at      datetime                 not null,
    constraint fk_menu_option_menu
        foreign key (menu_id) references menu (menu_id)
            on delete cascade
)
    charset = utf8mb4;
