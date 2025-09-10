# Recruitment Tool API

This project is a **Spring Boot + PostgreSQL backend service** for managing recruitment processes.  
It provides endpoints to import applicant data (via CSV/Google Form exports), view and filter applicants, update statuses, and manage reviewer comments.

---

## 🚀 Features

- **Applicant management**
  - Import applicants from CSV files (e.g., Google Form responses).
  - Store raw application data and parsed structured fields.
  - Track applicant status (`PENDING`, `FIRST_ROUND`, `SECOND_ROUND`, `ACCEPTED`, `REJECTED`).
  - Store resume URLs for quick access.

- **Search & filter**
  - Search applicants by name/email.
  - Filter by application status.
  - Pagination support for large cohorts.

- **Comments system**
  - Add reviewer comments to applicants.
  - Fetch comments with pagination, newest first.

---

## 🛠 Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL** (with Flyway migrations)
- **OpenCSV** (for CSV import)
- **Lombok** (for boilerplate reduction)
- **Jackson** (for JSON handling)
