package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import java.time.Instant
import java.util.UUID

data class RankingDTO(
    val amateurSoccerGroupId: UUID,
    val period: Period,
    val playerStatistics: Set<PlayerStatistic>,
) {
    data class Period(
        val from: Instant,
        val to: Instant,
    )

    data class PlayerStatistic(
        val playerId: UUID,
        val matches: UShort,
        val classification: String?,
        val points: Long,
        val victories: UShort,
        val draws: UShort,
        val defeats: UShort,
        val goalsInFavor: UShort,
        val goalsAgainst: UShort,
    )
}