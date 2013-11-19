#!/bin/sh

get_full_path() {
	local p="$1"
	( cd "${p}" ; pwd ) 
}

install_recursive() {
	local src="$1"
	local dst="$2"
	local d
		
	install -m 0755 -d "${DESTDIR}${dst}"
	( cd "${SOURCE_DIR}/${src}" && find . -type d ) | while read d; do
		if [ "${d}" != "." ]; then
	        [ -d "${DESTDIR}${dst}/${d}" ] || install -m 0755 -d "${DESTDIR}${dst}/${d}" || die "Cannot create ${d}"
		fi
	done || die "Cannot copy recursive ${src}"
	
	( cd "${SOURCE_DIR}/${src}" && find . -type f ) | while read f; do
		local mask
		[ "${f%%.sh}" == "${f}" ] && mask="0644" || mask="0755"
	    [ "${f%%.war}" == "${f}" ] || mask="0755"
		install -m "${mask}" "${SOURCE_DIR}/${src}/${f}" "${DESTDIR}${dst}/${f}" || die "Cannot install ${f}"
	done || die "Cannot copy recursive ${src}"
	
	return 0
}

copy_initscripts() {
        for d in \
                "${DESTDIR}/etc/sysconfig" \
                "${DESTDIR}/etc/init.d" \
                "${DESTDIR}/etc/logrotate.d" \
                "${DESTDIR}/etc/cron.d" \
                ; do
                install -m 0755 -d "${d}" || die "Failed on creating ${d}"
        done

        while [ -n "$1" ]; do
                local service="$1"; shift
                install -m 0755 "${SOURCE_DIR}/misc/${service}.init.d.redhat" "${DESTDIR}/etc/init.d/${service}" || die "Failed on installing ${service}.init.d.redhat"
                install -m 0644 "${SOURCE_DIR}/misc/${service}.conf.d" "${DESTDIR}/etc/sysconfig/${service}" || die "Failed on installing ${service}.conf.d"

                if [ -f "${SOURCE_DIR}/misc/logrotate.d/${service}.logrotate.redhat" ]; then
                        install -m 0644 "${SOURCE_DIR}/misc/logrotate.d/${service}.logrotate.redhat" "${DESTDIR}/etc/logrotate.d/${service}" || die "Failed on installing ${service}.logrotate.redhat"
                fi

                if [ -f "${SOURCE_DIR}/misc/cron.d/${service}.cron" ]; then
                        install -m 0644 "${SOURCE_DIR}/misc/cron.d/${service}.cron" "${DESTDIR}/etc/cron.d/${service}" || die "Failed on installing ${service}.cron"
                fi
        done
}


install_dirs() {
	while [ -n "$1" ]; do
		local d="$1"; shift
		install -m 0755 -d "${d}" || die "Failed on creating ${d}"
	done
}