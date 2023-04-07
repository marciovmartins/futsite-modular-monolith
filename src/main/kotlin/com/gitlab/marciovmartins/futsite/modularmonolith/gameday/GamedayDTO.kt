package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import java.time.Instant
import java.util.UUID

data class GamedayDTO(
    val gamedayId: UUID,
    val amateurSoccerGroupId: UUID,
    val date: Instant,
    val matches: List<Match>
) {
    data class Match(
        val players: Set<PlayerStatistic>,
    ) {
        data class PlayerStatistic(
            val playerId: UUID,
            val team: Team,
            val goalsInFavor: UByte,
            val ownGoals: UByte,
            val yellowCards: UByte,
            val blueCards: UByte,
            val redCards: UByte,
        )

        enum class Team {
            A, B,
        }
    }
}
