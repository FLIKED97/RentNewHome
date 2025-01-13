package com.rentalapp.models;

import lombok.Getter;

@Getter
public enum UserRole {
	TENANT("ROLE_TENANT"),
	LANDLORD("ROLE_LANDLORD"),
	ADMIN("ROLE_ADMIN");

	private final String value;

	UserRole(String value) {
		this.value = value;
	}
}