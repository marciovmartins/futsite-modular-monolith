package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroup
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.AmateurSoccerGroupRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.GamedayRepository
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.AmateurSoccerGroupId
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Ranking
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.argumentsprovider.RetrieveGamedaysSuccessfullyArgumentsProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup.Gameday as ExternalGameday

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GamedayRepositoryIT(
    @Autowired private val jpaAmateurSoccerGroup: AmateurSoccerGroupRepository,
    @Autowired private val jpaGamedayRepository: GamedayRepository
) {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(RetrieveGamedaysSuccessfullyArgumentsProvider::class)
    fun `find by amateur soccer group id`(
        testDescription: String,
        amateurSoccerGroupId: AmateurSoccerGroupId,
        period: Ranking.Period,
        gamedaysToPersist: Set<ExternalGameday>,
        expectedGamedays: Set<Gameday>,
    ) {
        // given
        val amateurSoccerGroup = AmateurSoccerGroup().apply {
            this.amateurSoccerGroupId = amateurSoccerGroupId.value
        }
        jpaAmateurSoccerGroup.save(amateurSoccerGroup)

        jpaGamedayRepository.saveAll(gamedaysToPersist)

        val gamedayRepository = GamedayBoundedContextGamedayRepository(jpaGamedayRepository)

        // when
        val gamedays = gamedayRepository.findBy(amateurSoccerGroupId, period)

        // then
        assertThat(gamedays)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedGamedays)
    }
}