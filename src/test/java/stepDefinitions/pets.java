package stepDefinitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.petclinic.model.Pet;
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

public class pets {

	CloseableHttpClient httpClient = HttpClients.createDefault();

	String res = "";

	String url = "";

	String pettype = "";

	String birthday = "";

	Pet pet;

	@Given("I am on the New pet page for Owner id {string}")
	public void i_am_on_the_new_pet_page_for_owner_id(String string) {

		pet = new Pet();
		url = "http://localhost:8080/owners/" + string;
		HttpGet httpGet = new HttpGet(url + "/pets/new");

		CloseableHttpResponse response;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			res = EntityUtils.toString(entity);
			// System.out.println(res);
			EntityUtils.consume(entity);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@When("I enter {string} as Name")
	public void i_enter_as_name(String string) {
		pet.setName(string);
	}

	@When("I enter {string} as Birth Date")
	public void i_enter_as_birth_date(String string) {
		birthday = string;
	}

	@When("I enter {string} as Type")
	public void i_enter_as_type(String string) {

		pettype = string;
	}

	@When("I click Add Pet")
	public void i_click_add_pet() {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Name", pet.getName()));
		params.add(new BasicNameValuePair("birthDate", birthday));
		params.add(new BasicNameValuePair("Type", pettype));

		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, Consts.UTF_8);
		HttpPost httpPost = new HttpPost(url + "/pets/new");
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

	@Then("A pet with this information should appear on the next page")
	public void a_pet_with_this_information_should_appear_on_the_next_page() {

		HttpGet httpGet = new HttpGet(url);

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
		System.out.println(pet.getName());

		assert (res.contains(pet.getName()));

	}

	@Then("A message should appear under Name saying {string}")
	public void a_message_should_appear_under_name_saying(String string) {
		assert (res.contains(string));

	}

	@When("I enter no Name")
	public void i_enter_no_name() {
		pet.setName(null);
	}

	@When("I enter nothing as Birth Date")
	public void i_enter_nothing_as_birth_date() {
		birthday = "";
	}

	@Then("A message should appear under Birth Date saying {string}")
	public void a_message_should_appear_under_birth_date_saying(String string) {
		assert (res.contains(string));

	}

	@Given("I am on the edit pet page for Owner id {string} and pet id {string}")
	public void i_am_on_the_edit_pet_page_for_owner_id_and_pet_id(String string, String string2) {

		pet = new Pet();
		url = "http://localhost:8080/owners/" + string;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Name", pet.getName()));
		params.add(new BasicNameValuePair("birthDate", birthday));
		params.add(new BasicNameValuePair("Type", pettype));

		UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, Consts.UTF_8);
		HttpPost httpPost = new HttpPost(url + "/pets/" + string2 + "/edit");
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
