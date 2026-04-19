# Design a URL Shortening Service
---

## Functional Requirements (FRs)

- Given a long url, return a short url.
- Given a short url, redirect to the original long url.

---

## Non-Functional Requirements (NFRs)

- Write requests/day for url generation: 100M.
- Read QPS: 100x of URL generation.
- Should be highly available and scalable.
- Should be fault-tolerant.
- A generated short URL should work for at least 5 years.