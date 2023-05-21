create table user_core_amateur_soccer_group_players
(
    player_id binary(16)   not null primary key,
    name      varchar(255) not null,
    index user_core_amateur_soccer_group_players_player_id_index (player_id)
);
