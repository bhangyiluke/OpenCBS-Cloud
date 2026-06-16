You are a senior full-stack engineer working in the OpenCBS-Cloud Sacco Management System.

Create a complete shares module for a sacco/cooperative financial institution. The module must support share capital management, member share ownership, share purchases, share transfers, member-to-member share transfers, and reporting on the age of shares and the sacco’s total share portfolio.

Work within the existing project conventions. Inspect the repository first, especially existing backend modules such as borrowings, savings, accounting, and reports. Follow the current Spring Boot/Java backend architecture, Angular frontend architecture, database migration standards, DTO patterns, validation, security, and reporting patterns.

Functional requirements:

1. Share product/configuration
   - Support configurable share products or share classes.
   - Track share name/code, nominal value, currency, minimum/maximum shares per member, whether member-to-member transfers are allowed, and active/inactive status.
   - Support share pricing rules if the system needs share price adjustments or premium/discount handling.
   - Validate that share transactions use active share products only.

2. Member share ownership
   - Track shares owned by each member.
   - Maintain share lots per member so the system can calculate the age of shares.
   - Each share lot should track member, share product, quantity, unit price, total amount, acquisition date, source transaction, current available quantity, and status.
   - Provide member share portfolio summaries.

3. Purchase of shares by members
   - Allow a member to purchase shares.
   - Create a share purchase transaction.
   - Add purchased shares to the member’s share lots.
   - Support accounting integration if the project has an accounting posting pattern.
   - Validate minimum and maximum shareholding limits.
   - Validate payment amount against share price and quantity.

4. Transfer of shares
   - Support transfer of shares between members.
   - Support transfer from a member to another member for the same share product.
   - Use FIFO, LIFO, or configurable lot selection policy. Prefer FIFO unless the existing project has another policy.
   - Reduce available quantity from source member share lots.
   - Add new share lots to receiving member.
   - Preserve original acquisition date for transferred shares so share age is not reset.
   - Create a share transfer transaction with source member, destination member, share product, quantity, unit price, total amount, transfer date, reason, and status.
   - Validate that the source member has enough available shares.
   - Prevent transfers to the same member.
   - Prevent transfers if the share product does not allow member-to-member transfers.
   - Prevent transfers that would violate member maximum shareholding limits.

5. Share age reporting
   - Implement reports or query services for age of shares.
   - Age should be calculated from acquisition date to the report date.
   - Support age buckets such as:
     - 0-30 days
     - 31-90 days
     - 91-180 days
     - 181-365 days
     - more than 365 days
   - Support filtering by member, share product, branch, and date range.
   - Return both quantity and value per age bucket.

6. Sacco share portfolio
   - Provide total share portfolio for the sacco.
   - Include total issued shares, total share value, shares by product, shares by member status, and shares by age bucket.
   - Support reporting by date.
   - Support branch-level filtering if the project has branches.

7. Member share ownership reporting
   - Provide reports showing shares owned by a member.
   - Include share product, quantity, unit price, total value, acquisition date, age, source transaction, and transfer history.
   - Support current holdings and historical holdings.

8. APIs
   Create REST APIs for:
   - Share products: create, update, list, get, activate/deactivate.
   - Member share purchases.
   - Member share transfers.
   - Member share portfolio.
   - Sacco share portfolio.
   - Share age analysis.
   - Share transaction history.

Use existing API conventions for request/response DTOs, pagination, sorting, validation, error handling, and authorization.

9. Database
   - Add Flyway migrations for all required tables.
   - Use proper foreign keys, indexes, constraints, and audit fields.
   - Do not manually edit existing migration files.
   - Include constraints for non-negative quantities and amounts where supported.
   - Consider idempotency for transaction creation.

10. Business rules
   - No member can own negative available shares.
   - No member can purchase shares below the configured minimum.
   - No member can exceed configured maximum shareholding unless the product allows exceptions.
   - Share transfers must be atomic.
   - Share purchases and transfers must be auditable.
   - All transactions must be traceable to a source member, destination member, product, quantity, amount, and actor.
   - Use transactional service methods.
   - Return meaningful domain exceptions for validation failures.

11. Frontend
   Create Angular UI pages/components following existing conventions for:
   - Share product management.
   - Member share purchase.
   - Member-to-member share transfer.
   - Member share portfolio.
   - Sacco share portfolio dashboard.
   - Share age analysis report.
   - Share transaction history.

Use existing Angular routing, services, forms, validation, state management, and SLDS component patterns.

12. Reporting
   - If the project uses JasperReports, add appropriate report templates or DTO-based report endpoints.
   - Include export-friendly data structures for PDF/Excel if existing reporting patterns support exports.

13. Tests
   - Add backend unit tests for share purchase, share transfer, share age calculation, portfolio summary, and validation failures.
   - Add repository tests where appropriate.
   - Add API tests if the project has that pattern.
   - Add frontend tests if the project has an existing frontend test setup.

Implementation guidance:
- Start by identifying the existing backend package/module structure and mirror it for a new shares module.
- Use existing entity, repository, service, controller, DTO, mapper, exception, and test patterns.
- Use existing audit fields and base entity patterns if present.
- Use existing branch/member abstractions rather than duplicating them.
- Use existing accounting integration patterns if present.
- Prefer Spring Modulith module boundaries if the project uses them.
- Keep business logic in services, not controllers.
- Do not add comments unless needed for clarity.
- Do not change unrelated code.
- After implementation, run the relevant backend and frontend validation commands if they are available in the repository.

Deliverables:
- Backend entities, repositories, services, controllers, DTOs, validators, exceptions, and migrations.
- Angular services, components/pages, routes, forms, and UI integration.
- Reports or report endpoints for share age, member portfolio, and sacco portfolio.
- Tests for critical business rules.
- A concise implementation summary describing files changed, APIs added, business rules enforced, and how to run the module.