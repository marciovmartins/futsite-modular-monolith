package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain

interface GamedayRepository {
    fun findBy(amateurSoccerGroupId: AmateurSoccerGroupId, period: Ranking.Period): Set<Gameday>
}