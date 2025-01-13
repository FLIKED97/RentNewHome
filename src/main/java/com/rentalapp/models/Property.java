package com.rentalapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "landlord_id", nullable = false)
	private User landlord;  // Замінено Landlord на User

	@Column(nullable = false, name = "title")
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT", name = "description")
	private String description;

	@Column(nullable = false, name = "price")
	private float price;

	@Column(nullable = false, name = "location")
	private String location;

	@Column(nullable = false, name = "status")
	private String status;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PropertyImage> images = new ArrayList<>();
}
