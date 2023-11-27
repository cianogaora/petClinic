package org.springframework.samples.petclinic.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.persistence.VetRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(VisitController.class)
class VisitControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VisitRepository visits;

	@MockBean
	private PetRepository pets;

	private Pet scooby;

	@MockBean
	private OwnerRepository owners;

	private Owner oliver;

	private Owner keiley;

	private Pet tabby;

	@BeforeEach
	void setup() {
		oliver = new Owner();
		oliver.setFirstName("Oliver");
		oliver.setLastName("Feinberg");
		oliver.setId(1);
		oliver.setAddress("2356 Sherbrooke");
		oliver.setCity("Montreal");
		oliver.setTelephone("5143458292");
		owners.save(oliver);

		scooby = new Pet();
		scooby.setName("Scooby");
		scooby.setBirthDate(LocalDate.of(2014, 9, 8));
		scooby.setId(3);
		scooby.setOwner(oliver);
		pets.save(scooby);
		PetType dogType = new PetType();
		dogType.setName("dog");
		scooby.setType(dogType);
		oliver.addPet(scooby);
		pets.save(scooby);

		keiley = new Owner();
		keiley.setFirstName("Keiley");
		keiley.setLastName("Feinberg");
		keiley.setId(2);
		keiley.setAddress("23 Aberdean");
		keiley.setCity("Kingston");
		keiley.setTelephone("4168234589");
		owners.save(keiley);

		tabby = new Pet();
		tabby.setBirthDate(LocalDate.of(2020, 01, 19));
		tabby.setId(6);
		tabby.setName("Tabby");
		tabby.setOwner(keiley);
		PetType catType = new PetType();
		dogType.setName("cat");
		tabby.setType(catType);
		keiley.addPet(tabby);
		pets.save(tabby);

		given(this.owners.findById(1)).willReturn(new Owner());
		given(this.pets.findById(3)).willReturn(scooby);
	}

	@Test
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", 1, 3)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@Test
	void testProcesNewVisitFormError() throws Exception {

		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", 1, 3).param("date", "2023-02-21").param("des",
				"Animal to come in for a visit")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("visit")).andExpect(view().name("pets/createOrUpdateVisitForm"));

	}

	@Test
	void testProcesNewVisitFormSuccess() throws Exception {

		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", 1, 3).param("date", "2024-03-04")
				.param("description", "some description")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

}
