package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface GamedayRepository : JpaRepository<Gameday, UUID>, GamedayRepositoryCustomized

interface GamedayRepositoryCustomized {
    fun save(entity: Gameday): Gameday
    fun findByAmateurSoccerGroupIdAndDateAfterAndDateBefore(
        amateurSoccerGroupId: UUID,
        from: Instant,
        to: Instant
    ): Set<Gameday>
}
