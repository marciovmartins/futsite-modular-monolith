package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.application.GetHelloWorld
import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.application.HelloWorldDTO
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class HelloWorldController(
    private val getHelloWorld: GetHelloWorld
) {
    @GetMapping("/api/v1/helloworld")
    fun helloWorldName(@RequestParam name: String?): ResponseEntity<HelloWorldDTO> {
        val helloWorldDTO = getHelloWorld.with(name)
        return ResponseEntity.ok(helloWorldDTO)
    }
}