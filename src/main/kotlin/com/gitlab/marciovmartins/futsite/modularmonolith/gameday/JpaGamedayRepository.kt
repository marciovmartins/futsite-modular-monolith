package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional
import java.util.UUID

@Repository
interface JpaGamedayRepository : JpaRepository<Gameday, UUID>, GamedayRepository

interface GamedayRepository {
    fun save(entity: Gameday): Gameday
    fun findById(id: UUID): Optional<Gameday>
    fun findByAmateurSoccerGroupIdAndDateAfterAndDateBefore(
        amateurSoccerGroupId: UUID,
        from: Instant,
        to: Instant
    ): Set<Gameday>
}
