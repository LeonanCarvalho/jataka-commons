package org.jatakasource.common.model.security;

import org.jatakasource.common.model.BasePojo;

public class UserPojo extends BasePojo<Long> implements IUser {
	private static final long serialVersionUID = -4886134248036363476L;

	private Long id;
	private String username;
	private String password;
	private String confirmpassword;
	private String firstName;
	private String lastName;
	private String personalId;

	// Authentication properties
	private String passwordSlat;
	private boolean enabled = true;
	private boolean accountNonLocked = true;
	private boolean accountNonExpired = true;
	private boolean credentialsNonExpired = true;
	private boolean administrator = true;

	public UserPojo() {

	}

	public UserPojo(Long id) {
		super();
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPersonalId() {
		return personalId;
	}

	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	public String getPasswordSlat() {
		return passwordSlat;
	}

	public void setPasswordSlat(String passwordSlat) {
		this.passwordSlat = passwordSlat;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	@Override
	public String getConfirmpassword() {
		return confirmpassword;
	}

	@Override
	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}
}
