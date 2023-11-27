package org.springframework.samples.petclinic.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 
 * 
 * Test class for the {@link CrashController}
 */

@WebMvcTest(CrashController.class)
class CrashControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testTriggerException() {

		assertThatThrownBy(() -> mockMvc.perform(get("/oups"))).hasCause(new RuntimeException(
				"Expected: controller used to showcase what " + "happens when an exception is thrown"));

	}

}
