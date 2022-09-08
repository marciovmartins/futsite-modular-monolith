# 2. Spring boot as main framework

Date: 2022-09-05

## Status

Accepted

## Context

A component/service needs to use several non-functional functionalities like rest resource for the http rest api,
database access, messaging and so on.

This kind of functionality it is not the core domain for the project but should not be neglected.

## Decision

We will use Spring Boot version 2.7.3 and their dependencies as main framework and use most of their capabilities.

## Consequences

Less coding, less mental effort, very known by more than 50% of the java/kotlin developers. This helps the developments
speed.
