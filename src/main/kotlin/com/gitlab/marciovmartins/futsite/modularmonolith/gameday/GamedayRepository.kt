package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.webmvc.RepositoryRestController
import java.time.Instant
import java.util.UUID

@RepositoryRestController
@RepositoryRestResource(path = "gameDays")
interface JpaGamedayRepository : JpaRepository<Gameday, UUID>, GamedayRepository {
}

interface GamedayRepository {
    fun save(entity: Gameday): Gameday
    fun findByAmateurSoccerGroupIdAndDateAfterAndDateBefore(
        amateurSoccerGroupId: UUID,
        from: Instant,
        to: Instant
    ): Set<Gameday>
}
