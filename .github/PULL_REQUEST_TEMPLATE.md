# PR Title
[BC] Summary

## Why
- What problem this PR solves.

## What Changed
- Key change 1
- Key change 2

## Scope
- Bounded Context: (identity/quiz/session/ranking/setup)
- Layers: (domain/application/infrastructure/presentation/bootstrap)

## Design Notes
- Domain rule decisions
- Trade-offs and rejected alternatives

## Test
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Local run verified
- [ ] Backward compatibility checked

## Review Focus
- Areas where reviewer should focus

## Checklist
- [ ] No business logic in application orchestration
- [ ] Cross-BC references use IDs only
- [ ] DB constraints match domain invariants
- [ ] Error mapping to gRPC status is defined
- [ ] Docs updated (`docs/*`)
