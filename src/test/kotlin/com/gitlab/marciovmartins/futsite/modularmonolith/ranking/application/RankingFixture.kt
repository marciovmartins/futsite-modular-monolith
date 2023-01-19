package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO.MatchDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO.MatchDTO.PlayerDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO.MatchDTO.Team
import java.time.Instant
import java.util.UUID

object RankingFixture {
    fun gamedayDTO(
        gamedayId: UUID = UUID.randomUUID(),
        amateurSoccerGroupId: UUID = UUID.randomUUID(),
        date: Instant = Instant.now(),
        matches: List<MatchDTO> = listOf(
            matchDTO(),
        )
    ) = GamedayDTO(
        id = gamedayId,
        amateurSoccerGroupId = amateurSoccerGroupId,
        date = date,
        matches = matches,
    )

    fun matchDTO(
        players: Set<PlayerDTO> = setOf(
            playerDTO(team = Team.A),
            playerDTO(team = Team.B),
        )
    ) = MatchDTO(
        players = players,
    )

    fun playerDTO(
        playerId: UUID = UUID.randomUUID(),
        team: Team,
        goalsInFavor: UByte = 1u,
        goalsAgainst: UByte = 0u,
        yellowCards: UByte = 0u,
        blueCards: UByte = 0u,
        redCards: UByte = 0u
    ) = PlayerDTO(playerId, team, goalsInFavor, goalsAgainst, yellowCards, blueCards, redCards)
}
