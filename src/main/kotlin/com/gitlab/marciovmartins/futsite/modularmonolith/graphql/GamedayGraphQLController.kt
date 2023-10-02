package com.gitlab.marciovmartins.futsite.modularmonolith.graphql

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.GamedayRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.time.Instant
import java.util.UUID

@Controller
class GamedayGraphQLController(
    private val gamedayRepository: GamedayRepository,
) {
    @MutationMapping
    fun registerGameday(
        @Argument id: UUID,
        @Argument amateurSoccerGroupId: UUID,
        @Argument date: Instant,
        @Argument matches: List<GamedayMatchGraphQL>,
    ): GamedayGraphQL {
        val gameday = Gameday().apply {
            this.gamedayId = id
            this.amateurSoccerGroupId = amateurSoccerGroupId
            this.date = date
            this.matches = matches.map { gamedayMatchGraphQL ->
                Gameday.Match().apply {
                    this.players = gamedayMatchGraphQL.players.map {
                        Gameday.Match.PlayerStatistic().apply {
                            this.playerId = it.playerId
                            this.team = Gameday.Match.Team.valueOf(it.team)
                            this.goalsInFavor = it.goalsInFavor.toUByte()
                            this.ownGoals = it.ownGoals.toUByte()
                            this.yellowCards = it.yellowCards.toUByte()
                            this.blueCards = it.blueCards.toUByte()
                            this.redCards = it.redCards.toUByte()
                        }
                    }.toSet()
                }
            }
        }

        gamedayRepository.save(gameday)

        return toGraphQL(gameday)
    }

    private fun toGraphQL(gameday: Gameday) = GamedayGraphQL(
        id = gameday.gamedayId!!,
        amateurSoccerGroupId = gameday.amateurSoccerGroupId!!,
        date = gameday.date,
        matches = gameday.matches.map { toGraphQL(it) },
        actions = listOf("self", "get-amateur-soccer-group")
    )

    private fun toGraphQL(gamedayMatch: Gameday.Match) = GamedayMatchGraphQL(
        players = gamedayMatch.players.map { toGraphQL(it) }
    )

    private fun toGraphQL(gamedayMatchPlayerStatistic: Gameday.Match.PlayerStatistic) =
        GamedayMatchPlayerStatisticGraphQL(
            playerId = gamedayMatchPlayerStatistic.playerId,
            team = gamedayMatchPlayerStatistic.team.name,
            goalsInFavor = gamedayMatchPlayerStatistic.goalsInFavor.toInt(),
            ownGoals = gamedayMatchPlayerStatistic.ownGoals.toInt(),
            yellowCards = gamedayMatchPlayerStatistic.yellowCards.toInt(),
            blueCards = gamedayMatchPlayerStatistic.blueCards.toInt(),
            redCards = gamedayMatchPlayerStatistic.redCards.toInt(),
        )

    data class GamedayGraphQL(
        val id: UUID,
        val amateurSoccerGroupId: UUID,
        val date: Instant,
        val matches: List<GamedayMatchGraphQL>,
        val actions: List<String>,
    )

    data class GamedayMatchGraphQL(
        val players: List<GamedayMatchPlayerStatisticGraphQL>,
    )

    data class GamedayMatchPlayerStatisticGraphQL(
        val playerId: UUID,
        val team: String,
        val goalsInFavor: Int,
        val ownGoals: Int,
        val yellowCards: Int,
        val blueCards: Int,
        val redCards: Int,
    )
}