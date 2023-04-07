package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.PlayerId
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.FakeGamedayRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID


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
                        Gameday.Match.PlayerStatistic(
                            playerStatisticId = 2,
                            playerId = playerId1,
                            team = Gameday.Match.Team.A,
                            goalsInFavor = 1u,
                            goalsAgainst = 0u,
                            yellowCards = 0u,
                            blueCards = 0u,
                            redCards = 0u,
                        ),
                        Gameday.Match.PlayerStatistic(
                            playerStatisticId = 3,
                            playerId = playerId2,
                            team = Gameday.Match.Team.B,
                            goalsInFavor = 0u,
                            goalsAgainst = 0u,
                            yellowCards = 0u,
                            blueCards = 0u,
                            redCards = 0u,
                        ),
                    ),
                )
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
        TODO("need to be implemented")
    }

    @Test
    fun `with many gameday with many matches registered`() {
        TODO("need to be implemented")
    }
}