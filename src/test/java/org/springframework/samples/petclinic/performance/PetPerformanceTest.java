package org.springframework.samples.petclinic.performance;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.persistence.OwnerRepository;
import org.springframework.samples.petclinic.persistence.PetRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PetPerformanceTest {

	@Value("${server.port}")
	private int port; // 8080

	@Autowired
	PetRepository petRepository;

	@Autowired
	OwnerRepository ownerRepository;

	@Autowired
	private TestRestTemplate testRestTemplate;

	private int testOwnerId;

	private static int TEST_PET_ID = 1;

	private static Stream<Arguments> provideArguments() {
		return Stream.of(Arguments.of(200), Arguments.of(500), Arguments.of(800), Arguments.of(1000),
				Arguments.of(2000), Arguments.of(5000), Arguments.of(8000));
	}

	// create a owner in database
	@BeforeEach
	public void before() {
		Owner owner = new Owner();
		owner.setLastName("ownerlastNameTest");
		owner.setFirstName("ownerfirstNameTest");
		owner.setAddress("ownerAddressTest");
		owner.setCity("ownerCityTest");
		owner.setTelephone("12345");
		ownerRepository.save(owner);
		this.testOwnerId = ownerRepository.findByLastName("ownerlastNameTest").iterator().next().getId();
	}

	@ParameterizedTest
	@MethodSource("provideArguments")
	public void CreatePet(int numOfInstance) throws URISyntaxException, IOException {
		// file writer
		String filename = "addPet-performance-test-" + numOfInstance + ".csv";
		FileWriter fw = new FileWriter(filename);

		// url for create pet
		String baseUrl = "http://localhost:" + port + "/owners/";
		String createPetUrl = baseUrl + testOwnerId + "/pets/new";
		URI createuri = new URI(createPetUrl);

		for (int i = 0; i < numOfInstance; i++) {
			// create pet
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String message = "Name=PetNameTest" + "&birthDate=1999-11-05" + "&type=addressTest" + "&city=cityTest"
					+ "&telephone=telephoneTest";
			HttpEntity<String> request = new HttpEntity<>(message, headers);
			Long start = System.nanoTime();
			ResponseEntity<String> createResponse = this.testRestTemplate.postForEntity(createuri, request,
					String.class);
			Long end = System.nanoTime();
			Long createTime = end - start;

			// record in file
			fw.append(String.valueOf(i)).append(",").append(String.valueOf(createTime)).append("\n");
			fw.flush();
		}
	}

	@ParameterizedTest
	@MethodSource("provideArguments")
	public void ModifyPet(int numOfInstance) throws URISyntaxException, IOException {
		// file writer
		String filename = "modifyPet-performance-test-" + numOfInstance + ".csv";
		FileWriter fw = new FileWriter(filename);

		// url for create pet
		String baseUrl = "http://localhost:" + port + "/owners/";
		String createPetUrl = baseUrl + testOwnerId + "/pets/new";
		URI createuri = new URI(createPetUrl);

		// url for modify pet
		String modifyPetUrl = baseUrl + testOwnerId + "/pets/" + TEST_PET_ID + "edit";
		URI modifyuri = new URI(modifyPetUrl);

		for (int i = 0; i < numOfInstance; i++) {
			// create pet
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String message = "Name=PetNameTest" + "&birthDate=1999-11-05" + "&type=addressTest" + "&city=cityTest"
					+ "&telephone=telephoneTest";
			HttpEntity<String> request = new HttpEntity<>(message, headers);
			ResponseEntity<String> createResponse = this.testRestTemplate.postForEntity(createuri, request,
					String.class);

			// modify pet with id=1
			String modifyMessage = "Name=PetNameTest" + i + "&birthDate=1999-11-05" + "&type=addressTest"
					+ "&city=cityTest" + "&telephone=telephoneTest";
			HttpEntity<String> modifyRequest = new HttpEntity<>(modifyMessage, headers);
			Long start = System.nanoTime();
			ResponseEntity<String> modifyResponse = this.testRestTemplate.postForEntity(modifyuri, modifyRequest,
					String.class);
			Long end = System.nanoTime();
			Long modifyTime = end - start;

			// record in file
			fw.append(String.valueOf(i)).append(",").append(String.valueOf(modifyTime)).append("\n");
			fw.flush();
		}
	}

}
