package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.domain.AmateurSoccerGroup.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO.MatchDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday.GamedayId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday.Match
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday.Match.Player

object GamedayMapper {
    fun from(gamedayDTO: GamedayDTO) = Gameday(
        id = GamedayId(gamedayDTO.id),
        amateurSoccerGroupId = AmateurSoccerGroupId(gamedayDTO.amateurSoccerGroupId),
        date = Gameday.Date(gamedayDTO.date),
        matches = gamedayDTO.matches.map {
            Match(
                players = it.players.map { playerDTO ->
                    Player(
                        playerId = Player.PlayerId(playerDTO.playerId),
                        team = Match.Team.valueOf(playerDTO.team.name),
                        goalsInFavor = Player.GoalsInFavor(playerDTO.goalsInFavor),
                        goalsAgainst = Player.GoalsAgainst(playerDTO.goalsAgainst),
                        yellowCards = Player.YellowCards(playerDTO.yellowCards),
                        blueCards = Player.BlueCards(playerDTO.blueCards),
                        redCards = Player.RedCards(playerDTO.redCards),
                    )
                }.toSet(),
            )
        },
    )

    fun from(gameday: Gameday) = GamedayDTO(
        id = gameday.id.value,
        amateurSoccerGroupId = gameday.amateurSoccerGroupId.value,
        date = gameday.date.value,
        matches = gameday.matches.map {
            MatchDTO(
                players = it.players.map { player ->
                    MatchDTO.PlayerDTO(
                        playerId = player.playerId.value,
                        team = MatchDTO.Team.valueOf(player.team.name),
                        goalsInFavor = player.goalsInFavor.value,
                        goalsAgainst = player.goalsAgainst.value,
                        yellowCards = player.yellowCards.value,
                        blueCards = player.blueCards.value,
                        redCards = player.redCards.value,
                    )
                }.toSet(),
            )
        },
    )
}
