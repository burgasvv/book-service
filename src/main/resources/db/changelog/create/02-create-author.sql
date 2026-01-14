
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists author(
    id uuid default gen_random_uuid() unique not null ,
    firstname varchar not null ,
    lastname varchar not null ,
    patronymic varchar not null ,
    about text unique not null
)