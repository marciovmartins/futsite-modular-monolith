package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure

import com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.domain.IllegalPropertyException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

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
            this.detail = ex::class.simpleName?.removeSuffix("Exception")
            ex.properties.forEach { this.setProperty(it.key, it.value) }
        }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            val constraintViolation = ex.constraintViolations.first()
            this.type = URI.create("about:blank") //FIXME: add proper human-readable documentation
            this.detail = constraintViolation.message
            this.setProperty("propertyName", constraintViolation.propertyPath.toString())
            this.setProperty("propertyValue", constraintViolation.invalidValue)

            val attributes = constraintViolation.constraintDescriptor.attributes

            val title = attributes["title"]
            if (title != null && title != "") this.title = title.toString()

            val properties = attributes["properties"]
            if (properties != null && properties is Array<*> && properties.isNotEmpty()) properties.forEach {
                if (it is Property) this.setProperty(it.key, it.value)
            }
        }
    }
}