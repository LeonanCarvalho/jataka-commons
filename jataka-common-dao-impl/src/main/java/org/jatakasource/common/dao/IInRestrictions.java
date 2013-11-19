package org.jatakasource.common.dao;

import java.util.List;

public interface IInRestrictions {
	<X, ID> List<X> getSingleBlock(List<ID> ids);
}
