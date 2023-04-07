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
                match(
                    playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                    playerStatistic(playerId2, Gameday.Match.Team.B),
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
                expectedPlayerStatistic(playerId1, victories = 1u, goalsInFavor = 1u),
                expectedPlayerStatistic(playerId2, defeats = 1u),
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
                match(
                    playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                    playerStatistic(playerId2, Gameday.Match.Team.B),
                ),
                match(
                    playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 2u),
                    playerStatistic(playerId2, Gameday.Match.Team.B),
                ),
            ),
        )

        val gameday2 = Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId,
            date = Instant.now().minus(1, ChronoUnit.DAYS),
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
                expectedPlayerStatistic(playerId1, 4u, 2u, 1u, 1u, 3u, 0u),
                expectedPlayerStatistic(playerId2, 4u, 1u, 1u, 2u, 1u, 0u),
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
                match(
                    playerStatistic(playerId1, Gameday.Match.Team.A, goalsInFavor = 1u),
                    playerStatistic(playerId2, Gameday.Match.Team.B),
                ),
                match(
                    playerStatistic(playerId1, Gameday.Match.Team.A),
                    playerStatistic(playerId2, Gameday.Match.Team.B),
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
                expectedPlayerStatistic(playerId1, 2u, 1u, 1u, 0u, 1u, 0u),
                expectedPlayerStatistic(playerId2, 2u, 0u, 1u, 1u, 0u, 0u),
            ),
        )

        val calculateRanking = CalculateRanking(gamedayRepository)

        // when
        val actualRanking = calculateRanking.with(amateurSoccerGroupId, period)

        // then
        assertThat(actualRanking).isEqualTo(expectedRanking)
    }

    private fun match(
        vararg players: Gameday.Match.PlayerStatistic,
    ) = Gameday.Match(
        Random.nextLong(), players.toSet(),
    )

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