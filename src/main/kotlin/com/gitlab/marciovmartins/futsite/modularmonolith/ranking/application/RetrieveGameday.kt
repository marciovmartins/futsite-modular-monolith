package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday.GamedayId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.GamedayRepository
import java.util.UUID

class RetrieveGameday(
    private val gamedayRepository: GamedayRepository
) {
    fun by(gamedayId: UUID): GamedayDTO {
        val gameday = gamedayRepository.findBy(GamedayId(gamedayId))
        return GamedayMapper.from(gameday)
    }
}
