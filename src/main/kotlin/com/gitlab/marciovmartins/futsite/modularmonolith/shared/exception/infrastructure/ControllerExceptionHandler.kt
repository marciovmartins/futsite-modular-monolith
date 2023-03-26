package com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.infrastructure

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.gitlab.marciovmartins.futsite.modularmonolith.shared.exception.domain.IllegalPropertyException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(IllegalPropertyException::class)
    fun handleIllegalArgumentException(ex: IllegalPropertyException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            this.type = typeUrl("illegal-property")
            this.title = ex::class.simpleName?.removeSuffix("Exception")
            this.detail = ex.message
            ex.properties.forEach { this.setProperty(it.key, it.value) }
        }
    }

    @ExceptionHandler(MissingParameterException::class)
    fun handleMissingPropertyException(ex: MissingParameterException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            this.type = typeUrl("missing-parameter")
            this.title = "Missing Parameter"
            this.setProperty("propertyName", ex.propertyName)
        }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            this.type = typeUrl("illegal-property")

            val constraintViolation = ex.constraintViolations.first()
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

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ProblemDetail {
        return ProblemDetail.forStatus(400).apply {
            when(ex.cause) {
                is MissingKotlinParameterException -> {
                    this.type = typeUrl("missing-parameter")
                    this.title = "Missing Parameter"
                    this.setProperty("propertyName", (ex.cause as MissingKotlinParameterException).parameter.name)
                }
            }
        }
    }

    private fun typeUrl(exceptionType: String) = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .replacePath("/api/exception/$exceptionType")
        .build().toUri()
}