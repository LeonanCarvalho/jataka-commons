package org.jatakasource.common.authentication;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.encoding.BaseDigestPasswordEncoder;
import org.springframework.util.Assert;

public class PasswordEncoders implements InitializingBean {

	private Map<String, BaseDigestPasswordEncoder> encoders;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(encoders, "encoders must be set !!!");
	}

	public Map<String, BaseDigestPasswordEncoder> getEncoders() {
		return encoders;
	}

	public void setEncoders(Map<String, BaseDigestPasswordEncoder> encoders) {
		this.encoders = encoders;
	}

	public BaseDigestPasswordEncoder getPasswordEncoder(PasswordAlgorithm algo) {
		Assert.notNull(encoders.get(algo.name()), algo + " Doesn't exists in encoders list !!!");

		return encoders.get(algo.name());
	}
}