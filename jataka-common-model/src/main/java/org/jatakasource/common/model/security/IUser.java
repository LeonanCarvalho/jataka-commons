package org.jatakasource.common.model.security;

import org.jatakasource.common.model.IDomainObject;

public interface IUser extends IDomainObject<Long> {

	Long getId();

	void setId(Long id);

	String getUsername();

	void setUsername(String username);

	String getFirstName();

	void setFirstName(String firstName);

	String getLastName();

	void setLastName(String lastName);

	String getPersonalId();

	void setPersonalId(String personalId);

	String getPassword();

	void setPassword(String password);

	String getConfirmpassword();
	
	void setConfirmpassword(String confirmpassword);
	
	String getPasswordSlat();
	
	void setPasswordSlat(String passwordSlat);

	boolean isAccountNonExpired();

	void setAccountNonExpired(boolean accountNonExpired);

	boolean isAccountNonLocked();

	void setAccountNonLocked(boolean accountNonLocked);

	boolean isCredentialsNonExpired();

	void setCredentialsNonExpired(boolean credentialsNonExpired);

	boolean isEnabled();

	void setEnabled(boolean enabled);

	boolean isAdministrator();

	void setAdministrator(boolean administrator);
}