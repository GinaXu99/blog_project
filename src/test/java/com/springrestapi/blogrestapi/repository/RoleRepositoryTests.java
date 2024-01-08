package com.springrestapi.blogrestapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.springrestapi.blogrestapi.entity.Role;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class RoleRepositoryTests {
	
	@Autowired 
	private RoleRepository roleRepository;
	
	private Role role;
	
	@BeforeEach
	public void setup() {
		role = Role.builder()
				.id(1L)
				.name("ADMIN")
				.build();
	}
	
	@Test
	@DisplayName("Test find role by name")
	public void findRoleByName() {
		roleRepository.save(role);
		Role result = roleRepository.findByName(role.getName()).get();
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualToIgnoringCase("ADMIN");
	}
}
