package org.openmrs.module.morgue.web.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs1_9.PatientResource1_9;
import org.openmrs.module.morgue.api.MorgueService;
import org.openmrs.module.morgue.api.model.MorguePatient;
import org.openmrs.module.morgue.rest.controller.base.MorgueResourceController;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
        RequestMethod.OPTIONS })
@Resource(name = RestConstants.VERSION_1 + MorgueResourceController.MORGUE_NAMESPACE + "/patient", supportedClass = MorguePatient.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.0 - 2.*" })
@Authorized
public class MorguePatientResource extends PatientResource1_9 {
	
	// @Override
	// public Patient getByUniqueId(String uniqueId) {
	// 	return Context.getPatientService().getPatientByUuid(uniqueId);
	// }
	
	// @Override
	// public void delete(Patient patient, String reason, RequestContext context) throws ResponseException {
	// 	patient.setVoided(true);
	// 	patient.setVoidReason(reason);
	// 	Context.getPatientService().savePatient(patient);
	// }
	
	// @Override
	// public Patient newDelegate() {
	// 	return new Patient();
	// }
	
	// @Override
	// public Patient save(Patient patient) {
	// 	return Context.getPatientService().savePatient(patient);
	// }
	
	// @Override
	// public DelegatingResourceDescription getCreatableProperties() {
	// 	DelegatingResourceDescription description = new DelegatingResourceDescription();
	
	// 	description.addProperty("identifiers");
	// 	description.addProperty("person");
	// 	return description;
	// }
	
	// @Override
	// public void purge(Patient patient, RequestContext context) throws ResponseException {
	// 	patient.setVoided(true);
	// 	Context.getPatientService().savePatient(patient);
	// }
	
	// @Override
	// public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
	// 	System.out.println("Openmrs Morgue: Getting patient data");
	// 	if (representation instanceof DefaultRepresentation) {
	// 		DelegatingResourceDescription description = new DelegatingResourceDescription();
	// 		description.addProperty("uuid");
	// 		description.addProperty("identifiers");
	// 		// String customRep = "(uuid,status,result,batchNumber,dateSent,resultDate,sampleType,order:(patient:(id,uuid,identifiers:(identifier,uuid))))";
	// 		// Representation rep = new CustomRepresentation(customRep);
	// 		// description.addProperty("person", rep);
	// 		description.addProperty("person", Representation.REF);
	// 		description.addSelfLink();
	
	// 		return description;
	// 	} else if (representation instanceof FullRepresentation) {
	// 		DelegatingResourceDescription description = new DelegatingResourceDescription();
	// 		description.addProperty("uuid");
	// 		description.addProperty("identifiers");
	// 		description.addProperty("person", Representation.REF);
	// 		description.addSelfLink();
	// 		description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
	
	// 		return description;
	// 	} else if (representation instanceof RefRepresentation) {
	// 		DelegatingResourceDescription description = new DelegatingResourceDescription();
	// 		description.addProperty("uuid");
	// 		description.addProperty("identifiers");
	// 		description.addProperty("person", Representation.REF);
	// 		description.addSelfLink();
	
	// 		return description;
	// 	}
	// 	return null;
	// }
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (rep instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("identifiers", Representation.REF);
			description.addProperty("person", Representation.DEFAULT);
			description.addProperty("voided");
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			
			return description;
		} else if (rep instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("display");
			description.addProperty("identifiers", Representation.DEFAULT);
			description.addProperty("person", Representation.FULL);
			description.addProperty("voided");
			description.addProperty("auditInfo");
			description.addSelfLink();
			
			return description;
		} else if (rep instanceof RefRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			// description.addProperty("identifiers");
			// description.addProperty("person", Representation.REF);
			description.addSelfLink();
			
			return description;
		}
		return null;
	}
	
	// @Override
	// protected PageableResult doGetAll(RequestContext context) {
	// 	return new NeedsPaging<Patient>(Context.getPatientService().getAllPatients(), context);
	// }
	
	@Override
	protected AlreadyPaged<Patient> doSearch(RequestContext context) {
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
		List<Patient> result = service.getPatients(dead, name, uuid, createdOnOrAfterDate, createdOnOrBeforeDate);
		
		return new AlreadyPaged<Patient>(context, result, false);
	}
}
