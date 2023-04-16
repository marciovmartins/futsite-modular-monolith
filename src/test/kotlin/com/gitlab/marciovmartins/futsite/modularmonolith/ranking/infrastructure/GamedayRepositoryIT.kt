package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroup
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.PlayerId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GamedayRepositoryIT(
    @Autowired private val jpaAmateurSoccerGroup: com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroupRepository,
    @Autowired private val jpaGamedayRepository: com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayRepository
) {
    @Test
    fun `find zero gamedays by amateur soccer group id and period`() {
        // given
        val amateurSoccerGroupId = AmateurSoccerGroupId(UUID.randomUUID())
        val period = Ranking.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now().minus(1, ChronoUnit.DAYS),
        )
        val playerId1 = UUID.randomUUID()
        val playerId2 = UUID.randomUUID()

        val amateurSoccerGroup = AmateurSoccerGroup(
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            name = "amateur-soccer-group-id ${Random.nextLong()}"
        )
        jpaAmateurSoccerGroup.save(amateurSoccerGroup)

        val date1 = Instant.now().minus(8, ChronoUnit.DAYS)
        val date2 = Instant.now().minus(0, ChronoUnit.DAYS)
        val gamedaysToPersist = listOf(
            gameday(
                amateurSoccerGroupId,
                date1,
                TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 0u, ownGoals = 0u),
                TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 0u, ownGoals = 0u),
            ),
            gameday(
                amateurSoccerGroupId,
                date2,
                TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 0u, ownGoals = 0u),
                TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 0u, ownGoals = 0u),
            ),
        )
        jpaGamedayRepository.saveAll(gamedaysToPersist)

        val gamedayRepository = GamedayBoundedContextGamedayRepository(jpaGamedayRepository)

        // when
        val gamedays = gamedayRepository.findBy(amateurSoccerGroupId, period)

        // then
        assertThat(gamedays).isEmpty()
    }

    @Test
    fun `find one gameday by amateur soccer group id and period`() {
        // given
        val amateurSoccerGroupId = AmateurSoccerGroupId(UUID.randomUUID())
        val period = Ranking.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now().minus(1, ChronoUnit.DAYS),
        )
        val playerId1 = UUID.randomUUID()
        val playerId2 = UUID.randomUUID()

        val amateurSoccerGroup = AmateurSoccerGroup(
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            name = "amateur-soccer-group-id ${Random.nextLong()}"
        )
        jpaAmateurSoccerGroup.save(amateurSoccerGroup)

        val date0 = Instant.now().minus(8, ChronoUnit.DAYS)
        val date1 = Instant.now().minus(2, ChronoUnit.DAYS)
        val date2 = Instant.now().minus(0, ChronoUnit.DAYS)
        val gamedaysToPersist = listOf(
            gameday(
                amateurSoccerGroupId,
                date0,
                TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 0u, ownGoals = 0u),
                TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 0u, ownGoals = 0u),
            ),
            gameday(
                amateurSoccerGroupId,
                date1,
                TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 5u, ownGoals = 4u),
                TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 4u, ownGoals = 3u),
            ),
            gameday(
                amateurSoccerGroupId,
                date2,
                TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 0u, ownGoals = 0u),
                TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 0u, ownGoals = 0u),
            ),
        )
        jpaGamedayRepository.saveAll(gamedaysToPersist)

        val gamedayRepository = GamedayBoundedContextGamedayRepository(jpaGamedayRepository)

        val expectedGamedays = setOf(
            expectedGameday(
                amateurSoccerGroupId,
                date1,
                TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 5u, ownGoals = 4u),
                TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 4u, ownGoals = 3u),
            )
        )

        // when
        val gamedays = gamedayRepository.findBy(amateurSoccerGroupId, period)

        // then
        assertThat(gamedays).hasSize(1)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedGamedays)
    }

    @Test
    fun `find many gamedays by amateur soccer group id and period`() {
        // given
        val amateurSoccerGroupId = AmateurSoccerGroupId(UUID.randomUUID())
        val period = Ranking.Period(
            from = Instant.now().minus(7, ChronoUnit.DAYS),
            to = Instant.now().minus(1, ChronoUnit.DAYS),
        )
        val playerId1 = UUID.randomUUID()
        val playerId2 = UUID.randomUUID()

        val amateurSoccerGroup = AmateurSoccerGroup(
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            name = "amateur-soccer-group-id ${Random.nextLong()}"
        )
        jpaAmateurSoccerGroup.save(amateurSoccerGroup)

        val date0 = Instant.now().minus(8, ChronoUnit.DAYS)
        val date1 = Instant.now().minus(4, ChronoUnit.DAYS)
        val date2 = Instant.now().minus(3, ChronoUnit.DAYS)
        val date3 = Instant.now().minus(2, ChronoUnit.DAYS)
        val date4 = Instant.now().minus(1, ChronoUnit.DAYS)
        val gamedaysToPersist = listOf(
            gameday(
                amateurSoccerGroupId,
                date0,
                TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 0u, ownGoals = 0u),
                TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 0u, ownGoals = 0u),
            ),
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
            gameday(
                amateurSoccerGroupId,
                date4,
                TestPlayerStatistic(playerId1, team = "A", goalsInFavor = 0u, ownGoals = 0u),
                TestPlayerStatistic(playerId2, team = "B", goalsInFavor = 0u, ownGoals = 0u),
            ),
        )
        jpaGamedayRepository.saveAll(gamedaysToPersist)

        val gamedayRepository = GamedayBoundedContextGamedayRepository(jpaGamedayRepository)

        val expectedGamedays = setOf(
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
        )

        // when
        val gamedays = gamedayRepository.findBy(amateurSoccerGroupId, period)

        // then
        assertThat(gamedays).hasSize(3)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedGamedays)
    }

    private fun gameday(
        amateurSoccerGroupId: AmateurSoccerGroupId,
        date: Instant,
        vararg players: TestPlayerStatistic,
    ) = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday(
        gamedayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId.value,
        date = date,
        matches = listOf(
            com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match(
                matchId = Random.nextLong(1, 99999999),
                players = players.map {
                    com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                        playerStatisticId = Random.nextLong(1, 99999999),
                        playerId = it.playerId,
                        team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.valueOf(it.team),
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
        val goalsInFavor: UByte,
        val ownGoals: UByte,
    )
}