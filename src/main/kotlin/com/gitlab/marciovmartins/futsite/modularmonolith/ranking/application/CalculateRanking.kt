package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CalculateRanking(
    private val gamedayRepository: GamedayRepository,
) {
    fun with(amateurSoccerGroupId: UUID, period: RankingDTO.Period): RankingDTO {
        val gamedays = gamedayRepository.findBy(
            amateurSoccerGroupId.toDomain(), period.toDomain(),
        )

        val ranking = Ranking.calculatePlayerStatistics(
            amateurSoccerGroupId = amateurSoccerGroupId.toDomain(),
            period = period.toDomain(),
            gamedays = gamedays
        )

        return ranking.toDTO()
    }

    private fun UUID.toDomain() = AmateurSoccerGroupId(this)
}
