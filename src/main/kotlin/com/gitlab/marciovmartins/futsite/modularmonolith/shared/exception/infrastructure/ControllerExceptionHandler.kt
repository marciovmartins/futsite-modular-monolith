package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.domain.IllegalPropertyException
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(IllegalPropertyException::class)
    fun handleIllegalArgumentException(ex: IllegalPropertyException): ProblemDetail {
        val typeUrl = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .replacePath("/api/exception/illegal-property")
            .build().toUri()

        return ProblemDetail.forStatus(400).apply {
            this.type = typeUrl
            this.detail = ex.message
            ex.properties.forEach { this.setProperty(it.key, it.value) }
        }
    }
}