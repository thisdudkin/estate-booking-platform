# Documentation

This directory contains exported architecture documentation assets for the Estate Booking Platform.

## Diagram Index

The root `README.md` now keeps canonical diagram sources inline as Graphviz and PlantUML code. The `images` directory is
kept only for exported or legacy visual assets.

| Diagram                                                               | Purpose                                                                                   |
|-----------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| Diagram                                                           | Purpose                                                                                 |
|-------------------------------------------------------------------|-----------------------------------------------------------------------------------------|
| [Data Ownership](images/data_ownership.svg)                       | Legacy exported diagram. The canonical source now lives as Graphviz in the root README. |
| [Registration Sequence](images/registration_sequence_diagram.svg) | Legacy exported diagram. The canonical source now lives as PlantUML in the root README. |

## Maintenance Notes

- Prefer inline Graphviz and PlantUML sources in the root `README.md` for architectural diagrams.
- When a new exported diagram is added to `images`, add it to the diagram index above.
- Avoid storing exported duplicates unless they serve a specific publishing target.
