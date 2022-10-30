--drop database task_manager;
--create database task_manager;

create schema public;

create schema dictionary;

create table dictionary.access_rights(
  id serial primary key,
  name varchar not null unique
);
insert into dictionary.access_rights(name) values ('REGULAR'),('ADMIN');

create table users(
    id serial primary key,
    email varchar unique ,
    password_hash varchar,
    access_rights_id int references dictionary.access_rights
);
insert into users(email, password_hash, access_rights_id) values ('admin@mail.ru', '-969160600', 2);


create table persons(
    user_id int references users unique,
    name varchar,
    surname varchar,
    nickname varchar
);

create table timetables(
    id serial primary key,
    user_id int references users,
    name varchar
);

create table note_types(
    id serial primary key,
    name varchar
);

create table task_manager.tasks(
    id serial primary key,
    timetable_id int references task_manager.timetables,
    name varchar,
    description varchar,
    notification_start_time timestamp,
    deadline_time timestamp,
    type int references
);

create table task_manager.events(
    id serial primary key,
    timetable_id int references task_manager.timetables,
    name varchar,
    description varchar,
    start_time timestamp,
    end_time timestamp
);

create table task_manager.repeatability(
    id serial primary key,
    name varchar not null unique
);

create table task_manager.periods(
    event_id int references task_manager.events,
    start_time timestamp not null,
    end_time timestamp,
    repeatability_id int references task_manager.repeatability,
    count_of_repeats int
);



