insert into amateur_soccer_group_players (player_id, amateur_soccer_group_id)
select distinct gps.player_id, g.amateur_soccer_group_id
from gamedays_player_statistic gps
         inner join gamedays_matches gm on gps.match_id = gm.match_id
         inner join gamedays g on gm.gameday_id = g.gameday_id;