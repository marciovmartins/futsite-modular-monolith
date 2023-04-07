package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.PlayerId
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.FakeGamedayRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random


/**
 * TODO: Look for some table/spreadsheet way to test several different possibilities provided by the business. CSV? JBehave?
 */
internal class CalculateRankingTest {
    @Test
    fun `without any gameday registered`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val period = RankingDTO.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now()
        )

        val expectedRanking = RankingDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            period = period,
            matches = 0U,
            playerStatistics = emptySet(),
        )

        val calculateRanking = CalculateRanking(FakeGamedayRepository())

        // when
        val actualRanking = calculateRanking.with(amateurSoccerGroupId, period)

        // then
        assertThat(actualRanking).isEqualTo(expectedRanking)
    }

    @Test
    fun `with one gameday with one match registered`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val period = RankingDTO.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now()
        )

        val playerId1 = PlayerId(UUID.randomUUID())
        val playerId2 = PlayerId(UUID.randomUUID())

        val gameday = Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId,
            date = Instant.now().minus(1, ChronoUnit.DAYS),
            matches = listOf(
                Gameday.Match(
                    matchId = 1,
                    players = setOf(
                        playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                        playerStatistic(playerId2, Gameday.Match.Team.B),
                    ),
                ),
            ),
        )

        val gamedayRepository = FakeGamedayRepository(
            mutableMapOf(
                gameday.gamedayId!! to gameday,
            )
        )

        val expectedRanking = RankingDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            period = period,
            matches = 1u,
            playerStatistics = setOf(
                RankingDTO.PlayerStatistic(
                    playerId = playerId1.value,
                    matches = 1u,
                    victories = 1u,
                    draws = 0u,
                    defeats = 0u,
                    goalsInFavor = 1u,
                    ownGoals = 0u,
                ),
                RankingDTO.PlayerStatistic(
                    playerId = playerId2.value,
                    matches = 1u,
                    victories = 0u,
                    draws = 0u,
                    defeats = 1u,
                    goalsInFavor = 0u,
                    ownGoals = 0u,
                ),
            ),
        )

        val calculateRanking = CalculateRanking(gamedayRepository)

        // when
        val actualRanking = calculateRanking.with(amateurSoccerGroupId, period)

        // then
        assertThat(actualRanking).isEqualTo(expectedRanking)
    }

    @Test
    fun `with one gameday with many matches registered`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val period = RankingDTO.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now()
        )

        val playerId1 = PlayerId(UUID.randomUUID())
        val playerId2 = PlayerId(UUID.randomUUID())

        val gameday1 = Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId,
            date = Instant.now().minus(2, ChronoUnit.DAYS),
            matches = listOf(
                Gameday.Match(
                    matchId = 1,
                    players = setOf(
                        playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                        playerStatistic(playerId2, Gameday.Match.Team.B),
                    ),
                ),
                Gameday.Match(
                    matchId = 4,
                    players = setOf(
                        playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 2u),
                        playerStatistic(playerId2, Gameday.Match.Team.B),
                    ),
                ),
            ),
        )

        val gameday2 = Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId,
            date = Instant.now().minus(1, ChronoUnit.DAYS),
            matches = listOf(
                Gameday.Match(
                    matchId = 7,
                    players = setOf(
                        playerStatistic(playerId1, Gameday.Match.Team.A),
                        playerStatistic(playerId2, Gameday.Match.Team.B),
                    ),
                ),
                Gameday.Match(
                    matchId = 10,
                    players = setOf(
                        playerStatistic(playerId1, Gameday.Match.Team.A),
                        playerStatistic(playerId2, Gameday.Match.Team.B, goalsInFavor = 1u),
                    ),
                ),
            ),
        )

        val gamedayRepository = FakeGamedayRepository(
            mutableMapOf(
                gameday1.gamedayId!! to gameday1,
                gameday2.gamedayId!! to gameday2,
            )
        )

        val expectedRanking = RankingDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            period = period,
            matches = 4u,
            playerStatistics = setOf(
                RankingDTO.PlayerStatistic(
                    playerId = playerId1.value,
                    matches = 4u,
                    victories = 2u,
                    draws = 1u,
                    defeats = 1u,
                    goalsInFavor = 3u,
                    ownGoals = 0u,
                ),
                RankingDTO.PlayerStatistic(
                    playerId = playerId2.value,
                    matches = 4u,
                    victories = 1u,
                    draws = 1u,
                    defeats = 2u,
                    goalsInFavor = 1u,
                    ownGoals = 0u,
                ),
            ),
        )

        val calculateRanking = CalculateRanking(gamedayRepository)

        // when
        val actualRanking = calculateRanking.with(amateurSoccerGroupId, period)

        // then
        assertThat(actualRanking).isEqualTo(expectedRanking)
    }

    @Test
    fun `with many gameday with many matches registered`() {
        // given
        val amateurSoccerGroupId = UUID.randomUUID()
        val period = RankingDTO.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now()
        )

        val playerId1 = PlayerId(UUID.randomUUID())
        val playerId2 = PlayerId(UUID.randomUUID())

        val gameday = Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId,
            date = Instant.now().minus(1, ChronoUnit.DAYS),
            matches = listOf(
                Gameday.Match(
                    matchId = 1,
                    players = setOf(
                        playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                        playerStatistic(playerId2, Gameday.Match.Team.B),
                    ),
                ),
                Gameday.Match(
                    matchId = 4,
                    players = setOf(
                        playerStatistic(playerId1, Gameday.Match.Team.A),
                        playerStatistic(playerId2, Gameday.Match.Team.B),
                    ),
                ),
            ),
        )

        val gamedayRepository = FakeGamedayRepository(
            mutableMapOf(
                gameday.gamedayId!! to gameday,
            )
        )

        val expectedRanking = RankingDTO(
            amateurSoccerGroupId = amateurSoccerGroupId,
            period = period,
            matches = 2u,
            playerStatistics = setOf(
                RankingDTO.PlayerStatistic(
                    playerId = playerId1.value,
                    matches = 2u,
                    victories = 1u,
                    draws = 1u,
                    defeats = 0u,
                    goalsInFavor = 1u,
                    ownGoals = 0u,
                ),
                RankingDTO.PlayerStatistic(
                    playerId = playerId2.value,
                    matches = 2u,
                    victories = 0u,
                    draws = 1u,
                    defeats = 1u,
                    goalsInFavor = 0u,
                    ownGoals = 0u,
                ),
            ),
        )

        val calculateRanking = CalculateRanking(gamedayRepository)

        // when
        val actualRanking = calculateRanking.with(amateurSoccerGroupId, period)

        // then
        assertThat(actualRanking).isEqualTo(expectedRanking)
    }

    private fun playerStatistic(
        playerId: PlayerId,
        team: Gameday.Match.Team,
        goalsInFavor: UByte = 0u,
        ownGoals: UByte = 0u,
        yellowCards: UByte = 0u,
        blueCards: UByte = 0u,
        redCards: UByte = 0u,
    ) = Gameday.Match.PlayerStatistic(
        Random.nextLong(), playerId, team, goalsInFavor, ownGoals, yellowCards, blueCards, redCards,
    )
}