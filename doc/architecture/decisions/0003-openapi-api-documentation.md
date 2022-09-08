# 3. OpenAPI Api Documentation

Date: 2022-09-05

## Status

Accepted

## Context

External components and clients needs to have some way to learn about the api capabilities without have to look inside
the code base.

## Decision

We will use the OpenAPI specification version 3.1. We will use Springdoc-openapi implementation to generate the openapi
artifact and the UI interface also provided by it.

## Consequences

With the solid OpenApi specification, it is possible to use external frameworks and libraries to generate web
documentation pages for the documentation, generate automatically clients for many programming languages.
