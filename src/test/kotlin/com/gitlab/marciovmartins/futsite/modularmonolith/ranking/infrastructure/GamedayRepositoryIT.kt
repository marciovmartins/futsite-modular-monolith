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
            to = Instant.now(),
        )

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
            to = Instant.now(),
        )

        val amateurSoccerGroup = AmateurSoccerGroup(
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            name = "amateur-soccer-group-id ${Random.nextLong()}"
        )
        jpaAmateurSoccerGroup.save(amateurSoccerGroup)

        val date = Instant.now().minus(1, ChronoUnit.DAYS)
        val playerId1 = UUID.randomUUID()
        val playerId2 = UUID.randomUUID()
        val gameday = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            date = date,
            matches = listOf(
                com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match(
                    matchId = Random.nextLong(1, 99999999),
                    players = setOf(
                        com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                            playerStatisticId = Random.nextLong(1, 99999999),
                            playerId = playerId1,
                            team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.A,
                            goalsInFavor = 5u,
                            ownGoals = 4u,
                            yellowCards = 3u,
                            blueCards = 2u,
                            redCards = 1u,
                        ),
                        com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                            playerStatisticId = Random.nextLong(1, 99999999),
                            playerId = playerId2,
                            team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.B,
                            goalsInFavor = 4u,
                            ownGoals = 3u,
                            yellowCards = 2u,
                            blueCards = 1u,
                            redCards = 0u,
                        ),
                    ),
                )
            )
        )
        jpaGamedayRepository.save(gameday)

        val gamedayRepository = GamedayBoundedContextGamedayRepository(jpaGamedayRepository)

        val expectedGamedays = setOf(
            Gameday(
                amateurSoccerGroupId = amateurSoccerGroupId,
                date = Gameday.Date(date),
                matches = listOf(
                    Gameday.Match(
                        players = setOf(
                            Gameday.Match.PlayerStatistic(
                                playerId = PlayerId(playerId1),
                                team = Gameday.Match.Team.A,
                                goalsInFavor = 5u,
                                ownGoals = 4u,
                            ),
                            Gameday.Match.PlayerStatistic(
                                playerId = PlayerId(playerId2),
                                team = Gameday.Match.Team.B,
                                goalsInFavor = 4u,
                                ownGoals = 3u,
                            ),
                        )
                    )
                ),
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
            to = Instant.now(),
        )
        val playerId1 = UUID.randomUUID()
        val playerId2 = UUID.randomUUID()

        val amateurSoccerGroup = AmateurSoccerGroup(
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            name = "amateur-soccer-group-id ${Random.nextLong()}"
        )
        jpaAmateurSoccerGroup.save(amateurSoccerGroup)

        val date1 = Instant.now().minus(3, ChronoUnit.DAYS)
        val gameday1 = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            date = date1,
            matches = listOf(
                com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match(
                    matchId = Random.nextLong(1, 99999999),
                    players = setOf(
                        com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                            playerStatisticId = Random.nextLong(1, 99999999),
                            playerId = playerId1,
                            team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.A,
                            goalsInFavor = 5u,
                            ownGoals = 4u,
                            yellowCards = 3u,
                            blueCards = 2u,
                            redCards = 1u,
                        ),
                        com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                            playerStatisticId = Random.nextLong(1, 99999999),
                            playerId = playerId2,
                            team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.B,
                            goalsInFavor = 4u,
                            ownGoals = 3u,
                            yellowCards = 2u,
                            blueCards = 1u,
                            redCards = 0u,
                        ),
                    ),
                )
            )
        )
        jpaGamedayRepository.save(gameday1)

        val date2 = Instant.now().minus(2, ChronoUnit.DAYS)
        val gameday2 = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            date = date2,
            matches = listOf(
                com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match(
                    matchId = Random.nextLong(1, 99999999),
                    players = setOf(
                        com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                            playerStatisticId = Random.nextLong(1, 99999999),
                            playerId = playerId1,
                            team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.A,
                            goalsInFavor = 6u,
                            ownGoals = 5u,
                            yellowCards = 2u,
                            blueCards = 1u,
                            redCards = 0u,
                        ),
                        com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                            playerStatisticId = Random.nextLong(1, 99999999),
                            playerId = playerId2,
                            team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.B,
                            goalsInFavor = 5u,
                            ownGoals = 4u,
                            yellowCards = 2u,
                            blueCards = 1u,
                            redCards = 0u,
                        ),
                    ),
                )
            )
        )
        jpaGamedayRepository.save(gameday2)

        val date3 = Instant.now().minus(1, ChronoUnit.DAYS)
        val gameday3 = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday(
            gamedayId = UUID.randomUUID(),
            amateurSoccerGroupId = amateurSoccerGroupId.value,
            date = date3,
            matches = listOf(
                com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match(
                    matchId = Random.nextLong(1, 99999999),
                    players = setOf(
                        com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                            playerStatisticId = Random.nextLong(1, 99999999),
                            playerId = playerId1,
                            team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.A,
                            goalsInFavor = 7u,
                            ownGoals = 6u,
                            yellowCards = 2u,
                            blueCards = 1u,
                            redCards = 0u,
                        ),
                        com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic(
                            playerStatisticId = Random.nextLong(1, 99999999),
                            playerId = playerId2,
                            team = com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.Team.B,
                            goalsInFavor = 6u,
                            ownGoals = 5u,
                            yellowCards = 2u,
                            blueCards = 1u,
                            redCards = 0u,
                        ),
                    ),
                )
            )
        )
        jpaGamedayRepository.save(gameday3)

        val gamedayRepository = GamedayBoundedContextGamedayRepository(jpaGamedayRepository)

        val expectedGamedays = setOf(
            Gameday(
                amateurSoccerGroupId = amateurSoccerGroupId,
                date = Gameday.Date(date1),
                matches = listOf(
                    Gameday.Match(
                        players = setOf(
                            Gameday.Match.PlayerStatistic(
                                playerId = PlayerId(playerId1),
                                team = Gameday.Match.Team.A,
                                goalsInFavor = 5u,
                                ownGoals = 4u,
                            ),
                            Gameday.Match.PlayerStatistic(
                                playerId = PlayerId(playerId2),
                                team = Gameday.Match.Team.B,
                                goalsInFavor = 4u,
                                ownGoals = 3u,
                            ),
                        )
                    )
                ),
            ),
            Gameday(
                amateurSoccerGroupId = amateurSoccerGroupId,
                date = Gameday.Date(date2),
                matches = listOf(
                    Gameday.Match(
                        players = setOf(
                            Gameday.Match.PlayerStatistic(
                                playerId = PlayerId(playerId1),
                                team = Gameday.Match.Team.A,
                                goalsInFavor = 6u,
                                ownGoals = 5u,
                            ),
                            Gameday.Match.PlayerStatistic(
                                playerId = PlayerId(playerId2),
                                team = Gameday.Match.Team.B,
                                goalsInFavor = 5u,
                                ownGoals = 4u,
                            ),
                        )
                    )
                ),
            ),
            Gameday(
                amateurSoccerGroupId = amateurSoccerGroupId,
                date = Gameday.Date(date3),
                matches = listOf(
                    Gameday.Match(
                        players = setOf(
                            Gameday.Match.PlayerStatistic(
                                playerId = PlayerId(playerId1),
                                team = Gameday.Match.Team.A,
                                goalsInFavor = 7u,
                                ownGoals = 6u,
                            ),
                            Gameday.Match.PlayerStatistic(
                                playerId = PlayerId(playerId2),
                                team = Gameday.Match.Team.B,
                                goalsInFavor = 6u,
                                ownGoals = 5u,
                            ),
                        )
                    )
                ),
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
}