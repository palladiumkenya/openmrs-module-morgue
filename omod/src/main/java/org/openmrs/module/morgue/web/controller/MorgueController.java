/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.morgue.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PersonService;
import org.openmrs.api.UserService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.morgue.rest.controller.base.MorgueResourceController;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class configured as controller using annotation
 */
@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + MorgueResourceController.MORGUE_NAMESPACE)
@Authorized
public class MorgueController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	UserService userService;
	
	/**
	 * Gets a list of deceased patients in the morgue
	 * 
	 * @param request
	 * @param dateFrom - The From date
	 * @param dateTo - The To date
	 * @return
	 */
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
	        RequestMethod.OPTIONS })
	@RequestMapping(method = RequestMethod.GET, value = "/deceased")
	@ResponseBody
	public Object getListOfDeceasedPatients(HttpServletRequest request, @RequestParam("dateFrom") String dateFrom,
	        @RequestParam("dateTo") String dateTo) {
		Set<SimpleObject> deceased = new LinkedHashSet<SimpleObject>();
		VisitService service = Context.getVisitService();
		VisitType morgueVisit = service.getVisitTypeByUuid("02b67c47-6071-4091-953d-ad21452e830c");
		List<VisitType> visitTypes = new ArrayList<>();
		visitTypes.add(morgueVisit);
		List<Visit> visits = service.getVisits(visitTypes, null, null, null, null, null, null, null, null, false, false);
		
		for (Visit visit : visits) {
			SimpleObject model = new SimpleObject();
			
			model.put("patient", visit.getPatient().getUuid());
			deceased.add(model);
		}
		
		return deceased;
	}
}
