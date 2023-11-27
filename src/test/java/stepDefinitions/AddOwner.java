package stepDefinitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.petclinic.model.Owner;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddOwner {

	CloseableHttpClient httpClient = HttpClients.createDefault();

	String res = "";

	String url;

	Owner owner;

	@Given("I am on the Add Owner page")
	public void i_am_on_the_add_owner_page() {

		owner = new Owner();
		HttpGet httpGet = new HttpGet("http://localhost:8080/owners/new");

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
			assert (res.contains("Add Owner"));
			EntityUtils.consume(entity);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	@When("I enter {string} as First Name")
	public void i_enter_as_first_name(String name) {

		owner.setFirstName(name);

	}

	@When("I enter {string} as Last Name")
	public void i_enter_as_last_name(String name) {

		owner.setLastName(name);
	}

	@When("I enter {string} in Address")
	public void i_enter_in_address(String add) {

		owner.setAddress(add);
	}

	@When("I enter {string} in City")
	public void i_enter_in_city(String city) {

		owner.setCity(city);
	}

	@When("I enter {string} in Telephone")
	public void i_enter_in_telephone(String number) {

		owner.setTelephone(number);
	}

	@When("I click Add Owner")
	public void i_click_add_owner() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lastName", owner.getLastName()));
		params.add(new BasicNameValuePair("firstName", owner.getFirstName()));
		params.add(new BasicNameValuePair("address", owner.getAddress()));
		params.add(new BasicNameValuePair("city", owner.getCity()));
		params.add(new BasicNameValuePair("telephone", owner.getTelephone()));
		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, Consts.UTF_8);

		HttpPost httpPost = new HttpPost("http://localhost:8080/owners/new");
		httpPost.setEntity(ent);

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
			System.out.println(res);
			EntityUtils.consume(entity);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Then("An owner with this information should appear on the next page")
	public void an_owner_with_this_information_should_appear_on_the_next_page() {

		HttpGet httpGet = new HttpGet("http://localhost:8080/owners?lastName=" + owner.getLastName());

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
		assert (res.contains(owner.getLastName()) && res.contains(owner.getLastName())
				&& res.contains(owner.getAddress()) && res.contains(owner.getCity())
				&& res.contains(owner.getTelephone()));
	}

	@When("I enter nothing in First Name")
	public void i_enter_nothing_in_first_name() {

		owner.setFirstName(null);
	}

	@Then("A message should appear under first name saying {string}")
	public void a_message_should_appear_under_first_name_saying(String message) {
		assert (res.contains(message));

	}

	@When("I enter nothing in Last Name")
	public void i_enter_nothing_in_last_name() {

		owner.setLastName(null);
	}

	@Then("A message should appear under Last Name saying {string}")
	public void a_message_should_appear_under_last_name_saying(String message) {
		assert (res.contains(message));

	}

	@When("I enter nothing in Address")
	public void i_enter_nothing_in_address() {

		owner.setAddress(null);
	}

	@Then("A message should appear under Address saying {string}")
	public void a_message_should_appear_under_address_saying(String message) {

		assert (res.contains(message));
	}

	@When("I enter nothing in City")
	public void i_enter_nothing_in_city() {

		owner.setCity(null);
	}

	@Then("A message should appear under City saying {string}")
	public void a_message_should_appear_under_city_saying(String message) {

		assert (res.contains(message));

	}

	@When("I enter nothing in the Telephone field")
	public void i_enter_nothing_in_the_telephone_field() {
		// Write code here that turns the phrase above into concrete actions
		owner.setTelephone(null);
	}

	@Then("A message should appear under Telephone saying {string}")
	public void a_message_should_appear_under_telephone_saying(String message) {
		assert (res.contains(message));

	}

	@Given("I am on the Edit Owner page for owner id {string}")
	public void i_am_on_the_edit_owner_page_for_owner_id(String string) {

		owner = new Owner();
		url = "http://localhost:8080/owners/" + string;
		HttpGet httpGet = new HttpGet(url + "/edit");

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
			// System.out.println(res);
			assert (res.contains("Update Owner"));
			EntityUtils.consume(entity);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	@When("I click Update Owner")
	public void i_click_update_owner() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lastName", owner.getLastName()));
		params.add(new BasicNameValuePair("firstName", owner.getFirstName()));
		params.add(new BasicNameValuePair("address", owner.getAddress()));
		params.add(new BasicNameValuePair("city", owner.getCity()));
		params.add(new BasicNameValuePair("telephone", owner.getTelephone()));
		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, Consts.UTF_8);

		HttpPost httpPost = new HttpPost(url + "/edit");
		httpPost.setEntity(ent);

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
			System.out.println(res);
			EntityUtils.consume(entity);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
