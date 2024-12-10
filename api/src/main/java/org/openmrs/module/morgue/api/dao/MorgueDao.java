/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.morgue.api.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonName;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmrs.PersonName;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.DataException;
import org.hibernate.transform.Transformers;
import org.openmrs.Cohort;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.util.PrivilegeConstants;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

// @Repository("morgue.MorgueDao")
public class MorgueDao {
	
	private SessionFactory sessionFactory;
	
	/**
	 * @Autowired private LabOrderManifestDao labOrderManifestDao;
	 */
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * Get patients // SELECT p.patient_id, pr.person_id, pn.person_name_id, pn.given_name,
	 * pn.middle_name, pn.family_name, pn.preferred FROM patient p JOIN person pr ON p.patient_id =
	 * pr.person_id LEFT JOIN person_name pn ON pr.person_id = pn.person_id WHERE pn.voided=0 and
	 * pr.voided=0 and (pn.given_name like "%john%" or pn.middle_name like "%john%" or
	 * pn.family_name like "%john%");
	 * 
	 * @param dead
	 * @param name
	 * @param uuid
	 * @param createdOnOrAfterDate
	 * @param createdOnOrBeforeDate
	 * @return
	 */
	public List<Object[]> getPatients(String dead, String name, String uuid, Date createdOnOrAfterDate,
	        Date createdOnOrBeforeDate) {
		System.err.println("Morgue Searching for patients using: " + uuid + " : " + name + " : " + dead + " : "
		        + createdOnOrAfterDate + " : " + createdOnOrBeforeDate + " : ");
		
		Session session = this.sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		
		// Create CriteriaQuery and specify the result type
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		
		// Define root entities
		Root<Patient> patientRoot = criteriaQuery.from(Patient.class);
		Root<Person> personRoot = criteriaQuery.from(Person.class);
		Root<PersonName> personNameRoot = criteriaQuery.from(PersonName.class);
		
		//Join 
		// Join<Patient, PersonName> personNameJoin = root.join("patientId");
		Predicate joinCondition1 = criteriaBuilder.equal(patientRoot.get("patientId"), personRoot.get("personId"));
		Predicate joinCondition2 = criteriaBuilder.equal(personRoot.get("personId"), personNameRoot.get("person"));
		
		// Define selection (include full patient and person details)
		criteriaQuery.multiselect(
		    patientRoot, // Full Patient details
		    personRoot, // Full Person details
		    personNameRoot, // Full Person Name details
		    personNameRoot.get("personNameId"), personNameRoot.get("givenName"), personNameRoot.get("middleName"),
		    personNameRoot.get("familyName"), personNameRoot.get("preferred"));
		
		// Create predicates for the restrictions
		Predicate predicate = criteriaBuilder.conjunction();
		
		// Add the Joins
		predicate = criteriaBuilder.and(predicate, joinCondition1, joinCondition2);
		
		// uuid
		if (uuid != null && !uuid.isEmpty()) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(patientRoot.get("uuid"), uuid));
		}
		
		// name
		if (name != null && !name.isEmpty()) {
			name = name.trim().toLowerCase();
			String searchPattern = "%" + name + "%";
			Predicate givenNamePredicate = criteriaBuilder.like(personNameRoot.get("givenName"), searchPattern);
			Predicate middleNamePredicate = criteriaBuilder.like(personNameRoot.get("middleName"), searchPattern);
			Predicate familyNamePredicate = criteriaBuilder.like(personNameRoot.get("familyName"), searchPattern);
			Predicate namePredicate = criteriaBuilder.or(givenNamePredicate, middleNamePredicate, familyNamePredicate);
			
			predicate = criteriaBuilder.and(predicate, namePredicate);
		}
		
		// dead
		if (dead != null && !dead.isEmpty()) {
			Integer isDead = 0;
			if (dead.trim().equalsIgnoreCase("true")) {
				isDead = 1;
			} else if (dead.trim().equalsIgnoreCase("false")) {
				isDead = 0;
			}
			
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(personRoot.get("dead"), isDead));
		}
		
		// createdOnOrAfterDate
		if (createdOnOrAfterDate != null) {
			Path<Date> datePath = patientRoot.get("dateCreated");
			
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(datePath, createdOnOrAfterDate));
		}
		
		// createdOnOrBeforeDate
		if (createdOnOrBeforeDate != null) {
			Path<Date> datePath = patientRoot.get("dateCreated");
			
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(datePath, createdOnOrBeforeDate));
		}
		
		// voided
		Predicate voidedPredicate = criteriaBuilder.and(criteriaBuilder.equal(personNameRoot.get("voided"), 0),
		    criteriaBuilder.equal(personRoot.get("personVoided"), 0));
		predicate = criteriaBuilder.and(predicate, voidedPredicate);
		
		// criteriaQuery.where(predicate);
		criteriaQuery.where(predicate).distinct(true);
		
		// Print the generated SQL query
		Query query = session.createQuery(criteriaQuery);
		String sqlQuery = query.unwrap(org.hibernate.query.Query.class).getQueryString();
		System.out.println("Generated SQL Query: " + sqlQuery);
		
		List<Object[]> results = session.createQuery(criteriaQuery).getResultList();
		
		return (results);
	}
}
