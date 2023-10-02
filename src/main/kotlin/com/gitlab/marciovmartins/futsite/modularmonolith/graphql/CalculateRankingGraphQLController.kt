package com.gitlab.marciovmartins.futsite.modularmonolith.graphql

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.CalculateRanking
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.RankingDTO
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class CalculateRankingGraphQLController(
    private val calculateRanking: CalculateRanking,
) {
    @QueryMapping
    fun calculateRanking(
        @Argument amateurSoccerGroupId: UUID,
        @Argument period: RankingDTO.Period,
    ): RankingGraphQL {
        val rankingDTO = calculateRanking.with(amateurSoccerGroupId, period)

        val links = mutableListOf(
            "self",
            "get-amateur-soccer-group",
        )
        links += rankingDTO.playerStatistics.map { it.playerId }.map {
            "get-player-$it"
        }
        links += rankingDTO.playerStatistics.map { it.playerId }.map {
            "get-player-user-data-$it"
        }

        return RankingGraphQL(
            content = rankingDTO.toGraphQL(),
            actions = links,
        )
    }

    private fun RankingDTO.toGraphQL() = RankingContentGraphQL(
        amateurSoccerGroupId = this.amateurSoccerGroupId,
        period = PeriodGraphQL(from = this.period.from.toString(), to = this.period.to.toString()),
        matches = this.matches,
        playerStatistics = this.playerStatistics.map { it.toGraphQL() }
    )

    private fun RankingDTO.PlayerStatistic.toGraphQL() = PlayerStatisticGraphQL(
        playerId = this.playerId,
        matches = this.matches,
        victories = this.victories,
        draws = this.draws,
        defeats = this.defeats,
        goalsInFavor = this.goalsInFavor,
        ownGoals = this.ownGoals,
    )

    data class RankingGraphQL(
        val content: RankingContentGraphQL,
        val actions: List<String>,
    )

    data class RankingContentGraphQL(
        val amateurSoccerGroupId: UUID,
        val period: PeriodGraphQL,
        val matches: UShort,
        val playerStatistics: List<PlayerStatisticGraphQL>,
    )

    data class PeriodGraphQL(
        val from: String,
        val to: String,
    )

    data class PlayerStatisticGraphQL(
        val playerId: UUID,
        val matches: UShort,
        val victories: UShort,
        val draws: UShort,
        val defeats: UShort,
        val goalsInFavor: UShort,
        val ownGoals: UShort,
    )
}
