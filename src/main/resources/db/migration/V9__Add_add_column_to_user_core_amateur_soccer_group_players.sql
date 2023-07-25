alter table user_core_amateur_soccer_group_players
    add column amateur_soccer_group_id binary(16) after player_id,
    add index user_core_amateur_soccer_group_players_amateur_soccer_group_id_i (amateur_soccer_group_id)
;

### the amateur_soccer_group_id will be changed to not null after the data been adjusted in the database with a next
### migration script