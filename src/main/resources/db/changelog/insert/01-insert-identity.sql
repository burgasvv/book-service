
--liquibase formatted sql

--changeset burgasvv:1
begin ;
insert into identity(authority, username, password, email, firstname, lastname, patronymic)
values ('ADMIN','burgasvv','$2a$10$OnYCh6Ht8OZ5BufobMNe7eZEbzp4/k2NvhS831FokRu1R/108Tzqi','burgasvv@gmail.com',
        'Burgas','Vyacheslav','Vasilevich');
commit ;