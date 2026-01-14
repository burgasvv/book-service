
--liquibase formatted sql

--changeset burgasvv:1
begin ;
insert into publisher(name, description)
values ('Росмэн','Описание издателя Росмэн');
insert into publisher(name, description)
values ('Фаргус','Описание издателя Фаргус');
commit ;