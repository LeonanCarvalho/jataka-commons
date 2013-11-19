#!/bin/bash

######################################################################
# export database variables and test if database server is installed #
###################################################################### 
verify_database_server() {
    [ -z "${DB_NAME}" ] && die "Please specify DB name !!!" 
    [ -z "${DB_INSTANCE_PASSWORD}" ] && export DB_INSTANCE_PASSWORD="jataka"
    
    if [ "${DB_TYPE}" = "orcl" ]; then
    	[ -z "${OS_DB_USER}" ] && OS_DB_USER="oracle"
    	[ -z "${OS_DB_PASSWORD}" ] && OS_DB_PASSWORD="oracle"
    	[ -z "${DB_HOST}" ] && DB_HOST="localhost"
    	[ -z "${DB_PORT}" ] && DB_PORT="1521"
    	[ -z "${DB_SID}" ] && DB_SID="orcl"
    	[ -z "${SQLPLUS_SID}" ] && SQLPLUS_SID="${DB_SID}"
    	[ -z "${DBA_USER}" ] && DBA_USER="system"
    	[ -z "${DBA_PASSWORD}" ] && DBA_PASSWORD="manager"
    	
    	if [ "${DB_HOST}" = "localhost" ]; then
    		su - "${OS_DB_USER}" -c "which oracle" > /dev/null 2>&1
    		if [ $? -eq 1 ]; then
    			NO_DB_ROLLBACK=1
    			die "Can't find oracle"
    		fi
    	fi
    fi

	
    if [ "${DB_TYPE}" = "pgsql" ]; then
    	[ -z "${OS_DB_USER}" ] && export OS_DB_USER="postgres" || export OS_DB_USER
    	[ -z "${OS_DB_PASSWORD}" ] && export OS_DB_PASSWORD="postgres" || export OS_DB_PASSWORD
    	[ -z "${DB_HOST}" ] && export DB_HOST="localhost" || export DB_HOST
    	[ -z "${DB_PORT}" ] && export DB_PORT="5432" || export DB_PORT
    	[ -z "${DBA_USER}" ] && export DBA_USER="postgres" || export DBA_USER
    	[ -z "${DBA_PASSWORD}" ] && export DBA_PASSWORD="postgres" || export DBA_PASSWORD
    	
    	if [ "${DB_HOST}" = "localhost" ]; then
    		su - "${OS_DB_USER}" -c "which psql" > /dev/null 2>&1
    		if [ $? -eq 1 ]; then
    			NO_DB_ROLLBACK=1
    			die "Can't find psql"
    		fi
    	fi
    fi
}

#
# Parse and export db.properties file
#
# Usage: parse_properties "/etc/jatakasource/testcube/default/db.propertis"
# #
parse_db_properties(){
    print_debug "Reading DB configuration from $1"
    local db_props_path="$1"
    
    # Use default db.properties as file name
    [ -d ${db_props_path} ] && db_props_path=${db_props_path}/db.properties 
    
    # Export the DB_TYPE variable
    db_type "${db_props_path}"
    print_debug "Setting DB_TYPE to ${DB_TYPE}" 
    
    # Parse db user name  
    DB_NAME=$(grep "^.*DB_NAME=.*" "${db_props_path}") 
    export DB_NAME=${DB_NAME##*=}
    print_debug "Setting DB_NAME to ${DB_NAME}"
        
    # Parse db password 
    DB_INSTANCE_PASSWORD=$(grep "^.*DB_INSTANCE_PASSWORD=.*" "${db_props_path}")
    export DB_INSTANCE_PASSWORD=${DB_INSTANCE_PASSWORD##*=}
    print_debug "Setting DB_INSTANCE_PASSWORD to ${DB_INSTANCE_PASSWORD}"
    
    DB_HOST=$(grep "^.*DB_HOST=.*" "${db_props_path}")
    export DB_HOST=${DB_HOST##*=}
    print_debug "Setting DB_HOST to ${DB_HOST}"
    
    DB_PORT=$(grep "^.*DB_PORT=.*" "${db_props_path}")
    export DB_PORT=${DB_PORT##*=}
    print_debug "Setting DB_PORT to ${DB_PORT}"
    
    DB_SID=$(grep "^.*DB_SID=.*" "${db_props_path}")
    export DB_SID=${DB_SID##*=}
    print_debug "Setting DB_SID to ${DB_SID} (Required only for ORCL)"
}

set_db_variables() {
    case "${DB_TYPE}" in
    	orcl)
            DB_URL="jdbc:oracle:thin:@${DB_HOST}:${DB_PORT}:${DB_SID}"
            DB_DRIVER="oracle.jdbc.driver.OracleDriver"
            DB_HIBERNATE_DIALECT="org.hibernate.dialect.Oracle10gDialect"
            ;;
	   pgsql)
            DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}"
            DB_DRIVER="org.postgresql.Driver"
            DB_HIBERNATE_DIALECT="org.hibernate.dialect.PostgreSQL82Dialect"
            ;;
    esac
}

# 
# Parse db.properties and export DB_TYPE parameter
#
# Usage: db_type "/etc/jatakasource/testcube/default/db.propertis"
# #
db_type() {
    local db_props_path="$1"
    
    # Use default db.properties as file name
    [ -d ${db_props_path} ] && db_props_path=${db_props_path}/db.properties 
    
    grep -q "^.*DB_TYPE=pgsql" "${db_props_path}"
    if [ $? -eq 0 ]; then
    	export DB_TYPE="pgsql"
    fi
    
    grep -q "^.*DB_TYPE=orcl" "${db_props_path}"
    if [ $? -eq 0 ]; then 
        export DB_TYPE="orcl"
    fi
}