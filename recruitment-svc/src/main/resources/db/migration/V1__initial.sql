create extension if not exists pgcrypto;

create table applicants (
      id uuid primary key default gen_random_uuid(),
      name text not null,
      email text,
      phone text,
      location text,
      major text,
      year text,
      gpa numeric(3,2),
      status text not null default 'PENDING',
      raw jsonb,
      created_at timestamptz not null default now()
);

create table applicant_comments (
    id uuid primary key default gen_random_uuid(),
    applicant_id uuid not null references applicants(id) on delete cascade,
    author text not null,
    body text not null,
    created_at timestamptz not null default now()
);


create index if not exists idx_applicants_created_at on applicants (created_at desc);
create index if not exists idx_comments_applicant on applicant_comments (applicant_id, created_at desc);
