# Documentation

This directory contains architecture documentation assets for the Estate Booking Platform.

## Diagram Index

The `images` directory stores SVG diagrams referenced by the main project documentation.

| Diagram                                                               | Purpose                                                                                   |
|-----------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| [System Context](images/system_context.svg)                           | Shows the platform boundary, external actors, and major infrastructure components.        |
| [Security Model](images/security_model.svg)                           | Explains token handling, Keycloak responsibilities, and service authorization boundaries. |
| [Registration Architecture](images/registration_architecture.svg)     | Describes the Keycloak-owned registration flow and asynchronous profile provisioning.     |
| [Data Ownership](images/data_ownership.svg)                           | Shows service-level ownership of data and the database-per-service rule.                  |
| [Cross-Service Reference](images/cross_service_reference.svg)         | Documents how services reference external identifiers without cross-service foreign keys. |
| [Listing Lifecycle](images/listing_lifecycle.svg)                     | Describes listing states and transitions from draft to archive.                           |
| [Profile Database Schema](images/profiles_db_schema.svg)              | Visualizes profile-related tables and ownership boundaries.                               |
| [Listing Database Schema](images/listing_db_schema.svg)               | Visualizes listing aggregate persistence.                                                 |
| [Media Database Schema](images/media_db_schema.svg)                   | Visualizes media metadata and upload tracking.                                            |
| [Moderation Database Schema](images/moderation_db_schema.svg)         | Visualizes moderation cases, decisions, and complaints.                                   |
| [Rental Request Database Schema](images/rental_request_db_schema.svg) | Visualizes rental requests, conversations, and viewing appointments.                      |
| [Search Database Schema](images/search_db_schema.svg)                 | Visualizes denormalized search documents and saved searches.                              |

## Maintenance Notes

- Keep diagrams in SVG format so they render clearly in GitHub and remain easy to review in pull requests.
- When a new diagram is added to `images`, add it to the diagram index above and reference it from `README.md` when it
  supports a core architectural topic.
- Avoid storing exported duplicates unless they serve a specific publishing target.
