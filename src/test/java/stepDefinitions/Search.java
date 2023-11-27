package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Search {

	CloseableHttpClient httpClient = HttpClients.createDefault();

	String res = "";

	@Given("I am on find owners page")
	public void i_am_on_find_owners_page() {

		HttpGet httpGet = new HttpGet("http://localhost:8080/owners/find");

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
			// System.out.println(res);
			assert (res.contains("<h2>Find Owners</h2>"));
			EntityUtils.consume(entity);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@When("I search for an owner with last name {string}")
	public void i_search_for_an_owner_with_last_name(String name) {

		HttpGet httpGet = new HttpGet("http://localhost:8080/owners?lastName=" + name);

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Then("Owner with the name {string} should be displayed")
	public void owner_with_the_name_should_be_displayed(String name) {
		assert (res.contains(name));
	}

	@When("I hit search without entering a name")
	public void i_hit_search_without_entering_a_name() {

		HttpGet httpGet = new HttpGet("http://localhost:8080/owners?lastName=");

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Then("All owners in the database should be returned")
	public void all_owners_in_the_database_should_be_returned() {
		assert (res.contains("Jean Coleman") && res.contains("Jeff Black") && res.contains("Maria Escobito")
				&& res.contains("David Schroeder") && res.contains("Betty Davis"));

	}

	@Then("I should be told no such owner exists")
	public void I_should_be_told_no_such_owner_exists() {
		assert (res.contains("has not been found"));
	}

	@Then("All Owners with the name {string} should be displayed")
	public void all_owners_with_the_name_should_be_displayed(String string) {
		assert (res.contains("Betty Davis") && res.contains("Harold Davis"));
	}

}
