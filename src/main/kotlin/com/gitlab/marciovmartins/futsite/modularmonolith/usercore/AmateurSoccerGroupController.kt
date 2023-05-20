package com.gitlab.marciovmartins.futsite.modularmonolith.usercore

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
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
import java.util.UUID

@RestController(value = "userCoreAmateurSoccerGroupController")
@RequestMapping(value = ["/api/user-core/amateurSoccerGroups"])
class AmateurSoccerGroupController(
    private val amateurSoccerGroupRepository: AmateurSoccerGroupRepository,
) {
    @PostMapping
    fun create(@RequestBody amateurSoccerGroup: AmateurSoccerGroup?): ResponseEntity<*> {
        if (amateurSoccerGroup == null) return ResponseEntity.badRequest().body("Unable to create")

        amateurSoccerGroupRepository.save(amateurSoccerGroup)

        return amateurSoccerGroupEntityModel(amateurSoccerGroup).let {
            it.getLink(IanaLinkRelations.SELF)
                .map { link -> ResponseEntity.created(URI.create(link.href)).body(it) }
                .orElseThrow()
        }
    }

    @GetMapping("{amateurSoccerGroupId}")
    fun show(
        @PathVariable amateurSoccerGroupId: UUID,
    ): EntityModel<*>? {
        return amateurSoccerGroupRepository.findById(amateurSoccerGroupId).map {
            amateurSoccerGroupEntityModel(it)
        }.orElse(null)
    }

    private fun amateurSoccerGroupEntityModel(amateurSoccerGroup: AmateurSoccerGroup): EntityModel<AmateurSoccerGroup> {
        return EntityModel.of(
            amateurSoccerGroup,
            linkTo(methodOn(AmateurSoccerGroupController::class.java).show(amateurSoccerGroup.amateurSoccerGroupId)!!)
                .withSelfRel()
        )
    }
}