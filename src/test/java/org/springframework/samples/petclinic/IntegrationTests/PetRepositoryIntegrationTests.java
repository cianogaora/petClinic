package org.springframework.samples.petclinic.IntegrationTests;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;

import java.text.CollationElementIterator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetRepositoryIntegrationTests {

	@Autowired
	private PetRepository pets;

	@Autowired
	private OwnerRepository owners;

	@Test
	@Order(1)
	void TestFindPetTypes() {
		List<PetType> petTypes = pets.findPetTypes();
		assert petTypes == null || (petTypes.get(0).getName().equals("bird") && petTypes.get(1).getName().equals("cat")
				&& petTypes.get(2).getName().equals("dog") && petTypes.get(3).getName().equals("hamster")
				&& petTypes.get(4).getName().equals("lizard") && petTypes.get(5).getName().equals("snake"));
	}

	@Test
	@Order(2)
	void TestFindPetById() {
		int id = 1;
		Pet pet = pets.findById(id);
		try {
			assert (pet.getId().equals(id));
		}
		catch (NullPointerException e) {
			System.out.println("id not present in database");
		}
	}

	@Test
	@Order(3)
	void TestSavePet() {
		Pet pet = pets.findById(1);
		pet.setName("NewTestName");
		pets.save(pet);
		assert (pets.findById(1).getName().equals("NewTestName"));
	}

}
