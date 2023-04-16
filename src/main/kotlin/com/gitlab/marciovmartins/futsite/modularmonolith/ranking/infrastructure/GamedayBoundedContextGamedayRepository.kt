package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking

class GamedayBoundedContextGamedayRepository(
    private val gamedayRepository: com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayRepository
) : GamedayRepository {
    override fun findBy(amateurSoccerGroupId: AmateurSoccerGroupId, period: Ranking.Period): Set<Gameday> {
        return emptySet()
    }
}
