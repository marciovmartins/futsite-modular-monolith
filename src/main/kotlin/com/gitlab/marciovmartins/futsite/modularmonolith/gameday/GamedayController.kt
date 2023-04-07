package com.gitlab.marciovmartins.futsite.modularmonolith.gameday

import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class GamedayController(
    private val gamedayRepository: GamedayRepository,
) {
    @PostMapping(
        path = ["/api/v1/gameDays"],
        consumes = [MediaType.APPLICATION_JSON_VALUE, "application/hal+json"],
        produces = ["application/hal+json"],
    )
    fun create(@RequestBody gamedayDTO: GamedayDTO): ResponseEntity<RepresentationModel<*>> {
        val gameday = gamedayDTO.toDomain()

        gamedayRepository.save(gameday)

        val gamedayLink = linkTo(methodOn(GamedayController::class.java).byId(gameday.gamedayId!!))
            .withSelfRel()

        return ResponseEntity
            .created(gamedayLink.toUri())
            .body(RepresentationModel.of(gameday.toDTO()).add(gamedayLink))
    }

    @GetMapping(
        path = ["/api/v1/gameDays/{gamedayId}"],
        produces = ["application/hal+json"],
    )
    fun byId(@PathVariable gamedayId: UUID): ResponseEntity<RepresentationModel<*>> {
        val gameday = gamedayRepository.findById(gamedayId).orElse(null)
            ?: return ResponseEntity.notFound().build()

        val selfLink = linkTo(methodOn(GamedayController::class.java).byId(gamedayId))
            .withSelfRel()
        val gamedayLink = linkTo(methodOn(GamedayController::class.java).byId(gamedayId))
            .withRel("gameday")

        return ResponseEntity.ok(
            RepresentationModel.of(gameday.toDTO())
                .add(selfLink)
                .add(gamedayLink)
        )
    }
}