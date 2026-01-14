
--liquibase formatted sql

--changeset burgasvv:1
begin ;
insert into author(firstname, lastname, patronymic, about)
values ('Дворянкин','Алексей','Петрович','Описание автора Дворянкина Алексея Петровича');
insert into author(firstname, lastname, patronymic, about)
values ('Петряков','Виктор','Николаевич','Описание автора Петрякова Виктора Николаевича');
insert into author(firstname, lastname, patronymic, about)
values ('Валдаев','Денис','Павлович','Описание автора Валдаева Дениса Павловича');
commit ;