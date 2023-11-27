package org.springframework.samples.petclinic.IntegrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.persistence.VetRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VetRepositoryIntegrationTests {

	@Autowired
	private VetRepository vets;

	@Test
	void TestFindAll() {
		Collection<Vet> vetList = null;
		vetList = vets.findAll();
		assert (vetList != null);
	}

}
