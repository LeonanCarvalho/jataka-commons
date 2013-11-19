#!/bin/bash

pgsql_pipe() {
	local db_user="$1"
	local host="$2"
	local db="$3"
	local cmd="$4"
	local extra="$5"

	psql -U "${db_user}" -h "${host}" -d "${db}" -c "${cmd}" --no-align -q -t -v ON_ERROR_STOP=1
}

pgsql_cmd() {
	local db_user="$1"
	local host="$2"
	local db="$3"
	local cmd="$4"
	local extra="$5"
	
	echo "${cmd}" | pgsql_pipe "${db_user}" "${host}" "${db}" "${cmd}" "${extra}"
}

pgsql_cmd_die() {
	local db_user="$1"
	local host="$2"
	local db="$3"
	local cmd="$4"
	local extra="$5"

	pgsql_cmd "${db_user}" "${host}" "${db}" "${cmd}" "${extra}" || die "Cannot execute SQL statement '${cmd}'"
}

pgsql_alive() {
	local db_user="$1"
	local db_host="$2"
	local last
	
	for x in 8 7 6 5 4 3 2 1 0; do
		last=$x
		psql -c "select 1, 2" -d template1 -U "${db_user}" -h "${db_host}" > /dev/null 2>&1 && break
		sleep 1
	done
	[ "${last}" != "0" ]
}

pgsql_load_file() {
        local db_user="$1"
        local host="$2"
        local db="$3"
        local sql_file="$4"

        psql -U "${db_user}" -h "${host}" -d "${db}" -f "${sql_file}"
}

