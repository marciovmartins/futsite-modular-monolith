package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import java.time.Instant
import java.util.UUID

data class RankingDTO(
    val amateurSoccerGroupId: UUID,
    val period: Period,
    val matches: UShort,
    val playerStatistics: Set<PlayerStatistic>,
) {
    data class Period(
        val from: Instant,
        val to: Instant,
    )

    /**
     * TODO: classification and points need to be implemented
     */
    data class PlayerStatistic(
        val playerId: UUID,
        val matches: UShort,
        val victories: UShort,
        val draws: UShort,
        val defeats: UShort,
        val goalsInFavor: UShort,
        val ownGoals: UShort,
    )
}