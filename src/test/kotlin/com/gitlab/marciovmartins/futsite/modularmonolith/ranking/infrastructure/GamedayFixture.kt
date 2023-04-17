package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

object GamedayFixture {
    fun gameday(
        amateurSoccerGroupId: AmateurSoccerGroupId,
        date: Instant,
        vararg players: TestPlayerStatistic = arrayOf(
            TestPlayerStatistic.empty(team = "A"),
            TestPlayerStatistic.empty(team = "B"),
        ),
    ) = Gameday(
        gamedayId = UUID.randomUUID(),
        amateurSoccerGroupId = amateurSoccerGroupId.value,
        date = date,
        matches = listOf(
            Gameday.Match(
                matchId = Random.nextLong(1, 99999999),
                players = players.map {
                    Gameday.Match.PlayerStatistic(
                        playerStatisticId = Random.nextLong(1, 99999999),
                        playerId = it.playerId,
                        team = Gameday.Match.Team.valueOf(it.team),
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

    fun gamedayBeforePeriod(
        amateurSoccerGroupId: AmateurSoccerGroupId,
        playerId1: UUID = UUID.randomUUID(),
        playerId2: UUID = UUID.randomUUID(),
    ) = gameday(
        amateurSoccerGroupId,
        Instant.now().minus(8, ChronoUnit.DAYS),
        TestPlayerStatistic(playerId1, team = "A"),
        TestPlayerStatistic(playerId2, team = "B"),
    )

    fun gamedayAfterPeriod(
        amateurSoccerGroupId: AmateurSoccerGroupId,
        playerId1: UUID = UUID.randomUUID(),
        playerId2: UUID = UUID.randomUUID(),
    ) = gameday(
        amateurSoccerGroupId,
        Instant.now().minus(0, ChronoUnit.DAYS),
        TestPlayerStatistic(playerId1, team = "A"),
        TestPlayerStatistic(playerId2, team = "B"),
    )

    data class TestPlayerStatistic(
        val playerId: UUID,
        val team: String,
        val goalsInFavor: UByte = 0u,
        val ownGoals: UByte = 0u,
    ) {
        companion object {
            fun empty(team: String) = TestPlayerStatistic(UUID.randomUUID(), team)
        }
    }
}