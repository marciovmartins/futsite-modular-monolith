create table gamedays
(
    gameday_id              binary(16) not null primary key,
    amateur_soccer_group_id binary(16) not null,
    gameday_date            timestamp  not null,
    date_created            timestamp  not null default current_timestamp,
    index gamedays_amateur_soccer_group_id_index (gameday_id, amateur_soccer_group_id)
);

create table gamedays_matches
(
    match_id   bigint unsigned not null primary key auto_increment,
    gameday_id binary(16)      not null,
    foreign key gamedays_matches_gameday_id_fk (gameday_id) references gamedays (gameday_id)
);

create table gamedays_player_statistic
(
    player_statistic_id bigint unsigned  not null primary key auto_increment,
    match_id            bigint unsigned  not null,
    player_id           binary(16)       not null,
    team                enum ('A', 'B')  not null,
    goals_in_favor      tinyint unsigned not null,
    own_goals           tinyint unsigned not null,
    yellow_cards        tinyint unsigned not null,
    blue_cards          tinyint unsigned not null,
    red_cards           tinyint unsigned not null,
    foreign key gamedays_player_statistic_match_id_fk (match_id) references gamedays_matches (match_id),
    index gamedays_player_statistic_player_id_index (player_id)
);