/*
 * Copyright 2012-2019 the original author or authors.
 */
package org.springframework.samples.petclinic.controller;

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

/**
 * Test class for the {@link OwnerController}
 */
@WebMvcTest(OwnerController.class)
class OwnerControllerTests {

	/*
	 * public static String asJsonString(final Object obj) { try { return new
	 * ObjectMapper().writeValueAsString(obj); } catch (Exception e) { throw new
	 * RuntimeException(e); } }
	 */

	@Autowired
	private MockMvc mockMvc;

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	@MockBean
	private VisitRepository visits;

	@MockBean
	private OwnerRepository owners;

	private Owner oliver;

	private Owner keiley;

	private Pet scooby;

	private Pet tabby;

	private Visit visita;

	private Visit visitb;

	private Set<Visit> visits2;

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

		keiley = new Owner();
		keiley.setFirstName("Keiley");
		keiley.setLastName("Feinberg");
		keiley.setId(2);
		keiley.setAddress("23 Aberdean");
		keiley.setCity("Kingston");
		keiley.setTelephone("4168234589");
		owners.save(keiley);

		scooby = new Pet();
		scooby.setName("Scooby");
		scooby.setBirthDate(LocalDate.of(2014, 9, 8));
		scooby.setId(3);
		scooby.setOwner(oliver);

