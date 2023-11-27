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

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hamcrest.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.persistence.VetRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.persistence.VetRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(PetController.class)
class PetControllerTests {

	@Autowired
	private MockMvc mockMvc;

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	@MockBean
	private PetRepository pets;

	private Pet scooby;

	private Owner greg;

	@MockBean
	private OwnerRepository owners;

	private Owner oliver;

	private Owner keiley;

	private Pet tabby;

	private PetType dogType;

	private PetType catType;

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
		dogType = new PetType();
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
		catType = new PetType();
		catType.setName("cat");
		tabby.setType(catType);
		pets.save(tabby);
		keiley.addPet(tabby);

		PetType dog = new PetType();
		dog.setId(2);
		dog.setName("dog");

		given(this.owners.findById(1)).willReturn(new Owner());
		given(this.pets.findPetTypes()).willReturn(Lists.newArrayList(catType, dogType));
		given(this.owners.findById(1)).willReturn(oliver);
		given(this.owners.findById(2)).willReturn(keiley);
		given(this.pets.findById(6)).willReturn(tabby);
		given(this.pets.findById(3)).willReturn(scooby);
	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", 1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("pet")).andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));

	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", 1).param("name", "dope").param("type.name", "dog")
				.param("birthDate", "2020-04-15")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void testProcessCreationFormDuplicate() throws Exception {
		given(this.owners.findById(1)).willReturn(oliver);
		given(this.pets.findById(3)).willReturn(scooby);
		mockMvc.perform(post("/owners/{ownerId}/pets/new", 1).param("name", "Scooby").param("type.name", "dog"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "birthDate"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "required"))
				.andExpect(model().attributeHasNoErrors("owner")).andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	@Test
	void testProcessCreationFormFailure() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", 1).param("name", "scooby").param("type.name", "dog"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(model().attributeHasFieldErrors("pet", "birthDate"))
				.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "required"))
				.andExpect(model().attributeHasNoErrors("owner")).andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", 1, 3)).andExpect(status().isOk())
				.andExpect(model().attributeExists("pet")).andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		given(this.owners.findById(1)).willReturn(oliver);
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", 1, 3).param("name", "Scooby")
				.param("type.name", "dog").param("birthDate", "2020-02-01"))
				.andExpect(view().name("redirect:/owners/{ownerId}")).andExpect(status().is3xxRedirection());
	}

	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).param("name", "Scooby").param("birthDate",
				"2020/02/07")).andExpect(model().attributeHasNoErrors("owner"))
				.andExpect(model().attributeHasErrors("pet")).andExpect(status().isOk())
				.andExpect(view().name(VIEWS_PETS_CREATE_OR_UPDATE_FORM));
	}

}
