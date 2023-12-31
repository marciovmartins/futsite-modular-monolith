package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.argumentsprovider

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.RankingDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.PlayerId
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.function.BiFunction
import java.util.stream.Stream

object GamedaySuccessfullyArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            testDescription = "without any gameday registered",
        ) { amateurSoccerGroupId: UUID, period: RankingDTO.Period ->
            result(
                gamedays = setOf(),
                expectedRankingDTO = RankingDTO(
                    amateurSoccerGroupId, period, matches = 0U, playerStatistics = emptySet()
                )
            )
        },
        argument(
            testDescription = "with one gameday with one match registered",
        ) { amateurSoccerGroupId: UUID, period: RankingDTO.Period ->
            val playerId1 = PlayerId(UUID.randomUUID())
            val playerId2 = PlayerId(UUID.randomUUID())
            result(
                gamedays = setOf(
                    Gameday(
                        amateurSoccerGroupId = AmateurSoccerGroupId(amateurSoccerGroupId),
                        date = Gameday.Date(Instant.now().minus(1, ChronoUnit.DAYS)),
                        matches = listOf(
                            match(
                                playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                                playerStatistic(playerId2, Gameday.Match.Team.B),
                            ),
                        ),
                    ),
                ),
                expectedRankingDTO = RankingDTO(
                    amateurSoccerGroupId = amateurSoccerGroupId,
                    period = period,
                    matches = 1u,
                    playerStatistics = setOf(
                        expectedPlayerStatistic(playerId1, victories = 1u, goalsInFavor = 1u),
                        expectedPlayerStatistic(playerId2, defeats = 1u),
                    ),
                )
            )
        },
        argument(
            testDescription = "with one gameday with many matches registered",
        ) { amateurSoccerGroupId: UUID, period: RankingDTO.Period ->
            val playerId1 = PlayerId(UUID.randomUUID())
            val playerId2 = PlayerId(UUID.randomUUID())
            result(
                gamedays = setOf(
                    Gameday(
                        amateurSoccerGroupId = AmateurSoccerGroupId(amateurSoccerGroupId),
                        date = Gameday.Date(Instant.now().minus(2, ChronoUnit.DAYS)),
                        matches = listOf(
                            match(
                                playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                                playerStatistic(playerId2, Gameday.Match.Team.B),
                            ),
                            match(
                                playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 2u),
                                playerStatistic(playerId2, Gameday.Match.Team.B),
                            ),
                        ),
                    ),
                ),
                expectedRankingDTO = RankingDTO(
                    amateurSoccerGroupId = amateurSoccerGroupId,
                    period = period,
                    matches = 2u,
                    playerStatistics = setOf(
                        expectedPlayerStatistic(playerId1, 2u, 2u, 0u, 0u, 3u, 0u),
                        expectedPlayerStatistic(playerId2, 2u, 0u, 0u, 2u, 0u, 0u),
                    ),
                )
            )
        },
        argument(
            testDescription = "with many gameday with many matches registered",
        ) { amateurSoccerGroupId: UUID, period: RankingDTO.Period ->
            val playerId1 = PlayerId(UUID.randomUUID())
            val playerId2 = PlayerId(UUID.randomUUID())
            result(
                gamedays = setOf(
                    Gameday(
                        amateurSoccerGroupId = AmateurSoccerGroupId(amateurSoccerGroupId),
                        date = Gameday.Date(Instant.now().minus(2, ChronoUnit.DAYS)),
                        matches = listOf(
                            match(
                                playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                                playerStatistic(playerId2, Gameday.Match.Team.B),
                            ),
                            match(
                                playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 2u),
                                playerStatistic(playerId2, Gameday.Match.Team.B),
                            ),
                        ),
                    ),
                    Gameday(
                        amateurSoccerGroupId = AmateurSoccerGroupId(amateurSoccerGroupId),
                        date = Gameday.Date(Instant.now().minus(1, ChronoUnit.DAYS)),
                        matches = listOf(
                            match(
                                playerStatistic(playerId1, Gameday.Match.Team.A),
                                playerStatistic(playerId2, Gameday.Match.Team.B),
                            ),
                            match(
                                playerStatistic(playerId1, Gameday.Match.Team.A),
                                playerStatistic(playerId2, Gameday.Match.Team.B, goalsInFavor = 1u),
                            ),
                        ),
                    )
                ),
                expectedRankingDTO = RankingDTO(
                    amateurSoccerGroupId = amateurSoccerGroupId,
                    period = period,
                    matches = 4u,
                    playerStatistics = setOf(
                        expectedPlayerStatistic(playerId1, 4u, 2u, 1u, 1u, 3u, 0u),
                        expectedPlayerStatistic(playerId2, 4u, 1u, 1u, 2u, 1u, 0u),
                    ),
                )
            )
        },
    )

    private fun argument(
        testDescription: String,
        amateurSoccerGroupId: UUID = UUID.randomUUID(),
        period: RankingDTO.Period = RankingDTO.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now()
        ),
        fn: BiFunction<UUID, RankingDTO.Period, Pair<Set<Gameday>, RankingDTO>>
    ): Arguments {
        val (gamedays, expectedRankingDTO) = fn.apply(amateurSoccerGroupId, period)
        return Arguments.of(testDescription, amateurSoccerGroupId, period, gamedays, expectedRankingDTO)
    }

    private fun result(
        gamedays: Set<Gameday>,
        expectedRankingDTO: RankingDTO,
    ) = Pair(gamedays, expectedRankingDTO)

    private fun match(
        vararg players: Gameday.Match.PlayerStatistic,
    ) = Gameday.Match(players.toSet())

    private fun playerStatistic(
        playerId: PlayerId,
        team: Gameday.Match.Team,
        goalsInFavor: UByte = 0u,
        ownGoals: UByte = 0u,
    ) = Gameday.Match.PlayerStatistic(
        playerId, team, goalsInFavor, ownGoals,
    )

    private fun expectedPlayerStatistic(
        playerId: PlayerId,
        matches: UShort = 1u,
        victories: UShort = 0u,
        draws: UShort = 0u,
        defeats: UShort = 0u,
        goalsInFavor: UShort = 0u,
        ownGoals: UShort = 0u,
    ) = RankingDTO.PlayerStatistic(
        playerId.value, matches, victories, draws, defeats, goalsInFavor, ownGoals,
    )
}
