# Key Access Library

Generic approach to fetching access-controlled keys for Provenance.

## Status

[![stability-release-candidate](https://img.shields.io/badge/stability-pre--release-48c9b0.svg)](https://github.com/mkenney/software-guides/blob/master/STABILITY-BADGES.md#release-candidate)
[![Latest Release][release-badge]][release-latest]
[![Artifact][publication-badge]][publication-url]

[release-badge]: https://img.shields.io/github/v/tag/provenance-io/kms-connector.svg
[release-latest]: https://github.com/provenance-io/kms-connector/releases/latest
[publication-badge]: https://maven-badges.herokuapp.com/maven-central/io.provenance.kms-connector/lib/badge.svg
[publication-url]: https://maven-badges.herokuapp.com/maven-central/io.provenance.kms-connector/lib/
[loc-badge]: https://tokei.rs/b1/github/provenance-io/kms-connector
[loc-report]: https://github.com/provenance-io/kms-connector

## Prerequisites

## Examples
### Vault
#### Example Key JSON
```json
{
    "public_signing_key": "0A21024D346A99201D31FCA89A7B8FD1F5AEC3B13E5A4B08FFE6A7C2174C7CC6C0B90D2001",
    "private_signing_key": "0A20C637707D9E94E30EB10ED4CF914C1D9C863B17AFA68B97A1F6EDEF5E220AEB92",
    "public_encryption_key": "0A210260CF86C34A828D0F147A707A4EF9CF3507FE03F456404051A7A65EE9CA62E2A82001",
    "private_encryption_key": "0A207BBEEB9EEEEDF05AB12495EE93FD5C5CA29CD49FA99E347C7D2AFC957F83D9AA"
}
```
