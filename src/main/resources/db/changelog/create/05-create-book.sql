
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists book(
    id uuid default gen_random_uuid() unique not null ,
    name varchar not null ,
    author_id uuid references author(id) on delete set null on update cascade ,
    publisher_id uuid references publisher(id) on delete set null on update cascade ,
    document_id uuid unique references document(id) on delete set null on update cascade ,
    about text unique not null ,
    price decimal not null default 0 check ( price >= 0 )
)