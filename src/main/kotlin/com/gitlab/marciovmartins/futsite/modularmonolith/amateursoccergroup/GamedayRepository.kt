package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface GamedayRepository : JpaRepository<Gameday, UUID> {
    fun findByAmateurSoccerGroupId(
        amateurSoccerGroupId: UUID,
        pageable: Pageable?,
    ): Page<Gameday>

    fun findByAmateurSoccerGroupIdAndDateAfterAndDateBefore(
        amateurSoccerGroupId: UUID,
        from: Instant,
        to: Instant
    ): Set<Gameday>

    fun findByAmateurSoccerGroupIdAndGamedayId(
        amateurSoccerGroupId: UUID,
        gamedayId: UUID,
    ): Gameday?
}
