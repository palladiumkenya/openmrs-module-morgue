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
import org.openmrs.module.morgue.api.model.MorguePatient;
import org.openmrs.module.morgue.rest.controller.base.MorgueResourceController;

import org.openmrs.module.morgue.api.model.MorgueDeceased;
import org.openmrs.module.morgue.api.MorgueService;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
        RequestMethod.OPTIONS })
@Resource(name = RestConstants.VERSION_1 + MorgueResourceController.MORGUE_NAMESPACE + "/deceased", supportedClass = MorguePatient.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.0 - 2.*" })
@Authorized
public class DeceasedResource extends DataDelegatingCrudResource<MorgueDeceased> {
	
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
			description.addProperty("dateOfAdmission");
			description.addProperty("patient", Representation.REF);
			description.addProperty("sex");
			description.addProperty("residence");
			description.addProperty("age");
			description.addProperty("timeOfDeath");
			description.addProperty("tagNumber");
			description.addProperty("obNumberForPoliceCase");
			description.addProperty("dateOfDeath");
			description.addProperty("timeOfAdmission");
			description.addProperty("discharge");
			description.addProperty("dateOfDischarge");
			description.addProperty("nextOfKinName");
			description.addProperty("amountPaid");
			description.addProperty("burialPermitSerialNumber");
			description.addProperty("nextOfKinId");
			description.addProperty("relationshipWithNextOfKin");
			description.addProperty("voided");
			description.addSelfLink();
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			
			return description;
		} else if (rep instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("uuid");
			description.addProperty("dateOfAdmission");
			description.addProperty("patient", Representation.REF);
			description.addProperty("sex");
			description.addProperty("residence");
			description.addProperty("age");
			description.addProperty("timeOfDeath");
			description.addProperty("tagNumber");
			description.addProperty("obNumberForPoliceCase");
			description.addProperty("dateOfDeath");
			description.addProperty("timeOfAdmission");
			description.addProperty("discharge");
			description.addProperty("dateOfDischarge");
			description.addProperty("nextOfKinName");
			description.addProperty("amountPaid");
			description.addProperty("burialPermitSerialNumber");
			description.addProperty("nextOfKinId");
			description.addProperty("relationshipWithNextOfKin");
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
	
	// // @Override
	// // protected PageableResult doGetAll(RequestContext context) {
	// // 	return new NeedsPaging<Patient>(Context.getPatientService().getAllPatients(), context);
	// // }
	
	// @Override
	// protected AlreadyPaged<Patient> doSearch(RequestContext context) {
	// 	// String uuid = context.getRequest().getParameter("uuid");
	// 	// String status = context.getRequest().getParameter("status");
	//     // String type = context.getRequest().getParameter("type");
	//     // String withErrors = context.getRequest().getParameter("withErrors");
	// 	// String createdOnOrBeforeDateStr = context.getRequest().getParameter("createdOnOrBefore");
	// 	// String createdOnOrAfterDateStr = context.getRequest().getParameter("createdOnOrAfter");
	// 	String dead = context.getRequest().getParameter("dead");
	
	// 	// Date createdOnOrBeforeDate = StringUtils.isNotBlank(createdOnOrBeforeDateStr) ? (Date) ConversionUtil.convert(createdOnOrBeforeDateStr, Date.class) : null;
	// 	// Date createdOnOrAfterDate = StringUtils.isNotBlank(createdOnOrAfterDateStr) ? (Date) ConversionUtil.convert(createdOnOrAfterDateStr, Date.class) : null;
	
	// 	PatientService patientService = Context.getPatientService();
	
	//     List<Patient> result = new ArrayList<>();
	// 	// List<Patient> result = service.getPatients(uuid, status, type, withErrors, createdOnOrAfterDate, createdOnOrBeforeDate);
	
	// 	if(dead != null && dead.trim().equalsIgnoreCase("true")) {
	// 		List<Patient> allPatients = patientService.getAllPatients();
	
	// 		// Filter out dead patients
	// 		result = allPatients.stream()
	// 			.filter(patient -> patient.getDead() && !patient.getVoided())  // Filter patients where getDead() is true
	// 			.collect(Collectors.toList());
	// 	} else if(dead != null && dead.trim().equalsIgnoreCase("false")) {
	// 		List<Patient> allPatients = patientService.getAllPatients();
	
	// 		// Filter out alive patients
	// 		result = allPatients.stream()
	//         .filter(patient -> !patient.getDead() && !patient.getVoided())  // Filter patients where getDead() is false
	//         .collect(Collectors.toList());
	// 	} else {
	// 		result = patientService.getAllPatients();
	// 	}
	
	// 	return new AlreadyPaged<Patient>(context, result, false);
	// }
	
	/**
	 * Gets the MorgueDeceased record by its unique identifier (UUID)
	 */
	@Override
	public MorgueDeceased getByUniqueId(String uniqueId) {
		return Context.getService(MorgueService.class).getDeceasedByUuid(uniqueId);
	}
	
	/**
	 * Creates a new instance of MorgueDeceased
	 */
	@Override
	public MorgueDeceased newDelegate() {
		return new MorgueDeceased();
	}
	
	/**
	 * Saves the MorgueDeceased record
	 */
	@Override
	public MorgueDeceased save(MorgueDeceased morgueDeceased) {
		return Context.getService(MorgueService.class).saveDeceased(morgueDeceased);
	}
	
	/**
	 * Deletes (voids) the MorgueDeceased record
	 */
	@Override
	protected void delete(MorgueDeceased morgueDeceased, String reason, RequestContext context) throws ResponseException {
		morgueDeceased.setVoided(true);
		Context.getService(MorgueService.class).saveDeceased(morgueDeceased);
	}
	
	/**
	 * Purges the MorgueDeceased record permanently
	 */
	@Override
	public void purge(MorgueDeceased morgueDeceased, RequestContext context) throws ResponseException {
		morgueDeceased.setVoided(true);
		Context.getService(MorgueService.class).saveDeceased(morgueDeceased);
	}
	
	// /**
	//  * Defines which properties to include in the "default" REST representation
	//  */
	// @Override
	// public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
	//     if (rep instanceof DefaultRepresentation) {
	//         DelegatingResourceDescription description = new DelegatingResourceDescription();
	//         description.addProperty("uuid");
	//         description.addProperty("dateOfAdmission");
	//         description.addProperty("patient");
	//         description.addProperty("sex");
	//         description.addProperty("residence");
	//         description.addProperty("age");
	//         description.addProperty("timeOfDeath");
	//         description.addProperty("tagNumber");
	//         description.addProperty("obNumberForPoliceCase");
	//         description.addProperty("dateOfDeath");
	//         description.addProperty("timeOfAdmission");
	//         description.addProperty("discharge");
	//         description.addProperty("dateOfDischarge");
	//         description.addProperty("nextOfKinName");
	//         description.addProperty("amountPaid");
	//         description.addProperty("burialPermitSerialNumber");
	//         description.addProperty("nextOfKinId");
	//         description.addProperty("relationshipWithNextOfKin");
	//         description.addSelfLink();
	//         return description;
	//     } else if (rep instanceof FullRepresentation) {
	//         DelegatingResourceDescription description = getRepresentationDescription(new DefaultRepresentation());
	//         description.addProperty("auditInfo");
	//         return description;
	//     }
	//     return null;
	// }
	
	/**
	 * Fetch the unique ID for the resource
	 */
	@Override
	public String getUniqueId(MorgueDeceased morgueDeceased) {
		return morgueDeceased.getUuid();
	}
	
	// /**
	//  * Defines the search query for REST
	//  */
	// @Override
	// protected List<MorgueDeceased> doSearch(String query, org.openmrs.module.webservices.rest.web.request.RequestContext context) {
	//     return Context.getService(MorgueService.class).searchMorgueDeceased(query);
	// }
	
	/**
	 * Defines the properties that can be set when creating or updating a resource
	 */
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("dateOfAdmission");
		description.addProperty("patient");
		description.addProperty("sex");
		description.addProperty("residence");
		description.addProperty("age");
		description.addProperty("timeOfDeath");
		description.addProperty("tagNumber");
		description.addProperty("obNumberForPoliceCase");
		description.addProperty("dateOfDeath");
		description.addProperty("timeOfAdmission");
		description.addProperty("discharge");
		description.addProperty("dateOfDischarge");
		description.addProperty("nextOfKinName");
		description.addProperty("amountPaid");
		description.addProperty("burialPermitSerialNumber");
		description.addProperty("nextOfKinId");
		description.addProperty("relationshipWithNextOfKin");
		return description;
	}
}
