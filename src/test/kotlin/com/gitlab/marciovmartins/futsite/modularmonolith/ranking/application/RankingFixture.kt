package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO.MatchDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO.MatchDTO.PlayerDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO.MatchDTO.Team
import java.time.Instant
import java.util.UUID

object RankingFixture {
    fun gamedayToCreateDTO() = GamedayDTO(
        id = UUID.randomUUID(),
        amateurSoccerGroupId = UUID.randomUUID(),
        date = Instant.now(),
        matches = listOf(
            MatchDTO(
                players = setOf(
                    PlayerDTO(UUID.randomUUID(), Team.A, 1u, 0u, 0u, 0u, 0u),
                    PlayerDTO(UUID.randomUUID(), Team.B, 0u, 0u, 0u, 0u, 0u),
                ),
            ),
        ),
    )
}
