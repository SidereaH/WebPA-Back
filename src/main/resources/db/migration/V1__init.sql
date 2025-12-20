create table if not exists parse_tasks (
    id uuid primary key,
    query_text varchar(512) not null,
    pages_to_scan integer not null,
    page_size integer not null,
    status varchar(32) not null,
    comment varchar(1024),
    error_message varchar(2000),
    created_at timestamptz not null,
    started_at timestamptz,
    finished_at timestamptz,
    append_to_existing boolean not null default false
);

create table if not exists parse_task_marketplaces (
    task_id uuid not null references parse_tasks (id) on delete cascade,
    marketplace varchar(64) not null
);

create index if not exists idx_parse_task_marketplaces_task on parse_task_marketplaces (task_id);

create table if not exists product_cards (
    id uuid primary key,
    task_id uuid references parse_tasks (id) on delete set null,
    query_text varchar(512),
    marketplace varchar(64) not null,
    source_url text,
    name varchar(512) not null,
    price numeric(19,2),
    image_url text,
    rating double precision,
    feedbacks_count integer,
    seller varchar(256),
    supplier_rating double precision,
    available integer,
    description text,
    main_info jsonb,
    normalized_attributes jsonb,
    raw_attributes jsonb,
    images jsonb,
    excel_filename varchar(255),
    collected_at timestamptz not null,
    updated_at timestamptz not null
);

create index if not exists idx_product_cards_query on product_cards (query_text);
create index if not exists idx_product_cards_marketplace on product_cards (marketplace);
create index if not exists idx_product_cards_task_id on product_cards (task_id);
