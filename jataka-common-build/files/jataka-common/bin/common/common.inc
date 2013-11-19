# -*- mode: bash; -*-

SUBST_NEW_FILES=0
SUBST_NEW_FILES_NAMES=""
which service > /dev/null 2>&1 && HAVE_SERVICE=1

get_full_path() {
    # Absolute path to this script, e.g. /home/user/bin/foo.sh
    SCRIPT=$(readlink -f $0)

    if [ ! -d ${SCRIPT} ]; then
        # Absolute path this script is in, thus /home/user/bin
        SCRIPT=`dirname $SCRIPT`
    fi

    ( cd "${SCRIPT}" ; pwd )
}

die() {
	local localmsg="$1"
	echo "FATAL: ${localmsg}" >&2
	exit 1
}

print_msg() {
	echo "$1"
}

# If a DEBUG variable exists and is equal "1" then it echos all the provided arguments
# Usage: print_debug "what ever you want to print" 
function print_debug() {
    [ -n "${DEBUG}" -a "${DEBUG}" == "1" ] && echo "DEBUG: $*"
}

_common_service_exec() {
	local command="$1"
	local service="$2"

	if [ -n "${HAVE_SERVICE}" ]; then
		service "${service}" "${command}" 
	elif [ -x "/etc/init.d/${service}" ]; then
		"/etc/init.d/${service}" "${command}"
	else
		die "Don't know how to handle service '${service}'"
	fi
}

common_service_status() {
	local service="$1"
	_common_service_exec status "${service}"
}

common_service_start() {
	local service="$1"
	_common_service_exec start "${service}"
}

common_service_stop() {
	local service="$1"
	_common_service_exec stop "${service}"
}

common_timestamp_get_raw() {
	date "+%Y%m%d%H%M%S"
}

common_timestamp_get_display() {
	date "+%Y-%m-%d %H:%M:%S"
}

#
# Usage: source -> dest
#
create_link() {
	local source="$1"
	local dest="$2"
	[ -L "${dest}" ] && rm "${dest}"
	[ -e "${dest}" ] && die "${dest} exists and is not symbolic link, please remove by hand"
	
	ln -s "${source}" "${dest}" || die "Failed on linking ${dest}"
}

#
# Usage: create_dirs "testcube /tmp/1 /tmp/2"
#
# directories - a list of directories, first position is owner user, second...n directory names  
#
create_dirs() {
	local user="$1"; shift

	while [ -n "$1" ]; do
		local d="$1"; shift
		mkdir -p "${d}" || die "Failed on creating ${d} directory"
		chown ${user}:${user} "${d}" || die "chown to '${user}' failed '${d}'" 
		# This required to allow other [debug] users access
		chmod 0775 "${d}" || die "Cannot change permissions to '${d}'"
	done
}

#
# Usage: create_files "testcube /tmp/1/x.txt /tmp/2/x.txt"
#
# files - a list of files, first position is owner user, second...n files  
#
create_files() {
	local user="$1"; shift

	while [ -n "$1" ]; do
		local d="$1"; shift
		touch "${d}" || die "Failed on creating ${d} file"
		chown ${user}:${user} "${d}" || die "chown to '${user}' failed '${d}'" 
		# This required to allow other [debug] users access
		chmod 0775 "${d}" || die "Cannot change permissions to '${d}'"
	done
}

is_root_user() { 
	[ $(id -u) = 0 ] || die "Please run as root."
}

common_pid_cleanup() {
	local pidfile="$1"
	rm -f "${pidfile}"
}

common_pid_setup() {
	local pidfile="$1"
	if [ -f "${pidfile}" ]; then
		kill -0 "$(cat "${pidfile}")" > /dev/null 2>&1 && return 1
	fi
	echo $$ > "${pidfile}"
	return 0
}

#
# Usage:
#
# vars - a list of variables to common_substitute.
# keep - keep .new files if destination differ 
## 0 - don't keep
## 1 - Keep .new files
## 2 - Append to existing destfile
# source
# dest
#
# If files are with .in suffix substitution takes place.
# otherwise regular copy is performed.
#
common_substitute() {
	local vars="$1"
	local keep="$2"
	local source="$3"
	local dest="$4"
	local TMP="${TMP:-/tmp}"
	local script="${TMP}/$$-script"
	local v

	local name="${source%.in}"

	if [ "${name}" != "${source}" ]; then
		rm -f "${script}"
		for v in ${vars}; do
			local s
			eval s="\$${v}"
			cat >> "${script}" << __EOF__
s|@${v}@|${s}|g
__EOF__
		done
		sed -f "${script}" "${source}" > "${dest}.new" || return 1
		rm -f "${script}"
	else
		cp "${source}" "${dest}.new"
	fi

	local dont_move
	if [ -e "${dest}" ]; then
		if [ "${keep}" = 1 ]; then
			if [ -e "${dest}" ]; then
				if ! cmp -s "${dest}.new" "${dest}"; then
					dont_move=1
					SUBST_NEW_FILES=$((${SUBST_NEW_FILES}+1))
					SUBST_NEW_FILES_NAMES="${SUBST_NEW_FILES_NAMES} '${dest}.new'"
				fi
			fi
		fi
	fi

    if [ "${keep}" = 2 ]; then
        cat ${dest}.new >> "${dest}" || return 1
        rm -rf ${dest}.new
        return 0
    fi 

    if [ -z "${dont_move}" ]; then
        mv "${dest}.new" "${dest}" || return 1    
    fi
}