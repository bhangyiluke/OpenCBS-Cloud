create table share_products (
  id                       bigserial       primary key,
  name                     varchar(200)    not null,
  code                     varchar(32)     not null,
  currency_id              bigint          not null,
  nominal_value            numeric(19, 4)  not null,
  unit_price               numeric(19, 4)  not null,
  min_shares_per_member    bigint          not null,
  max_shares_per_member    bigint          null,
  allow_member_transfers   boolean         not null default false,
  lot_selection_policy     varchar(32)     not null default 'FIFO',
  status                   varchar(32)     not null default 'ACTIVE',

  constraint uk_share_products_code unique (code),
  constraint uk_share_products_name unique (name),
  constraint fk_share_products_currency foreign key (currency_id) references currencies (id),
  constraint ck_share_products_nominal_value positive check (nominal_value > 0),
  constraint ck_share_products_unit_price positive check (unit_price > 0),
  constraint ck_share_products_min_shares positive check (min_shares_per_member > 0),
  constraint ck_share_products_max_shares check (max_shares_per_member is null or max_shares_per_member >= min_shares_per_member)
);

create table share_lots (
  id                     bigserial       primary key,
  share_product_id       bigint          not null,
  profile_id             bigint          not null,
  branch_id              bigint          null,
  quantity               bigint          not null,
  available_quantity     bigint          not null,
  unit_price             numeric(19, 4)  not null,
  total_amount           numeric(19, 4)  not null,
  acquisition_date       date            not null,
  source_transaction_id  bigint          null,
  source_transaction_type varchar(32)     null,
  status                 varchar(32)     not null default 'ACTIVE',
  created_by_id          bigint          null,

  constraint fk_share_lots_product foreign key (share_product_id) references share_products (id),
  constraint fk_share_lots_profile foreign key (profile_id) references profiles (id),
  constraint fk_share_lots_branch foreign key (branch_id) references branches (id),
  constraint fk_share_lots_created_by foreign key (created_by_id) references users (id),
  constraint ck_share_lots_quantity positive check (quantity >= 0),
  constraint ck_share_lots_available_quantity positive check (available_quantity >= 0),
  constraint ck_share_lots_available_not_greater_quantity check (available_quantity <= quantity),
  constraint ck_share_lots_unit_price positive check (unit_price > 0),
  constraint ck_share_lots_total_amount positive check (total_amount >= 0)
);

create table share_transactions (
  id                     bigserial       primary key,
  share_product_id       bigint          not null,
  source_profile_id      bigint          null,
  destination_profile_id bigint          null,
  branch_id              bigint          null,
  type                   varchar(32)     not null,
  status                 varchar(32)     not null default 'PENDING',
  quantity               bigint          not null,
  unit_price             numeric(19, 4)  not null,
  total_amount           numeric(19, 4)  not null,
  transaction_date       date            not null,
  reason                 varchar(500)    null,
  idempotency_key        varchar(128)    null,
  created_by_id          bigint          null,

  constraint uk_share_transactions_idempotency_key unique (idempotency_key),
  constraint fk_share_transactions_product foreign key (share_product_id) references share_products (id),
  constraint fk_share_transactions_source_profile foreign key (source_profile_id) references profiles (id),
  constraint fk_share_transactions_destination_profile foreign key (destination_profile_id) references profiles (id),
  constraint fk_share_transactions_branch foreign key (branch_id) references branches (id),
  constraint fk_share_transactions_created_by foreign key (created_by_id) references users (id),
  constraint ck_share_transactions_quantity positive check (quantity > 0),
  constraint ck_share_transactions_unit_price positive check (unit_price > 0),
  constraint ck_share_transactions_total_amount positive check (total_amount >= 0)
);

create index idx_share_products_status on share_products (status);
create index idx_share_lots_profile_product on share_lots (profile_id, share_product_id, status);
create index idx_share_lots_acquisition_date on share_lots (acquisition_date);
create index idx_share_lots_branch on share_lots (branch_id);
create index idx_share_transactions_product on share_transactions (share_product_id);
create index idx_share_transactions_profiles on share_transactions (source_profile_id, destination_profile_id);
create index idx_share_transactions_date on share_transactions (transaction_date);
create index idx_share_transactions_branch on share_transactions (branch_id);
