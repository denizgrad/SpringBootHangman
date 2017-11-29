package com.dozen.hangman.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class AModel {
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Id
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return object has an id
	 */
	@JsonIgnore
	public boolean isNew () {
		return id == null;
	}
	
	//error 
//	@Transient
//	Integer code;
//	@Transient
//	String message;
//	
//	public void setError(Integer code, String message) {
//		this.code = code;
//		this.message = message;
//	}
	
}
