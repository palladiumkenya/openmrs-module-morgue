package org.openmrs.module.morgue.web.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.PatientResource1_9;
import org.openmrs.module.morgue.api.MorgueService;
import org.openmrs.module.morgue.api.model.CombinedPatientDetails;
import org.openmrs.module.morgue.api.model.MorguePatient;
import org.openmrs.module.morgue.rest.controller.base.MorgueResourceController;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
        RequestMethod.OPTIONS })
@Resource(name = RestConstants.VERSION_1 + MorgueResourceController.MORGUE_NAMESPACE + "/patient", supportedClass = MorguePatient.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.0 - 2.*" })
@Authorized
// public class MorguePatientResource extends PatientResource1_9 {
public class MorguePatientResource extends DelegatingCrudResource<CombinedPatientDetails> {
	
	@Override
	public CombinedPatientDetails newDelegate() {
		return new CombinedPatientDetails(null, null, null);
	}
	
	@Override
	public CombinedPatientDetails save(CombinedPatientDetails delegate) {
		throw new UnsupportedOperationException("Save is not supported for combined data.");
	}
	
	@Override
	public CombinedPatientDetails getByUniqueId(String uniqueId) {
		Integer patientId = Integer.parseInt(uniqueId);
		Patient patient = Context.getPatientService().getPatient(patientId);
		if (patient == null) {
			return null;
		}
		
		Person person = Context.getPersonService().getPerson(patientId);
		PersonName personName = person != null ? person.getPersonName() : null;
		
		return new CombinedPatientDetails(patient, person, personName);
	}
	
	@Override
	public void delete(CombinedPatientDetails delegate, String reason, RequestContext context) {
		throw new UnsupportedOperationException("Delete is not supported for combined data.");
	}
	
	@Override
	public void purge(CombinedPatientDetails delegate, RequestContext context) {
		throw new UnsupportedOperationException("Purge is not supported for combined data.");
	}
	
	@Override
	protected AlreadyPaged<CombinedPatientDetails> doSearch(RequestContext context) {
		String uuid = context.getRequest().getParameter("uuid");
		String name = context.getRequest().getParameter("name");
		String createdOnOrBeforeDateStr = context.getRequest().getParameter("createdOnOrBefore");
		String createdOnOrAfterDateStr = context.getRequest().getParameter("createdOnOrAfter");
		String dead = context.getRequest().getParameter("dead");
		
		Date createdOnOrBeforeDate = StringUtils.isNotBlank(createdOnOrBeforeDateStr) ? (Date) ConversionUtil.convert(
		    createdOnOrBeforeDateStr, Date.class) : null;
		Date createdOnOrAfterDate = StringUtils.isNotBlank(createdOnOrAfterDateStr) ? (Date) ConversionUtil.convert(
		    createdOnOrAfterDateStr, Date.class) : null;
		
		MorgueService service = Context.getService(MorgueService.class);
		List<Object[]> result = service.getPatients(dead, name, uuid, createdOnOrAfterDate, createdOnOrBeforeDate);
		
		// Apply pagination
		int start = context.getStartIndex();
		int limit = context.getLimit();
		int total = result.size();
		int toIndex = Math.min(start + limit, total);
		List<Object[]> paginatedList = result.subList(Math.min(start, total), toIndex);
		System.out.println("Start index is: " + start + " : And Limit is: " + limit + " : Total is: " + total + " : To index is: " + toIndex);
		
		List<CombinedPatientDetails> combinedDetailsList = new ArrayList<>();
		for (Object[] ob : paginatedList) {
			Patient patient = (Patient) ob[0];
			Person person = (Person) ob[1];
			PersonName personName = (PersonName) ob[2];
			Integer personNameId = (Integer) ob[3];
			String givenName = (String) ob[4];
			String middleName = (String) ob[5];
			String familyName = (String) ob[6];
			Boolean preferred = (Boolean) ob[7];
			System.out.println("Result Got: " + patient.getId() + " : " + person.getId() + " : " + personName.getId() + " : " + personNameId + " : "
			        + givenName + " : " + middleName + " : " + familyName + " : " + preferred);
			CombinedPatientDetails combinedPatientDetails = new CombinedPatientDetails(patient, person, personName);
			combinedDetailsList.add(combinedPatientDetails);
		}
		
		long totalList = total;
		// Determine if there are more results
		boolean hasMoreResults = start + limit < total;
		System.out.println("Has more results is: " + hasMoreResults);
		return new AlreadyPaged<CombinedPatientDetails>(context, combinedDetailsList, hasMoreResults, totalList);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("patient", Representation.DEFAULT);
		description.addProperty("person", Representation.DEFAULT);
		description.addProperty("personName", Representation.DEFAULT);
		return description;
	}
	
	@Override
	public String getUri(Object instance) {
		CombinedPatientDetails combinedDetails = (CombinedPatientDetails) instance;
		return "v1/combinedpatientdetails/" + combinedDetails.getPatient().getId();
	}
	
	@Override
	public String getResourceVersion() {
		return "1.0";
	}
}
