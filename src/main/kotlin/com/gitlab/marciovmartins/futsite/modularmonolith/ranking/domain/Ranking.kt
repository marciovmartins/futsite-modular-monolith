package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroup
import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday
import java.time.Instant

class Ranking private constructor(
    val amateurSoccerGroupId: AmateurSoccerGroup.AmateurSoccerGroupId,
    val period: Period,
    val matches: Matches,
    val playerStatistics: Set<PlayerStatistic>
) {
    companion object {
        fun calculatePlayerStatistics(
            amateurSoccerGroupId: AmateurSoccerGroup.AmateurSoccerGroupId,
            period: Period,
            gamedays: Set<Gameday>,
        ): Ranking {
            val matches = gamedays.flatMap { it.matches }.count()

            val playerStatistics = mutableMapOf<PlayerId, PlayerStatistic>()
            gamedays.flatMap { it.matches }.forEach { match ->
                val teams = match.players
                    .groupBy { it.team }
                    .mapValues {
                        it.value.fold(TeamStatistic.zero()) { teamStatistic, playerStatistic ->
                            teamStatistic.add(playerStatistic)
                        }
                    }

                val teamA = teams[Gameday.Match.Team.A]!!
                val teamB = teams[Gameday.Match.Team.B]!!

                val goalsTeamA = teamA.goalsInFavor.value.plus(teamB.ownGoals.value).toUShort()
                val goalsTeamB = teamB.goalsInFavor.value.plus(teamA.ownGoals.value).toUShort()

                val teamWinner: Gameday.Match.Team? = when (goalsTeamA.compareTo(goalsTeamB)) {
                    0 -> null
                    1 -> Gameday.Match.Team.A
                    else -> Gameday.Match.Team.B
                }

                match.players.forEach { playerStatistic ->
                    val playerId = PlayerId(playerStatistic.playerId)
                    val statistic = playerStatistics.getOrDefault(playerId, PlayerStatistic.zero(playerId))
                    val newStatistic: PlayerStatistic = when (teamWinner) {
                        null -> statistic.draw(playerStatistic)
                        playerStatistic.team -> statistic.victory(playerStatistic)
                        else -> statistic.defeat(playerStatistic)
                    }
                    playerStatistics[playerId] = newStatistic
                }
            }
            return Ranking(
                amateurSoccerGroupId = amateurSoccerGroupId,
                period = period,
                matches = Matches(matches.toUShort()),
                playerStatistics = playerStatistics.values.toSet(),
            )
        }
    }

    data class Period(val from: Instant, val to: Instant)
    data class Matches(val value: UShort)
    data class PlayerStatistic(
        val playerId: PlayerId,
        val matches: Matches,
        val victories: Victories,
        val draws: Draws,
        val defeats: Defeats,
        val goalsInFavor: GoalsInFavor,
        val ownGoals: OwnGoals,
    ) {
        fun draw(playerStatistic: Gameday.Match.PlayerStatistic) = PlayerStatistic(
            playerId = this.playerId,
            matches = Matches(this.matches.value.inc()),
            victories = this.victories,
            draws = Draws(this.draws.value.inc()),
            defeats = this.defeats,
            goalsInFavor = GoalsInFavor(this.goalsInFavor.value.plus(playerStatistic.goalsInFavor).toUShort()),
            ownGoals = OwnGoals(this.ownGoals.value.plus(playerStatistic.ownGoals).toUShort()),
        )

        fun victory(playerStatistic: Gameday.Match.PlayerStatistic) = PlayerStatistic(
            playerId = this.playerId,
            matches = Matches(this.matches.value.inc()),
            victories = Victories(this.victories.value.inc()),
            draws = this.draws,
            defeats = this.defeats,
            goalsInFavor = GoalsInFavor(this.goalsInFavor.value.plus(playerStatistic.goalsInFavor).toUShort()),
            ownGoals = OwnGoals(this.ownGoals.value.plus(playerStatistic.ownGoals).toUShort()),
        )

        fun defeat(playerStatistic: Gameday.Match.PlayerStatistic) = PlayerStatistic(
            playerId = this.playerId,
            matches = Matches(this.matches.value.inc()),
            victories = this.victories,
            draws = this.draws,
            defeats = Defeats(this.defeats.value.inc()),
            goalsInFavor = GoalsInFavor(this.goalsInFavor.value.plus(playerStatistic.goalsInFavor).toUShort()),
            ownGoals = OwnGoals(this.ownGoals.value.plus(playerStatistic.ownGoals).toUShort()),
        )

        companion object {
            fun zero(playerId: PlayerId) = PlayerStatistic(
                playerId, Matches(0u), Victories(0u), Draws(0u), Defeats(0u), GoalsInFavor(0u), OwnGoals(0u),
            )
        }

        data class Matches(val value: UShort)
        data class Victories(val value: UShort)
        data class Draws(val value: UShort)
        data class Defeats(val value: UShort)
        data class GoalsInFavor(val value: UShort)
        data class OwnGoals(val value: UShort)
    }

    private data class TeamStatistic(
        val goalsInFavor: GoalsInFavor,
        val ownGoals: OwnGoals,
    ) {
        fun add(playerStatistic: Gameday.Match.PlayerStatistic) = TeamStatistic(
            goalsInFavor = GoalsInFavor(this.goalsInFavor.value.plus(playerStatistic.goalsInFavor).toUShort()),
            ownGoals = OwnGoals(this.ownGoals.value.plus(playerStatistic.ownGoals).toUShort())
        )

        companion object {
            fun zero() = TeamStatistic(GoalsInFavor(0u), OwnGoals(0u))
        }

        data class GoalsInFavor(val value: UShort)
        data class OwnGoals(val value: UShort)
    }
}
