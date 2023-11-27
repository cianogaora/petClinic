package org.springframework.samples.petclinic.performance;

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
import org.springframework.samples.petclinic.persistence.OwnerRepository;
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
public class OwnerPerformanceTest {

	@Value("${server.port}")
	private int port; // 8080

	@Autowired
	OwnerRepository repository;

	@Autowired

	private TestRestTemplate testRestTemplate;

	private static final int TEST_OWNER_ID = 1;

	private static Stream<Arguments> provideArguments() {
		return Stream.of(Arguments.of(200), Arguments.of(500), Arguments.of(800), Arguments.of(1000),
				Arguments.of(2000), Arguments.of(5000), Arguments.of(8000));
	}

	@ParameterizedTest
	@MethodSource("provideArguments")
	public void CreateOwner(int numOfInstance) throws URISyntaxException, IOException {
		// file writer
		String filename = "addOwner-performance-test-" + numOfInstance + ".csv";
		FileWriter fw = new FileWriter(filename);

		// url for create owner
		String baseUrl = "http://localhost:" + port + "/";
		String createOwnerUrl = baseUrl + "owners/new";
		URI createuri = new URI(createOwnerUrl);

		for (int i = 0; i < numOfInstance; i++) {
			// create owner
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String message = "firstName=firstNameTest" + "&lastName=lastNameTest" + "&address=addressTest"
					+ "&city=cityTest" + "&telephone=12345";
			HttpEntity<String> request = new HttpEntity<>(message, headers);
			Long startAdd = System.nanoTime();
			ResponseEntity<String> createResponse = this.testRestTemplate.postForEntity(createuri, request,
					String.class);
			Long endAdd = System.nanoTime();
			Long createTime = endAdd - startAdd;

			// record in file
			fw.append(String.valueOf(i)).append(",").append(String.valueOf(createTime)).append("\n");
			fw.flush();
		}
	}

	@ParameterizedTest
	@MethodSource("provideArguments")
	public void ModifyOwner(int numOfInstance) throws URISyntaxException, IOException {
		// file writer
		String filename = "modifyOwner-performance-test-" + numOfInstance + ".csv";
		FileWriter fw = new FileWriter(filename);

		// url for create owner
		String baseUrl = "http://localhost:" + port + "/";
		String createOwnerUrl = baseUrl + "owners/new";
		URI createuri = new URI(createOwnerUrl);

		String modifyOwnerUrl = baseUrl + "owners/" + TEST_OWNER_ID + "/edit";
		URI modifyuri = new URI(modifyOwnerUrl);

		for (int i = 0; i < numOfInstance; i++) {
			// create owner
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			String message = "firstName=firstNameTest" + "&lastName=lastNameTest" + "&address=addressTest"
					+ "&city=cityTest" + "&telephone=12345";
			HttpEntity<String> request = new HttpEntity<>(message, headers);
			ResponseEntity<String> createResponse = this.testRestTemplate.postForEntity(createuri, request,
					String.class);

			// modify owner with id=1
			String modifyMessage = "firstName=firstNameTest" + i + "&lastName=lastNameTest" + "&address=addressTest"
					+ "&city=cityTest" + "&telephone=12345";
			HttpEntity<String> modifyRequest = new HttpEntity<>(modifyMessage, headers);
			Long startModify = System.nanoTime();
			ResponseEntity<String> modifyResponse = this.testRestTemplate.postForEntity(modifyuri, modifyRequest,
					String.class);
			Long endModify = System.nanoTime();
			Long modifyTime = endModify - startModify;

			// record in file
			fw.append(String.valueOf(i)).append(",").append(String.valueOf(modifyTime)).append("\n");
			fw.flush();
		}
	}

}
