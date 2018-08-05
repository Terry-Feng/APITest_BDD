package api.bdd;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import java.util.List;
import java.util.Map;

import static io.restassured.http.ContentType.JSON;
import static net.serenitybdd.rest.SerenityRest.rest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CategoryStep {
    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    private String GET_CATEGORY_BY_ID = "https://api.tmsandbox.co.nz/v1/Categories/%s/Details.json?catalogue=false";
    private int STATUS_CODE_SUCCESS = 200;
    private String KEY_NAME = "Name";
    private String KEY_CANRELIST = "CanRelist";

    @Given("a category with number (.*)")
    public void category_with_id(String id) {
        request = rest().given().accept(JSON).contentType(JSON);
        GET_CATEGORY_BY_ID = String.format(GET_CATEGORY_BY_ID, id);
    }

    @When("I search for detail information")
    public void search_detail() {
        response = request.when().get(GET_CATEGORY_BY_ID);
        json = response.then().statusCode(STATUS_CODE_SUCCESS);
    }

    @Then("the name is \"([^\"]*)\"")
    public void verify_canReList(String name) {
        json.body(KEY_NAME, equalTo(name));
    }

    @Then("the value of CanRelist is (.*)")
    public void verify_canReList(boolean value) {
        json.body(KEY_CANRELIST, equalTo(value));
    }

    @Then("the promotions element with name \"([^\"]*)\" contains description \"([^\"]*)\"")
    public void verity_element_content(String eleName, String eleDes) {
        List<Map<String, Object>> promotionList = json.extract().body().jsonPath().getList("Promotions");
        for (Map<String, Object> promotion : promotionList) {
            if (promotion.get("Name").equals(eleName)) {
                assertThat(promotion, containsField("Description", eleDes));
            }
        }
    }

    private Matcher<? super Map<String, Object>> containsField(final String field, final String value) {
        return new TypeSafeMatcher<Map<String, Object>>() {
            @Override
            protected boolean matchesSafely(Map<String, Object> map) {
                return map.get(field).toString().contains(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("the result %s doesn't contains %s", field, value));
            }
        };
    }
}
