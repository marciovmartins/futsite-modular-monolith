package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GamedayRepositoryIT(
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
}