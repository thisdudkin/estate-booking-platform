# Security Policy

## Reporting Vulnerabilities

If you discover a security vulnerability, misconfiguration, leaked secret, unsafe default, or any behavior that may put users or data at risk, please open a GitHub Issue in this repository.

When reporting, include:

- A clear description of the issue.
- Steps to reproduce, if available.
- The affected component or file.
- The potential impact.
- Any suggested mitigation, if you have one.

Please avoid publishing real credentials, tokens, private keys, or sensitive personal data in the issue. If a reproduction requires secrets, replace them with safe placeholders.

## Scope

This is an educational demo project, not a production service. Even so, security reports are welcome because they help improve the architecture, documentation, and implementation practices.

Relevant areas include:

- Authentication and authorization problems.
- JWT validation mistakes.
- Incorrect role or ownership checks.
- Insecure registration or compensation behavior.
- Secret handling issues.
- Unsafe logging of passwords, tokens, or personal data.
- Dependency vulnerabilities.
- Container or local infrastructure misconfigurations.

## Expected Response

Maintainers will review security-related GitHub Issues as soon as reasonably possible and may ask for additional details. Confirmed issues should be fixed, documented, or explicitly accepted as a known educational limitation.
