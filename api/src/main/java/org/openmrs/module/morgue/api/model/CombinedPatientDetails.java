package org.openmrs.module.morgue.api.model;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonName;

public class CombinedPatientDetails {
	
	private Patient patient;
	
	private Person person;
	
	private PersonName personName;
	
	// Constructor
	public CombinedPatientDetails(Patient patient, Person person, PersonName personName) {
		this.patient = patient;
		this.person = person;
		this.personName = personName;
	}
	
	// Getters and Setters
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public PersonName getPersonName() {
		return personName;
	}
	
	public void setPersonName(PersonName personName) {
		this.personName = personName;
	}
}
