package org.springframework.samples.petclinic.IntegrationTests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.samples.petclinic.persistence.VisitRepository;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OwnerRepositoryIntegrationTests {

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private PetRepository pets;

	@Autowired
	private VisitRepository visits;

	@Test
	@Order(2)
	void testFindByLastName() {
		String name = "Franklin";
		Collection<Owner> results = owners.findByLastName(name);
		for (Owner o : results) {
			assert (o.getLastName().equals(name));
		}
	}

	@Test
	@Order(1)
	void testFindOwnerById() {
		int id = 1;
		Owner o = owners.findById(id);
		try {
			assert (o.getId().equals(id));
		}
		catch (NullPointerException e) {
			System.out.println("id not present in database");
		}
	}

	@Test
	@Order(3)
	void TestSaveNewOwner() {
		Owner o = new Owner();
		Pet p = new Pet();
		PetType type = new PetType();
		LocalDate bday = LocalDate.parse("2012-04-05");
		LocalDate vdate = LocalDate.parse("2022-04-05");

		type.setId(1);
		p.setOwner(o);
		p.setName("spot");
		p.setType(type);
		int oid = 1;
		int pid = 1;
		int vid = 1;
		while (pets.findById(pid) != null) {
			pid++;
		}
		Collection<Visit> petVisits = visits.findByPetId(pid);

		while (pets.findById(vid) != null) {
			vid++;
		}
		Visit visit = new Visit();
		visit.setId(vid);
		visit.setPetId(pid);
		visit.setDate(vdate);
		visit.setDescription("all healthy");
		petVisits.add(visit);

		p.setId(pid);
		p.setBirthDate(bday);
		p.setVisitsInternal(petVisits);

		o.setAddress("1234 Sherbrooke street");
		o.setCity("Montreal");
		o.setTelephone("1234567890");
		o.setFirstName("David");
		o.setLastName("Attenborough");
		while (owners.findById(oid) != null) {
			oid++;
		}
		o.setId(oid);
		o.setPetsInternal(p.getOwner().getPetsInternal());

		owners.save(o);
		assertThat(owners.findById(oid).equals(o));
	}

	@Test
	@Order(4)
	void TestUpdateOwner() {
		Owner o = owners.findById(1);
		if (o != null) {
			o.setLastName("newLastName");
		}
		owners.save(o);
		assertThat(owners.findById(1).getLastName().equals("newLastName"));
	}

}
