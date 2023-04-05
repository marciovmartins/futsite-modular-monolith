package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayRepository
import java.util.UUID

class CalculateRanking(
    private val gamedayRepository: GamedayRepository
) {
    fun with(amateurSoccerGroupId: UUID, period: RankingDTO.Period): RankingDTO {
        return RankingDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            period = period,
            playerStatistics = emptySet(),
        )
    }
}
