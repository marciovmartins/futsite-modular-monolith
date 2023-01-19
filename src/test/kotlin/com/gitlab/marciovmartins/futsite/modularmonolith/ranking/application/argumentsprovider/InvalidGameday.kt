package com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.argumentsprovider

import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.GamedayDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.RankingFixture
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.domain.Gameday
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.time.Instant
import java.util.stream.Stream
import kotlin.reflect.KClass

object InvalidGameday : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> = Stream.of(
        argument(
            testDescription = "Gameday date year before 2020",
            gameday = RankingFixture.gamedayDTO().copy(
                date = Instant.parse("2019-12-31T23:59:59.999999Z")
            ),
            exception = Gameday.Date.InvalidDateException::class,
            properties = mapOf(
                "propertyName" to "date",
                "propertyValue" to Instant.parse("2019-12-31T23:59:59.999999Z"),
            ),
        ),
        argument(
            testDescription = "Gameday date in the future",
            gameday = RankingFixture.gamedayDTO().copy(
                date = Instant.parse("2030-01-01T00:00:00.000000Z")
            ),
            exception = Gameday.Date.InvalidDateException::class,
            properties = mapOf(
                "propertyName" to "date",
                "propertyValue" to Instant.parse("2030-01-01T00:00:00.000000Z"),
            ),
        ),
    )
}

private fun argument(
    testDescription: String,
    gameday: GamedayDTO,
    exception: KClass<out Exception>,
    properties: Map<String, Any>,
) = Arguments.of(testDescription, gameday, exception, properties)
