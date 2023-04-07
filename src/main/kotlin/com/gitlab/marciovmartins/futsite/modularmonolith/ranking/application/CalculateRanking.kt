package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroup
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking
import java.util.UUID

class CalculateRanking(
    private val gamedayRepository: GamedayRepository
) {
    fun with(amateurSoccerGroupId: UUID, period: RankingDTO.Period): RankingDTO {
        val gamedays = gamedayRepository.findByAmateurSoccerGroupIdAndDateAfterAndDateBefore(
            amateurSoccerGroupId, period.from, period.to,
        )

        val ranking = Ranking.calculatePlayerStatistics(
            amateurSoccerGroupId = AmateurSoccerGroup.AmateurSoccerGroupId(amateurSoccerGroupId),
            period = period.toDomain(),
            gamedays = gamedays
        )

        return ranking.toDTO()
    }
}
