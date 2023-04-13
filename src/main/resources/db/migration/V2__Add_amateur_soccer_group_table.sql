create table amateur_soccer_group
(
    amateur_soccer_group_id binary(16)   not null primary key,
    name                    varchar(255) not null,
    date_created            timestamp    not null default current_timestamp,
    index amateur_soccer_group_id_index (amateur_soccer_group_id)
);

alter table gamedays
    add foreign key gamedays_amateur_soccer_group_id (amateur_soccer_group_id) references amateur_soccer_group (amateur_soccer_group_id);