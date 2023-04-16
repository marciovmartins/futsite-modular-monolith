package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.argumentsprovider

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.PlayerId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.function.Function
import java.util.stream.Stream
import kotlin.random.Random
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday as ExternalGameday

object RetrieveGamedaysSuccessfullyArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            testDescription = "find zero gamedays by amateur soccer group id and period",
        ) { amateurSoccerGroupId: AmateurSoccerGroupId ->
            val playerId1 = UUID.randomUUID()
            val playerId2 = UUID.randomUUID()
            arguments(
                gamedaysToPersist = setOf(
                    gamedayBeforePeriod(amateurSoccerGroupId, playerId1, playerId2),
                    gamedayBeforePeriod(amateurSoccerGroupId, playerId1, playerId2),
                ),
                expectedGamedays = emptySet(),
            )
        },
        argument(
            testDescription = "find one gameday by amateur soccer group id and period",
        ) { amateurSoccerGroupId: AmateurSoccerGroupId ->
            val playerId1 = UUID.randomUUID()
            val playerId2 = UUID.randomUUID()
            val date1 = Instant.now().minus(2, ChronoUnit.DAYS)
            arguments(
                gamedaysToPersist = setOf(
                    gamedayBeforePeriod(amateurSoccerGroupId, playerId1, playerId2),
                    gameday(
                        amateurSoccerGroupId,
                        date1,
                        TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 5u, ownGoals = 4u),
                        TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 4u, ownGoals = 3u),
                    ),
                    gamedayAfterPeriod(amateurSoccerGroupId, playerId1, playerId2),
                ),
                expectedGamedays = setOf(
                    expectedGameday(
                        amateurSoccerGroupId,
                        date1,
                        TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 5u, ownGoals = 4u),
                        TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 4u, ownGoals = 3u),
                    )

                ),
            )
        },
        argument(
            testDescription = "find many gamedays by amateur soccer group id and period",
        ) { amateurSoccerGroupId: AmateurSoccerGroupId ->
            val playerId1 = UUID.randomUUID()
            val playerId2 = UUID.randomUUID()
            val date1 = Instant.now().minus(4, ChronoUnit.DAYS)
            val date2 = Instant.now().minus(3, ChronoUnit.DAYS)
            val date3 = Instant.now().minus(2, ChronoUnit.DAYS)
            arguments(
                gamedaysToPersist = setOf(
                    gamedayBeforePeriod(amateurSoccerGroupId, playerId1, playerId2),
                    gameday(
                        amateurSoccerGroupId,
                        date1,
                        TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 5u, ownGoals = 4u),
                        TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 4u, ownGoals = 3u),
                    ),
                    gameday(
                        amateurSoccerGroupId,
                        date2,
                        TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 6u, ownGoals = 5u),
                        TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 5u, ownGoals = 4u),
                    ),
                    gameday(
                        amateurSoccerGroupId,
                        date3,
                        TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 7u, ownGoals = 6u),
                        TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 6u, ownGoals = 5u),
                    ),
                    gamedayAfterPeriod(amateurSoccerGroupId, playerId1, playerId2),
                ),
                expectedGamedays = setOf(
                    expectedGameday(
                        amateurSoccerGroupId,
                        date1,
                        TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 5u, ownGoals = 4u),
                        TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 4u, ownGoals = 3u),
                    ),
                    expectedGameday(
                        amateurSoccerGroupId,
                        date2,
                        TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 6u, ownGoals = 5u),
                        TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 5u, ownGoals = 4u),
                    ),
                    expectedGameday(
                        amateurSoccerGroupId,
                        date3,
                        TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 7u, ownGoals = 6u),
                        TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 6u, ownGoals = 5u),
                    ),
                ),
            )
        },
    )

    private fun argument(
        testDescription: String,
        amateurSoccerGroupId: AmateurSoccerGroupId = AmateurSoccerGroupId(UUID.randomUUID()),
        period: Ranking.Period = Ranking.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now().minus(1, ChronoUnit.DAYS),
        ),
        fn: Function<AmateurSoccerGroupId, Pair<Set<ExternalGameday>, Set<Gameday>>>
    ): Arguments {
        val (gamedays, expectedRankingDTO) = fn.apply(amateurSoccerGroupId)
        return Arguments.of(testDescription, amateurSoccerGroupId, period, gamedays, expectedRankingDTO)
    }

    private fun arguments(
        gamedaysToPersist: Set<ExternalGameday>,
        expectedGamedays: Set<Gameday>,
    ) = Pair(gamedaysToPersist, expectedGamedays)

    private fun gameday(
        amateurSoccerGroupId: AmateurSoccerGroupId,
        date: Instant,
        vararg players: TestPlayerStatistic,
    ) = ExternalGameday(
        gamedayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId.value,
        date = date,
        matches = listOf(
            ExternalGameday.Match(
                matchId = Random.nextLong(1, 99999999),
                players = players.map {
                    ExternalGameday.Match.PlayerStatistic(
                        playerStatisticId = Random.nextLong(1, 99999999),
                        playerId = it.playerId,
                        team = ExternalGameday.Match.Team.valueOf(it.team),
                        goalsInFavor = it.goalsInFavor,
                        ownGoals = it.ownGoals,
                        yellowCards = 0u,
                        blueCards = 0u,
                        redCards = 0u,
                    )
                }.toSet()
            )
        )
    )

    private fun gamedayBeforePeriod(
        amateurSoccerGroupId: AmateurSoccerGroupId,
        playerId1: UUID,
        playerId2: UUID,
    ) = gameday(
        amateurSoccerGroupId,
        Instant.now().minus(8, ChronoUnit.DAYS),
        TestPlayerStatistic(playerId1, team = "A"),
        TestPlayerStatistic(playerId2, team = "B"),
    )

    private fun gamedayAfterPeriod(
        amateurSoccerGroupId: AmateurSoccerGroupId,
        playerId1: UUID,
        playerId2: UUID,
    ) = gameday(
        amateurSoccerGroupId,
        Instant.now().minus(0, ChronoUnit.DAYS),
        TestPlayerStatistic(playerId1, team = "A"),
        TestPlayerStatistic(playerId2, team = "B"),
    )

    private fun expectedGameday(
        amateurSoccerGroupId: AmateurSoccerGroupId,
        date: Instant,
        vararg players: TestPlayerStatistic,
    ) = Gameday(
        amateurSoccerGroupId = amateurSoccerGroupId,
        date = Gameday.Date(date),
        matches = listOf(
            Gameday.Match(
                players = players.map {
                    Gameday.Match.PlayerStatistic(
                        playerId = PlayerId(it.playerId),
                        team = Gameday.Match.Team.valueOf(it.team),
                        goalsInFavor = it.goalsInFavor,
                        ownGoals = it.ownGoals,
                    )
                }.toSet()
            )
        ),
    )

    private data class TestPlayerStatistic(
        val playerId: UUID,
        val team: String,
        val goalsInFavor: UByte = 0u,
        val ownGoals: UByte = 0u,
    )
}