# Team Workflow

## Canonical Spec
- Start from local spec docs in `docs/`.
- Keep a single source of truth for decisions.

## Recommended Iteration Unit
- One PR = one bounded context + one layer.
- Suggested order: `setup -> identity -> quiz -> session -> ranking`.

## Definition of Done
- Domain invariants are explicit in code and DB constraints.
- Application layer contains orchestration only.
- Infrastructure maps domain <-> persistence clearly.
- Presentation layer has explicit request/response and error mapping.
- Test and docs updated.

## PR Cadence
- Small PRs daily or every two days.
- Early draft PR is encouraged for review feedback.

## Version Memory Rule
- Version decisions must be recorded in `docs/STACK_BASELINE.md`.
- If a PR changes versions, update `docs/STACK_BASELINE.md` in the same PR.

## Roadmap Alignment
- Follow docs/ROADMAP.md backlog order by default.
- If order changes, document reason in PR 'Design Notes'.
