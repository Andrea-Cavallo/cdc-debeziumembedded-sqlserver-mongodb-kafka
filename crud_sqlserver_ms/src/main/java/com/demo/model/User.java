package com.demo.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements BaseEntity {

	public static final int VARCHAR_MAX_LENGTH = 4096;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	@Column(nullable = false, length = VARCHAR_MAX_LENGTH)
	private String name;

	@Column(nullable = false, length = VARCHAR_MAX_LENGTH)
	private String surname;

	@OneToMany(mappedBy = "user")
	private List<Order> orders;

}
