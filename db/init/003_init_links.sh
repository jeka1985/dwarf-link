#!/usr/bin/env bash

set -e

psql -v ON_ERROR_STOP=1 -U "${POSTGRES_USER}" -d "${POSTGRES_DB}" <<-EOF
  \c dwarflink;

  CREATE OR replace FUNCTION intToHash(i bigint) RETURNS text AS \$$
    declare
      chars constant text[] := string_to_array($HASH_PATTERN, null);
      base  constant int := array_length(chars, 1);
      result text := '';
      -- Хеши получаются максимально короткими, добавляем отступ чтобы получить строку длиннее
      rest bigint := i + $HASH_PADDING;
      rem integer;

    begin

      loop
        rem := rest % base;
        rest := rest / base;
        -- Индексация массива начинается с 1
        result := '' || chars[rem + 1] || result;

        exit when rest <= 0;

      end loop;

      return result;
    end;
  \$$ immutable LANGUAGE plpgsql;

  CREATE TABLE links (
    id              bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uid             varchar(300) NOT NULL GENERATED ALWAYS AS (intToHash(id)) STORED,
    user_id         uuid references users(id),
    href            varchar(300) NOT NULL,
    created         timestamp not null default current_timestamp,
    r_limit         integer DEFAULT 100,
    expires         timestamp DEFAULT current_timestamp + interval '10 day'
  );
EOF

