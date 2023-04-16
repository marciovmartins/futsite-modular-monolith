package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.PlayerId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking

class GamedayBoundedContextGamedayRepository(
    private val gamedayRepository: com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayRepository
) : GamedayRepository {
    override fun findBy(amateurSoccerGroupId: AmateurSoccerGroupId, period: Ranking.Period): Set<Gameday> {
        return gamedayRepository.findByAmateurSoccerGroupIdAndDateAfterAndDateBefore(
            amateurSoccerGroupId.value, period.from, period.to,
        )
            .map { mapTo(it) }
            .toSet()
    }

    private fun mapTo(gamedayEntity: com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday): Gameday {
        return Gameday(
            amateurSoccerGroupId = AmateurSoccerGroupId(gamedayEntity.amateurSoccerGroupId!!),
            date = Gameday.Date(gamedayEntity.date),
            matches = gamedayEntity.matches.map { mapTo(it) },
        )
    }

    private fun mapTo(matchEntity: com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match): Gameday.Match {
        return Gameday.Match(
            players = matchEntity.players.map { mapTo(it) }.toSet(),
        )
    }

    private fun mapTo(playerStatistic: com.gitlab.marciovmartins.futsite.modularmonolith.gameday.Gameday.Match.PlayerStatistic): Gameday.Match.PlayerStatistic {
        return Gameday.Match.PlayerStatistic(
            playerId = PlayerId(playerStatistic.playerId),
            team = Gameday.Match.Team.valueOf(playerStatistic.team.name),
            goalsInFavor = playerStatistic.goalsInFavor,
            ownGoals = playerStatistic.ownGoals,
        )
    }
}
