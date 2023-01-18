package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository

class FakeGamedayRepository : GamedayRepository {
    private val rows = mutableMapOf<Gameday.GamedayId, Gameday>()

    override fun persist(gameday: Gameday) {
        rows[gameday.id] = gameday
    }

    override fun findBy(gamedayId: Gameday.GamedayId): Gameday {
        return rows[gamedayId]!!
    }
}
