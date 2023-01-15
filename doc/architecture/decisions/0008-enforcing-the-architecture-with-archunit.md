# 8. Enforcing the Architecture with ArchUnit

Date: 2022-10-27

## Status

Accepted

## Context

Different developers have different programming styles. It is important to have a unique way to develop the components.

## Decision

Use the ArchUnit library to enforce the onion architecture.

The Onion Architecture has layers and each layer can have dependencies with itself and with the inner layers only.

https://www.archunit.org/

## Consequences

Avoid put classes and files in the wrong package. Help to identify flaws in the design and classes.
