package org.springframework.samples.petclinic.IntegrationTests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VisitRepositoryIntegrationTests {

	@Autowired
	private VisitRepository visits;

	@Autowired
	private PetRepository pets;

	@Test
	@Order(1)
	void TestSaveVisit() {
		Visit v = new Visit();
		LocalDate vdate = LocalDate.parse("2021-01-02");
		Pet pet = pets.findById(1);
		Collection<Visit> petVisits = pet.getVisits();
		int vid = 1;
		if (!petVisits.isEmpty()) {
			while (petVisits.iterator().hasNext()) {
				vid++;
			}
		}

		v.setDate(vdate);
		v.setId(vid);
		v.setPetId(pet.getId());
		v.setDescription("Pet has covid");

		visits.save(v);

		petVisits = visits.findByPetId(pet.getId());
		Visit nextVisit = petVisits.iterator().next();
		assert nextVisit == null || (nextVisit.getDescription().equals("Pet has covid"));

	}

	@Test
	@Order(2)
	void TestFindByPetId() {
		Visit v = new Visit();
		LocalDate vdate = LocalDate.parse("2021-01-02");
		v.setDescription("Test description");
		v.setPetId(1);
		v.setDate(vdate);
		v.setId(1);
		visits.save(v);

		List<Visit> visitList = visits.findByPetId(1);
		Visit visit = visitList.iterator().next();
		assert visit.getDescription().equals("Test description");
	}

}
