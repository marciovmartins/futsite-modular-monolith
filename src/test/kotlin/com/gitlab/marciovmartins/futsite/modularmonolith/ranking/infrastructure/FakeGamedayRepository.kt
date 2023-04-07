package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayRepository
import java.time.Instant
import java.util.Optional
import java.util.UUID

class FakeGamedayRepository(
    private val rows: MutableMap<UUID, Gameday> = mutableMapOf()
) : GamedayRepository {
    override fun save(entity: Gameday): Gameday {
        rows[entity.gamedayId!!] = entity
        return entity
    }

    override fun findById(id: UUID): Optional<Gameday> = Optional.ofNullable(rows[id])

    override fun findByAmateurSoccerGroupIdAndDateAfterAndDateBefore(
        amateurSoccerGroupId: UUID,
        from: Instant,
        to: Instant
    ): Set<Gameday> {
        return rows.values.filter { it.amateurSoccerGroupId == amateurSoccerGroupId }
            .filter { it.date.isAfter(from) && it.date.isBefore(to) }
            .toSet()
    }
}
