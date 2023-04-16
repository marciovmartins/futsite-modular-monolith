package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain

import java.time.Instant

class Gameday(
    val amateurSoccerGroupId: AmateurSoccerGroupId,
    val date: Date,
    val matches: List<Match>,
) {
    data class Match(
        val players: Set<PlayerStatistic>,
    ) {
        data class PlayerStatistic(
            val playerId: PlayerId,
            val team: Team,
            val goalsInFavor: UByte,
            val ownGoals: UByte,
        )

        enum class Team {
            A, B
        }
    }

    data class Date(
        val value: Instant,
    )
}
