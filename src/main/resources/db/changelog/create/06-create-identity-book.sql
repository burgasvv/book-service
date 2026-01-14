
--liquibase formatted sql

--changeset burgasvv:1
create table if not exists identity_book(
    identity_id uuid references identity(id) on delete cascade on update cascade ,
    book_id uuid references book(id) on delete cascade on update cascade
)