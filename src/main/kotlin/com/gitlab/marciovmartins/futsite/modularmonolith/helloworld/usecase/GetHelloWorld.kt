package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.usecase

import org.springframework.stereotype.Service

@Service
class GetHelloWorld {
    fun with(name: String) = HelloWorldDTO(
        message = "Hello world, $name!",
        name = name
    )
}
