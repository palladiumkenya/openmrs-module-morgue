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
import java.util.Date;
import java.util.List;
import org.openmrs.Patient;
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
	public List<Patient> getPatients(String dead, String name, String uuid, Date createdOnOrAfterDate,
	        Date createdOnOrBeforeDate) {
		return dao.getPatients(dead, name, uuid, createdOnOrAfterDate, createdOnOrBeforeDate);
	}
	
}
