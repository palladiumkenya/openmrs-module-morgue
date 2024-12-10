package org.openmrs.module.morgue.api.model;

import java.io.Serializable;
import java.util.Date;

import org.openmrs.Patient;
import org.openmrs.BaseOpenmrsData;

public class MorgueDeceased extends BaseOpenmrsData implements Serializable {
	
	private static final long serialVersionUID = 1L; // Required for Serializable
	
	private Integer id;
	
	private Date dateOfAdmission;
	
	private Patient patient;
	
	private String sex;
	
	private String residence;
	
	private Integer age;
	
	private Date timeOfDeath;
	
	private String tagNumber;
	
	private String obNumberForPoliceCase;
	
	private Date dateOfDeath;
	
	private Date timeOfAdmission;
	
	private Boolean discharge;
	
	private Date dateOfDischarge;
	
	private String nextOfKinName;
	
	private Double amountPaid;
	
	private String burialPermitSerialNumber;
	
	private String nextOfKinId;
	
	private String relationshipWithNextOfKin;
	
	// Constructor
	public MorgueDeceased() {
		//
	}
	
	// Constructor
	public MorgueDeceased(Date dateOfAdmission, Patient patient, String sex, String residence, Integer age,
	    Date timeOfDeath, String tagNumber, String obNumberForPoliceCase, Date dateOfDeath, Date timeOfAdmission,
	    Boolean discharge, Date dateOfDischarge, String nextOfKinName, Double amountPaid, String burialPermitSerialNumber,
	    String nextOfKinId, String relationshipWithNextOfKin) {
		this.dateOfAdmission = dateOfAdmission;
		this.patient = patient;
		this.sex = sex;
		this.residence = residence;
		this.age = age;
		this.timeOfDeath = timeOfDeath;
		this.tagNumber = tagNumber;
		this.obNumberForPoliceCase = obNumberForPoliceCase;
		this.dateOfDeath = dateOfDeath;
		this.timeOfAdmission = timeOfAdmission;
		this.discharge = discharge;
		this.dateOfDischarge = dateOfDischarge;
		this.nextOfKinName = nextOfKinName;
		this.amountPaid = amountPaid;
		this.burialPermitSerialNumber = burialPermitSerialNumber;
		this.nextOfKinId = nextOfKinId;
		this.relationshipWithNextOfKin = relationshipWithNextOfKin;
	}
	
	// Getters and Setters
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getDateOfAdmission() {
		return dateOfAdmission;
	}
	
	public void setDateOfAdmission(Date dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getResidence() {
		return residence;
	}
	
	public void setResidence(String residence) {
		this.residence = residence;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Date getTimeOfDeath() {
		return timeOfDeath;
	}
	
	public void setTimeOfDeath(Date timeOfDeath) {
		this.timeOfDeath = timeOfDeath;
	}
	
	public String getTagNumber() {
		return tagNumber;
	}
	
	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}
	
	public String getObNumberForPoliceCase() {
		return obNumberForPoliceCase;
	}
	
	public void setObNumberForPoliceCase(String obNumberForPoliceCase) {
		this.obNumberForPoliceCase = obNumberForPoliceCase;
	}
	
	public Date getDateOfDeath() {
		return dateOfDeath;
	}
	
	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}
	
	public Date getTimeOfAdmission() {
		return timeOfAdmission;
	}
	
	public void setTimeOfAdmission(Date timeOfAdmission) {
		this.timeOfAdmission = timeOfAdmission;
	}
	
	public Boolean getDischarge() {
		return discharge;
	}
	
	public void setDischarge(Boolean discharge) {
		this.discharge = discharge;
	}
	
	public Date getDateOfDischarge() {
		return dateOfDischarge;
	}
	
	public void setDateOfDischarge(Date dateOfDischarge) {
		this.dateOfDischarge = dateOfDischarge;
	}
	
	public String getNextOfKinName() {
		return nextOfKinName;
	}
	
	public void setNextOfKinName(String nextOfKinName) {
		this.nextOfKinName = nextOfKinName;
	}
	
	public Double getAmountPaid() {
		return amountPaid;
	}
	
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}
	
	public String getBurialPermitSerialNumber() {
		return burialPermitSerialNumber;
	}
	
	public void setBurialPermitSerialNumber(String burialPermitSerialNumber) {
		this.burialPermitSerialNumber = burialPermitSerialNumber;
	}
	
	public String getNextOfKinId() {
		return nextOfKinId;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public void setNextOfKinId(String nextOfKinId) {
		this.nextOfKinId = nextOfKinId;
	}
	
	public String getRelationshipWithNextOfKin() {
		return relationshipWithNextOfKin;
	}
	
	public void setRelationshipWithNextOfKin(String relationshipWithNextOfKin) {
		this.relationshipWithNextOfKin = relationshipWithNextOfKin;
	}
	
}
