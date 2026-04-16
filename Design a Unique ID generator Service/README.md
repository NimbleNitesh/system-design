# Design a Unique ID Generator Service

---

## Functional Requirements (FRs)

- The service should generate a **unique ID** on every call.
- IDs should contain only **digits (0–9)**.
- IDs should be **ordered by date**, i.e.:
  - If `ID1` is generated before `ID2`, then `ID1 < ID2` (numerically).

---

## Non-Functional Requirements (NFRs)

- Each ID should not exceed **8 bytes (64 bits)**.
- The system should support generating up to **1 billion (1B) IDs per day**.