		PetType dogType = new PetType();
		dogType.setName("dog");
		scooby.setType(dogType);

		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.of(2016, 8, 7));
		visit1.setDescription("First visit");
		visit1.setId(4);
		visit1.setPetId(3);

		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2019, 2, 15));
		visit2.setDescription("dog got some vaccinations today");
		visit2.setId(5);
		visit2.setPetId(3);

		Set<Visit> visits1 = new LinkedHashSet<>();
		visits1.add(visit1);
		visits1.add(visit2);
		scooby.setVisitsInternal(visits1);

		tabby = new Pet();
		tabby.setBirthDate(LocalDate.of(2020, 01, 19));
		tabby.setId(6);
		tabby.setName("Tabby");
		tabby.setOwner(keiley);

		PetType catType = new PetType();
		dogType.setName("cat");
		tabby.setType(catType);

		visita = new Visit();
		visita.setDate(LocalDate.of(2016, 6, 9));
		visita.setDescription("First visit");
		visita.setId(7);
		visita.setPetId(6);
		visits.save(visita);

		visitb = new Visit();
		visitb.setDate(LocalDate.of(2019, 9, 15));
		visitb.setDescription("cat got some vaccinations today");
		visitb.setId(8);
		visitb.setPetId(6);
		visits.save(visitb);

		visits2 = new LinkedHashSet<>();
		visits2.add(visitb);
		visits2.add(visita);
		tabby.setVisitsInternal(visits2);

		Set<Pet> pets1 = new HashSet<>();
		pets1.add(scooby);
		oliver.setPetsInternal(Collections.singleton(scooby));

		Set<Pet> pets2 = new HashSet<>();
		pets2.add(tabby);

		keiley.setPetsInternal(Collections.singleton(tabby));

		given(this.owners.findById(1)).willReturn(oliver);
		given(this.owners.findById(2)).willReturn(keiley);
		given(this.owners.findByLastName("Fienberg")).willReturn(Lists.newArrayList(oliver, keiley));

		this.mockMvc = MockMvcBuilders.standaloneSetup(new OwnerController(this.owners, this.visits)).build();

	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/new")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));
	}

	@Test
	void testProcessCreationFormFailure() throws Exception {
		Owner testOwner = new Owner();
		testOwner.setAddress("address1");
		testOwner.setCity("Ottawa");
		testOwner.setFirstName("Firstname");
		testOwner.setLastName("lastName");
		testOwner.setTelephone("1234");
		testOwner.setId(12);
		mockMvc.perform(post("/owners/new").content(testOwner.toString())).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));

	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		Owner testOwner = new Owner();
		testOwner.setAddress("address1");
		testOwner.setCity("Ottawa");
		testOwner.setFirstName("Firstname2");
		testOwner.setLastName("lastName");
		testOwner.setTelephone("1234");
		mockMvc.perform(post("/owners/new").param("firstName", "f4").param("lastName", "l4").param("address", "a4")
				.param("city", "c4").param("telephone", "5567")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/null")).andExpect(header().exists("Location"))
				.andExpect(header().string("Location", Matchers.containsString("/owners/null")));

	}

	@Test
	void testProcessCreationFormBadPhone() throws Exception {
		mockMvc.perform(post("/owners/new").param("firstName", "f4").param("lastName", "l4").param("address", "a4")
				.param("city", "c4").param("telephone", "ABC")).andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm")).andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"));
	}

	@Test
	void testProcessCreationFormMissing() throws Exception {
		mockMvc.perform(post("/owners/new").param("firstName", "f4").param("address", "a4").param("city", "c4")
				.param("telephone", "5567")).andExpect(status().isOk())
				.andExpect(view().name("owners/createOrUpdateOwnerForm")).andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "lastName"));
	}

	@Test
	void testInitFindForm() throws Exception {

		mockMvc.perform(get("/owners/find")).andExpect(status().isOk()).andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/findOwners"));

	}

	@Test
	void testInitFindFormWithName() throws Exception {
		mockMvc.perform(get("/owners/find").param("lastName", "Feinberg")).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner")).andExpect(view().name("owners/findOwners"));

	}

	@Test
	void testProcessFindFormLastNotFound() throws Exception {
		given(this.owners.findByLastName("Einstein")).willReturn(Lists.newArrayList());
		mockMvc.perform(get("/owners").param("lastName", "Einstein")).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner")).andExpect(view().name("owners/findOwners"));

	}

	@Test
	void testProcessFindFormLastNameSingle() throws Exception {

		given(this.owners.findByLastName("")).willReturn(Lists.newArrayList(oliver));

		mockMvc.perform(get("/owners").content("lastName=Feinberg")).andExpect(status().is3xxRedirection());
	}

	@Test
	void testProcessFindFormLastNameMultiple() throws Exception {

		given(this.owners.findByLastName("")).willReturn(Lists.newArrayList(oliver, keiley));

		mockMvc.perform(get("/owners")).andExpect(status().isOk()).andExpect(view().name("owners/ownersList"))
				.andExpect(model().attributeExists("owner"));

	}

	@Test
	void testInitUpdateOwnerForm() throws Exception {
		given(this.owners.findById(2)).willReturn(keiley);
		mockMvc.perform(get("/owners/{ownerId}/edit", 2)).andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("Keiley"))))
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Feinberg"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("23 Aberdean"))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Kingston"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("4168234589"))))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));

	}

	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		given(this.owners.findById(2)).willReturn(keiley);
		given(this.visits.findByPetId(6)).willReturn(Lists.newArrayList(visita));

		mockMvc.perform(post("/owners/{ownerId}/edit", 2).param("firstName", "Harry").param("lastName", "Feinberg")
				.param("address", "13 Hogwarts").param("city", "London").param("telephone", "7869083425"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));

	}

	@Test
	void testProcessUpdateOwnerFormFailure() throws Exception {
		given(this.owners.findById(2)).willReturn(keiley);
		given(this.visits.findByPetId(6)).willReturn(Lists.newArrayList(visita));

		mockMvc.perform(post("/owners/{ownerId}/edit", 99).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("first", "Harry").param("lastName", "Feinberg").param("address", "13 Hogwarts")
				.param("city", "London").param("telephone", "7869083425")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));

	}

	@Test
	void testShowOwner() throws Exception {
		given(this.owners.findById(2)).willReturn(keiley);
		mockMvc.perform(get("/owners/{ownerId}", 2)).andExpect(status().isOk())
				.andExpect(model().attribute("owner", hasProperty("pets", not(empty()))))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("Keiley"))))
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Feinberg"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("23 Aberdean"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("23 Aberdean"))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Kingston"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("4168234589"))))

				.andExpect(view().name("owners/ownerDetails"))

				// Use our own matches that checks the list of pets
				.andExpect(model().attribute("owner", hasProperty("pets", new BaseMatcher<List<Pet>>() {
					@Override
					public boolean matches(Object ob) {
						@SuppressWarnings("unchecked")
						List<Pet> pets = (List<Pet>) ob;
						Pet pet = pets.get(0);
						if (pet.getVisits().isEmpty()) {
							return true;
						}
						return false;
					}

					@Override
					public void describeTo(Description description) {
						description.appendText("Tabby didnt get any visits");

					}
				})));

	}

}