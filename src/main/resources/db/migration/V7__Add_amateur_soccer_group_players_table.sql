create table amateur_soccer_group_players
(
    player_id               binary(16) not null primary key,
    amateur_soccer_group_id binary(16) not null,
    index amateur_soccer_group_players_player_id_index (player_id),
    foreign key amateur_soccer_group_players_amateur_soccer_group_id (amateur_soccer_group_id) references amateur_soccer_group (amateur_soccer_group_id)
);
