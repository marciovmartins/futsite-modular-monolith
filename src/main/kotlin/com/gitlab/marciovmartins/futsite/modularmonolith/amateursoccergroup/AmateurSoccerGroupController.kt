package com.gitlab.marciovmartins.futsite.modularmonolith.amateursoccergroup

import com.gitlab.marciovmartins.futsite.modularmonolith.gameday.GamedayController
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.application.RankingDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.ranking.infrastructure.CalculateRankingController
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping(value = ["/api/amateurSoccerGroups"])
class AmateurSoccerGroupController(
    private val amateurSoccerGroupRepository: AmateurSoccerGroupRepository,
) {
    @GetMapping
    fun showAll(): CollectionModel<EntityModel<*>> {
        val allAmateurSoccerGroupModel = amateurSoccerGroupRepository.findAll().map {
            EntityModel.of(
                it,
                linkTo(methodOn(AmateurSoccerGroupController::class.java).show(it.amateurSoccerGroupId!!)!!)
                    .withSelfRel(),
            )
        }

        return CollectionModel.of(
            allAmateurSoccerGroupModel,
            linkTo(methodOn(AmateurSoccerGroupController::class.java).showAll()).withSelfRel()
                .andAffordance(afford(methodOn(AmateurSoccerGroupController::class.java).create(null))),
        )
    }

    @PostMapping
    fun create(@RequestBody amateurSoccerGroup: AmateurSoccerGroup?): ResponseEntity<*> {
        if (amateurSoccerGroup == null) return ResponseEntity.badRequest().body("Unable to create")

        amateurSoccerGroupRepository.save(amateurSoccerGroup)
        return EntityModel.of(
            amateurSoccerGroup,
            linkTo(methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroup.amateurSoccerGroupId!!)!!).withSelfRel()
        ).getLink(IanaLinkRelations.SELF)
            .map { ResponseEntity.noContent().location(URI.create(it.href)).build<Any>() }
            .orElseGet { ResponseEntity.badRequest().body("Unable to create $amateurSoccerGroup") }
    }

    @GetMapping
    @RequestMapping("/{amateurSoccerGroupId}")
    fun show(@PathVariable amateurSoccerGroupId: UUID): EntityModel<*>? {
        val period = RankingDTO.Period(Instant.now(), Instant.now())
        return amateurSoccerGroupRepository.findById(amateurSoccerGroupId).map {
            EntityModel.of(
                it,
                linkTo(methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroupId)!!)
                    .withSelfRel(),
                linkTo(methodOn(AmateurSoccerGroupController::class.java).showAll())
                    .withRel("get-amateur-soccer-groups"),
                linkTo(methodOn(GamedayController::class.java).showAll(amateurSoccerGroupId))
                    .withRel("get-gamedays"),
                linkTo(methodOn(GamedayController::class.java).create(amateurSoccerGroupId, null))
                    .withRel("create-gameday"),
                linkTo(
                    methodOn(CalculateRankingController::class.java).calculateRanking(amateurSoccerGroupId, period)
                )
                    .withRel("calculate-ranking")
            )
        }.orElse(null)
    }
}