#!/usr/bin/env bash

set -e

psql -v ON_ERROR_STOP=1 -U "${POSTGRES_USER}" -d "${POSTGRES_DB}" <<-EOF
  \c dwarflink;

  CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

  CREATE TABLE users (
    id       uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created  timestamp not null default current_timestamp
  );
EOF

