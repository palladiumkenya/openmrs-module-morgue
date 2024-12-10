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
	
	public List<Patient> getPatients(String dead, String name, String uuid, Date createdOnOrAfterDate,
	        Date createdOnOrBeforeDate) {
		System.err.println("Morgue Searching for patients using: " + uuid + " : " + name + " : " + dead + " : "
		        + createdOnOrAfterDate + " : " + createdOnOrBeforeDate + " : ");
		
		Session session = this.sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Patient> criteriaQuery = criteriaBuilder.createQuery(Patient.class);
		Root<Patient> root = criteriaQuery.from(Patient.class);
		
		//Join 
		Join<Patient, PersonName> personNameJoin = root.join("personName");
		
		// Create predicates for the restrictions
		Predicate predicate = criteriaBuilder.conjunction();
		
		// uuid
		if (uuid != null && !uuid.isEmpty()) {
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("uuid"), uuid));
		}
		
		// name
		if (name != null && !name.isEmpty()) {
			name = name.trim().toLowerCase();
			String searchPattern = "%" + name + "%"; // For case-sensitive match. Use "%john%" for case-insensitive in some databases.
			Predicate givenNamePredicate = criteriaBuilder.like(personNameJoin.get("givenName"), searchPattern);
			Predicate middleNamePredicate = criteriaBuilder.like(personNameJoin.get("middleName"), searchPattern);
			Predicate familyNamePredicate = criteriaBuilder.like(personNameJoin.get("familyName"), searchPattern);
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
			
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("dead"), isDead));
		}
		
		// createdOnOrAfterDate
		if (createdOnOrAfterDate != null) {
			Path<Date> datePath = root.get("dateCreated");
			
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(datePath, createdOnOrAfterDate));
		}
		
		// createdOnOrBeforeDate
		if (createdOnOrBeforeDate != null) {
			Path<Date> datePath = root.get("dateCreated");
			
			predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(datePath, createdOnOrBeforeDate));
		}
		
		// criteriaQuery.where(predicate);
		criteriaQuery.where(predicate).distinct(true);
		
		// Print the generated SQL query
		Query query = session.createQuery(criteriaQuery);
		String sqlQuery = query.unwrap(org.hibernate.query.Query.class).getQueryString();
		System.out.println("Generated SQL Query: " + sqlQuery);
		
		List<Patient> results = session.createQuery(criteriaQuery).getResultList();
		
		return (results);
	}
}
