package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking

class FakeGamedayRepository(
    private val rows: Set<Gameday>
) : GamedayRepository {
    override fun findBy(amateurSoccerGroupId: AmateurSoccerGroupId, period: Ranking.Period): Set<Gameday> = rows
        .filter { it.date.value.isAfter(period.from) && it.date.value.isBefore(period.to) }
        .toSet()
}
