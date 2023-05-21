package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PlayerRepository : JpaRepository<Player, UUID>, CustomizedPlayerRepository

interface CustomizedPlayerRepository {
    fun findByAmateurSoccerGroupId(
        amateurSoccerGroupId: UUID
    ): Set<Player>
}