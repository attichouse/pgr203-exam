create table PERSON
(
    person_id serial primary key,
    first_name varchar(100) not null,
    last_name varchar (100) not null,
    email varchar(100) not null
);