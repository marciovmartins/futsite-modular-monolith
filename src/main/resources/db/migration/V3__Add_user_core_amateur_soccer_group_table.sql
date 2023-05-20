create table user_core_amateur_soccer_group
(
    amateur_soccer_group_id binary(16)   not null primary key,
    name                    varchar(255) not null,
    index amateur_soccer_group_id_index (amateur_soccer_group_id)
);
