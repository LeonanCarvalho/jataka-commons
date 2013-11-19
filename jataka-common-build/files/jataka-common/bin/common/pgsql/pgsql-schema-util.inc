#!/bin/bash

################################################################
# pgsql-util.inc must be imported in order to use this scripts #
################################################################

_schema_drop_schema() {
    if [ "$(pgsql_cmd "${DBA_USER}" "${DB_HOST}" \
        "template1" "SELECT count(*) FROM pg_database WHERE datname='${DB_NAME}'" 2>&1)" = "1" ]; then
        
        pgsql_cmd_die ${DBA_USER} "${DB_HOST}" "template1" "DROP DATABASE \"${DB_NAME}\""
    fi
}

_schema_create_user() {
    if [ "$(pgsql_cmd "${DBA_USER}" "${DB_HOST}" "template1" \
        "SELECT count(*) FROM pg_user WHERE usename='${DB_NAME}'" 2>&1)" = "0" ]; then
        
        pgsql_cmd_die "${DBA_USER}" "${DB_HOST}" "template1" \
            "CREATE USER \"${DB_NAME}\" CREATEDB CREATEUSER"
    fi
}

_schema_drop_user() {
    if [ "$(pgsql_cmd "${DBA_USER}" "${DB_HOST}" "template1" \
        "SELECT count(*) FROM pg_user WHERE usename='${DB_NAME}'" 2>&1)" = "1" ]; then
        
        pgsql_cmd_die "${DBA_USER}" "${DB_HOST}" "template1" \
            "DROP USER \"${DB_NAME}\""   
    fi
}

_schema_create_schema() {
    pgsql_cmd_die "${DBA_USER}" "${DB_HOST}" "template1" \
        "CREATE DATABASE \"${DB_NAME}\" OWNER \"${DB_NAME}\""
}

_schema_grant() {
    pgsql_cmd_die "${DBA_USER}" "${DB_HOST}" "${DB_NAME}" \
        "GRANT ALL PRIVILEGES ON DATABASE \"${DB_NAME}\" TO \"${DB_NAME}\""
    
    pgsql_cmd_die "${DBA_USER}" "${DB_HOST}" "${DB_NAME}" \
        "GRANT ALL ON DATABASE \"${DB_NAME}\" TO \"${DB_NAME}\""
}

pgsql_schema_drop() {
    _schema_drop_schema
    _schema_drop_user
}

pgsql_schema_create() {
    _schema_drop_schema
    _schema_create_user
    _schema_create_schema
    _schema_grant
}

pgsql_is_alive() {
    pgsql_alive "${DBA_USER}" "${DB_HOST}"
}

psql_schema_execute_scripts() {
    local dir="$1"

    find "${dir}" -type f -name 'psql-*' | sort | while read f; do
       pgsql_load_file "${DB_NAME}" "${DB_HOST}" "${DB_NAME}" \
            "${f}"
    done || die "Failed on running sql scripts from ${dir}"
}
