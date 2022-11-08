--drop database task_manager;
--create database task_manager;

drop schema if exists public cascade ;
create schema public;

create table users
(
    id            bigserial primary key,
    email         varchar unique not null,
    password_hash varchar        not null,
    name          varchar,
    surname       varchar,
    nickname      varchar unique not null,
    access_rights varchar        not null check ( access_rights in ('REGULAR', 'ADMIN'))
);

insert into users(email, password_hash, nickname, access_rights) values ('admin@mail.ru', '21232F297A57A5A743894A0E4A801FC3', 'admin', 'ADMIN');



create table timetables
(
    id   bigserial primary key,
    name varchar not null
);



create table users_timetables
(
    user_id       bigint references users (id) on delete cascade,
    timetable_id  bigint references timetables (id) on delete cascade,
    access_rights varchar check ( access_rights in ('READER', 'WRITER', 'OWNER') ),

    unique (user_id, timetable_id)
);


create table notes
(
    id            bigserial primary key,
    timetable_id  int references timetables,
    name          varchar not null,
    body          varchar not null,
    is_in_archive boolean not null
);


create table tasks
(
    note_id                 bigint,-- primary key references notes (id) on delete cascade,
    notification_start_date timestamp not null,
    deadline_time           timestamp not null,
    is_done                 boolean   not null
);
alter table tasks add constraint deadline_bigger_start check(notification_start_date <= tasks.deadline_time);
alter table tasks add constraint notification_is_date check ( date_trunc('day', notification_start_date) = notification_start_date );

create table events
(
    note_id bigint primary key references notes (id) on delete cascade,
    place   varchar,
    link    varchar
);


create table periods
(
    event_id   bigint references events (note_id) on delete cascade,
    start_time timestamp not null,
    end_time   timestamp not null ,
    group_id   int
);
alter table periods add constraint end_bigger_start check ( start_time <= end_time );
alter table periods add constraint is_one_day check ( end_time <= date_trunc('day', start_time) + interval '1 day' );


create table usedGroups(
    id serial primary key
);

insert into usedGroups(id) values (default) returning id;






