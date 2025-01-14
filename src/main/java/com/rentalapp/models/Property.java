package com.rentalapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
	private User landlord;

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

	@Column(nullable = false, name = "timePeriod")
	private String timePeriod;

	@Column(nullable = false, name = "roomCount")
	private int roomCount;

	@Column(nullable = false, name = "housingType")
	private String housingType;

	@Column(nullable = false, name = "area")
	private float area;

	@Column(nullable = false, name = "rating")
	private float rating;

	@Column(nullable = false, name = "publicationDate")
	private LocalDate publicationDate;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PropertyImage> images = new ArrayList<>();
}
