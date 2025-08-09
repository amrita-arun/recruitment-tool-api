alter table applicants
  add column if not exists answers jsonb,
  add column if not exists resume_url text;

create index if not exists idx_applicants_answers_gin
  on applicants using gin (answers);