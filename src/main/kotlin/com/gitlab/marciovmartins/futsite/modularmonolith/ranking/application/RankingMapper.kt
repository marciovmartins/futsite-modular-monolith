package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking

fun Ranking.toDTO() = RankingDTO(
    amateurSoccerGroupId = this.amateurSoccerGroupId.value,
    period = this.period.let { RankingDTO.Period(it.from, it.to) },
    matches = this.matches.value,
    playerStatistics = this.playerStatistics.map {
        RankingDTO.PlayerStatistic(
            playerId = it.playerId.value,
            matches = it.matches.value,
            victories = it.victories.value,
            draws = it.draws.value,
            defeats = it.defeats.value,
            goalsInFavor = it.goalsInFavor.value,
            ownGoals = it.ownGoals.value,
        )
    }.toSet()
)

fun RankingDTO.Period.toDomain() = Ranking.Period(this.from, this.to)