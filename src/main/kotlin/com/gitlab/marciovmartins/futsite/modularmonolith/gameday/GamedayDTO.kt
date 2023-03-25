package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import java.time.Instant
import java.util.UUID

data class GamedayDTO(
    val id: UUID,
    val amateurSoccerGroupId: UUID,
    val date: Instant,
    val matches: List<MatchDTO>,
) {
    data class MatchDTO(
        val players: Set<PlayerDTO>,
    ) {
        data class PlayerDTO(
            val playerId: UUID,
            val team: Team,
            val goalsInFavor: UByte,
            val goalsAgainst: UByte,
            val yellowCards: UByte,
            val blueCards: UByte,
            val redCards: UByte,
        )

        enum class Team {
            A, B
        }
    }
}
