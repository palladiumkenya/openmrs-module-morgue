/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.morgue.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.UserService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.morgue.api.model.MorgueDeceased;
import org.openmrs.module.morgue.api.MorgueService;
import org.openmrs.module.morgue.api.dao.MorgueDao;

public class MorgueServiceImpl extends BaseOpenmrsService implements MorgueService {
	
	MorgueDao dao;
	
	UserService userService;
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(MorgueDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public MorgueDeceased getDeceasedByUuid(String uuid) throws APIException {
		return dao.getDeceasedByUuid(uuid);
	}
	
	@Override
	public MorgueDeceased saveDeceased(MorgueDeceased deceased) throws APIException {
		return dao.saveDeceased(deceased);
	}
}
