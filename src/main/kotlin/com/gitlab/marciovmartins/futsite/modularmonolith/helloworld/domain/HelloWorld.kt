package com.gitlab.marciovmartins.futsite.modularmonolith.helloworld.domain

data class HelloWorld(
        val name: Name,
) {
    constructor(name: String?) : this(name?.let { Name(it) } ?: Name.DEFAULT)

    data class Name(
            val value: String,
    ) {
        init {
            if (value.isEmpty()) throw IllegalArgumentException("Empty hello world name")
        }

        companion object {
            val DEFAULT = Name("John Doe")
        }
    }
}
