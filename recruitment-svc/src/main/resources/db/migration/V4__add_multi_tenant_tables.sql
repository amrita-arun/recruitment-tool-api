hey -- Create users table
create table users (
    id uuid primary key default gen_random_uuid(),
    email text unique not null,
    password text not null,
    name text not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-- Create organizations table
create table organizations (
    id uuid primary key default gen_random_uuid(),
    name text not null,
    description text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-- Create organization_members table
create table organization_members (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references users(id) on delete cascade,
    organization_id uuid not null references organizations(id) on delete cascade,
    role text not null,
    joined_at timestamptz not null default now(),
    unique(user_id, organization_id)
);

-- Create recruitment_cycles table
create table recruitment_cycles (
    id uuid primary key default gen_random_uuid(),
    name text not null,
    description text,
    organization_id uuid not null references organizations(id) on delete cascade,
    status text not null default 'ACTIVE',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

-- Add recruitment_cycle_id to applicants table
alter table applicants add column recruitment_cycle_id uuid references recruitment_cycles(id) on delete cascade;

-- Create indexes for performance
create index if not exists idx_users_email on users (email);
create index if not exists idx_organization_members_user on organization_members (user_id);
create index if not exists idx_organization_members_org on organization_members (organization_id);
create index if not exists idx_recruitment_cycles_org on recruitment_cycles (organization_id);
create index if not exists idx_applicants_recruitment_cycle on applicants (recruitment_cycle_id);