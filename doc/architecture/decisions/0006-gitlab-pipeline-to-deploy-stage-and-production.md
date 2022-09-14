# 6. Gitlab pipeline to deploy Stage and Production

Date: 2022-09-14

## Status

Accepted

## Context

The implemented feature should go live and be accessible in a stage environment and also production. Should be used the
private microk8s kubernetes cluster.

## Decision

We will use the Gitlab CI/CD directly with the Kubernetes Cluster via an agent in the k8s.

[Link to the documentation](https://docs.gitlab.com/ee/user/clusters/agent/ci_cd_workflow.html)

## Consequences

We will learn how to connect GitLab CI/CD with k8s. Use the pipeline to deploy the application in state and also in
production.

Very simple and straightforward way to deploy applications.

Having an easy way to deploy the application will allow to speed up the development process and start to do real tests
with the application.