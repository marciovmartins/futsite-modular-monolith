package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.application

import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.application.HelloWorldMapper.toDTO
import com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.domain.HelloWorld
import org.springframework.stereotype.Service

@Service
class GetHelloWorld {
    fun with(name: String? = null): HelloWorldDTO {
        val helloWorld = HelloWorld(name)
        return helloWorld.toDTO()
    }
}
