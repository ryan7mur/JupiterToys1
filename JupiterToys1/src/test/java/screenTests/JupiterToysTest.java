package screenTests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.JupiterToys;

public class JupiterToysTest extends JupiterToys{

	public static final List<String> EXPECTED_FORENAME_ERRORS 	= Arrays.asList("#b94a48", "forename", "Forename is required");
	public static final List<String> EXPECTED_EMAIL_ERRORS 		= Arrays.asList("#b94a48", "email", "Email is required");
	public static final List<String> EXPECTED_MESSAGE_ERRORS 	= Arrays.asList("#b94a48", "message", "Message is required");

	public static final List<String> EXPECTED_FORENAME_STATE 	= Arrays.asList("#000000", "forename");
	public static final List<String> EXPECTED_EMAIL_STATE 		= Arrays.asList("#000000", "email");
	public static final List<String> EXPECTED_MESSAGE_STATE 	= Arrays.asList("#000000", "message");

	public static final List<String> CONTACT_REQUIRED_VALUES 	= Arrays.asList("Ryan", "test@email.com", "Test message");

	public static final List<String> PRODUCT_ONE_DETAILS 		= Arrays.asList("Stuffed Frog", "10.99");
	public static final List<String> PRODUCT_TWO_DETAILS 		= Arrays.asList("Fluffy Bunny", "9.99");
	public static final List<String> PRODUCT_THREE_DETAILS 		= Arrays.asList("Valentine Bear", "14.99");
	public static final List<String> PRODUCT_FOUR_DETAILS 		= Arrays.asList("Funny Cow", "10.99");

	public static final String DOLLAR	= "$";
	
	/*
	 * Test case 1:
	 * 1. From the home page go to contact page
	 * 2. Click submit button
	 * 3. Validate errors
	 * 4. Populate mandatory fields
	 * 5. Validate errors are gone
	 */
	@Test
	public void testCase1() throws IOException, InterruptedException {		
		openHomePage();
		navigateToContactPage();
		submitContactForm();

		// Validate error message shown at the top of contact form
		WebElement errorHeader = findElementWait(By.cssSelector("div.alert.alert-error.ng-scope"));
		assertEquals(true, errorHeader.isDisplayed());
		assertEquals("We welcome your feedback - but we won't get it unless you complete the form correctly.", errorHeader.getText());

		// Find elements which portray errors within the contact form
		List<WebElement> errorGroups = findElementsWait(By.cssSelector("div.control-group.error"));
		List<WebElement> errorLabels = findElementsWait(errorGroups, By.cssSelector(".control-label"));
		List<WebElement> errorFields = findElementsWait(errorGroups, By.cssSelector(".ng-invalid-required"));
		List<WebElement> fieldErrorMessages = findElementsWait(errorGroups, By.cssSelector("[id$=-err]"));

		String color = "color";
		List<String> errorLabelColors = Arrays.asList(errorLabels.get(0).getCssValue(color), 
				errorLabels.get(1).getCssValue(color), errorLabels.get(2).getCssValue(color));

		// Verify errors are displayed as expected
		String name = "name";
		assertEquals(EXPECTED_FORENAME_ERRORS, 
				Arrays.asList(Color.fromString(errorLabelColors.get(0)).asHex(), errorFields.get(0).getAttribute(name), fieldErrorMessages.get(0).getText()));
		assertEquals(EXPECTED_EMAIL_ERRORS, 
				Arrays.asList(Color.fromString(errorLabelColors.get(1)).asHex(), errorFields.get(1).getAttribute(name), fieldErrorMessages.get(1).getText()));
		assertEquals(EXPECTED_MESSAGE_ERRORS, 
				Arrays.asList(Color.fromString(errorLabelColors.get(2)).asHex(), errorFields.get(2).getAttribute(name), fieldErrorMessages.get(2).getText()));

		fillRequiredContactFields(CONTACT_REQUIRED_VALUES);

		// Verify error message at the top of form has reverted to normal
		WebElement validHeader = findElementWait(By.cssSelector("div.alert.alert-info.ng-scope"));
		assertEquals(true, validHeader.isDisplayed());
		assertEquals("We welcome your feedback - tell it how it is.", validHeader.getText());

		// Check to see if error elements have reverted to valid elements  
		List<WebElement> labelAsterisks = findElementsWait(By.cssSelector("span.req"));
		List<WebElement> requiredLabels = findElementsWait(labelAsterisks, (By.xpath("//parent::*")));
		List<WebElement> requiredFields = findElementsWait(By.cssSelector("div.control-group .ng-valid-required"));

		List <String> requiredLabelColors = Arrays.asList(requiredLabels.get(0).getCssValue(color), 
				requiredLabels.get(1).getCssValue(color), requiredLabels.get(2).getCssValue(color));

		assertEquals(EXPECTED_FORENAME_STATE, 
				Arrays.asList(Color.fromString(requiredLabelColors.get(0)).asHex(), requiredFields.get(0).getAttribute(name)));
		assertEquals(EXPECTED_EMAIL_STATE, 
				Arrays.asList(Color.fromString(requiredLabelColors.get(1)).asHex(), requiredFields.get(1).getAttribute(name)));
		assertEquals(EXPECTED_MESSAGE_STATE, 
				Arrays.asList(Color.fromString(requiredLabelColors.get(2)).asHex(), requiredFields.get(2).getAttribute(name)));

		assertEquals(true, getDriver().findElements(By.cssSelector("[id$=-err]")).isEmpty());
	}

	/*
	 * Test case 2:
	 * 1. From the home page go to contact page
	 * 2. Populate mandatory fields
	 * 3. Click submit button
	 * 4. Validate successful submission message
	 * Note: Run this test 5 times to ensure 100% pass rate
	 */
	@Test
	public void testCase2() throws IOException, InterruptedException{
		openHomePage();
		navigateToContactPage();
		fillRequiredContactFields(CONTACT_REQUIRED_VALUES);
		submitContactForm();
		// Wait for Sending feedback mask to disappear
		assertEquals(true, waitForFeedbackMask(By.linkText("Sending Feedback")));
		
		// Verify message has been submitted successfully
		assertEquals(true, findElementWait(By.cssSelector(".alert.alert-success")).isDisplayed());
		assertEquals("Thanks " + CONTACT_REQUIRED_VALUES.get(0) + ", we appreciate your feedback.", 
				findElementWait(By.cssSelector(".alert.alert-success")).getText());
	}

	/*
	 * Test case 3:
	 * 1. From the home page go to shop page
	 * 2. Click buy button 2 times on “Funny Cow”
	 * 3. Click buy button 1 time on “Fluffy Bunny”
	 * 4. Click the cart menu
	 * 5. Verify the items are in the cart
	 */	
	@Test
	public void testCase3() throws IOException, InterruptedException {		
		openHomePage();
		navigateToShopPage();

		// Store required shop products in a hashmap containing product name and quantity needed
		Map<String, Integer> requiredProducts = new LinkedHashMap<String, Integer>();
		requiredProducts.put(PRODUCT_FOUR_DETAILS.get(0), 2);		
		requiredProducts.put(PRODUCT_TWO_DETAILS.get(0), 1);
		selectProductsFromShop(requiredProducts);

		navigateToCartPage();
		
		// Verify the correct items and their quantity have been added to the cart
		assertEquals(buildCartHeader("3"), findElementWait(By.cssSelector("p.cart-msg")).getText());
		Map<String, String> expectedCartDetails = new LinkedHashMap<String, String>();
		expectedCartDetails.put(PRODUCT_FOUR_DETAILS.get(0), "2");
		expectedCartDetails.put(PRODUCT_TWO_DETAILS.get(0), "1");

		List<WebElement> cartItems = findElementsWait(By.cssSelector("tr.cart-item"));

		Map<String, String> itemCartDetails = new LinkedHashMap<String, String>();
		for (WebElement cartItem : cartItems) {
			itemCartDetails.put(findElementWait(cartItem, By.cssSelector("td:first-child")).getText(), 
					findElementWait(cartItem, By.cssSelector("input[name = 'quantity']")).getAttribute("value"));
		}
		assertEquals(expectedCartDetails, itemCartDetails);
	}

	/*
	 * Test case 4: Advanced
	 * 1. Buy 2 Stuffed Frog, 5 Fluffy Bunny, 3 Valentine Bear
	 * 2. Go to the cart page
	 * 3. Verify the price for each product
	 * 4. Verify that each product’s sub total = product price * quantity
	 * 5. Verify that total = sum(sub totals)
	 */	
	@Test
	public void testCase4() throws IOException, InterruptedException {	
		openHomePage();
		navigateToShopPage();
		
		// Store required shop products in a hashmap containing product name and quantity needed
		Map<String, Integer> requiredProducts = new HashMap<String, Integer>();
		requiredProducts.put(PRODUCT_ONE_DETAILS.get(0), 2);		
		requiredProducts.put(PRODUCT_TWO_DETAILS.get(0), 5);
		requiredProducts.put(PRODUCT_THREE_DETAILS.get(0), 3);
		selectProductsFromShop(requiredProducts);

		navigateToCartPage();	
		assertEquals(buildCartHeader("10"), findElementWait(By.cssSelector("p.cart-msg")).getText());

		// Get the details displayed on the cart page for each product - i.e. price, quantity, subtotal
		List<WebElement> cartItems = findElementsWait(By.cssSelector("tr.cart-item"));
		Map<String, List<String>> itemCartDetails = new LinkedHashMap<String, List<String>>();
		for (WebElement cartItem : cartItems) {
			itemCartDetails.put(findElementWait(cartItem, By.cssSelector("td.ng-binding:first-child")).getText(), 
					Arrays.asList(findElementWait(cartItem, By.cssSelector("input[name = 'quantity']")).getAttribute("value"), 
							findElementWait(cartItem, By.cssSelector("td.ng-binding:nth-child(2)")).getText(), 
							findElementWait(cartItem, By.cssSelector("td.ng-binding:nth-child(4)")).getText()));
		}

		// Store the product information on the cart page in a list matched to each product
		List<String> stuffedFrogCartDetails = itemCartDetails.get(PRODUCT_ONE_DETAILS.get(0));
		List<String> fluffyBunnyCartDetails = itemCartDetails.get(PRODUCT_TWO_DETAILS.get(0));
		List<String> valentineBearCartDetails = itemCartDetails.get(PRODUCT_THREE_DETAILS.get(0));

		// Verify each individual product price
		assertEquals(DOLLAR + PRODUCT_ONE_DETAILS.get(1), stuffedFrogCartDetails.get(1));
		assertEquals(DOLLAR + PRODUCT_TWO_DETAILS.get(1), fluffyBunnyCartDetails.get(1));
		assertEquals(DOLLAR + PRODUCT_THREE_DETAILS.get(1), valentineBearCartDetails.get(1));

		// Verify each subtotal per product
		assertEquals(DOLLAR + String.valueOf(10.99 * 2), stuffedFrogCartDetails.get(2));
		assertEquals(DOLLAR + String.valueOf(9.99 * 5), fluffyBunnyCartDetails.get(2));
		assertEquals(DOLLAR + String.valueOf(14.99 * 3), valentineBearCartDetails.get(2));

		// Find and verify the total cost of products in cart 
		Double calculatedTotal = Double.parseDouble(stuffedFrogCartDetails.get(2).substring(1)) + 
				Double.parseDouble(fluffyBunnyCartDetails.get(2).substring(1)) + Double.parseDouble(valentineBearCartDetails.get(2).substring(1));
		assertEquals("116.9", calculatedTotal.toString());
		assertEquals("Total: 116.9", findElementWait(By.cssSelector(".total")).getText());
	}

	public void openHomePage() throws IOException {
		String url = propertiesReader("websiteUrl");
		getDriver().get(url);

		assertEquals(true, findElementWait(By.className("hero-unit")).isDisplayed());
		assertEquals(true, findElementWait(By.linkText("Start Shopping »")).isDisplayed());
	}
	
	public void selectProductsFromShop(Map<String, Integer> products) throws IOException {
		// Store all visible shop products in list
		List<WebElement> availableProducts = findElementsWait(By.cssSelector("li.product"));

		// Create a hashmap and iterate over the list just created to populate the map
		Map<String, WebElement> productDetails = new HashMap<String, WebElement>();
		for (WebElement product : availableProducts) {
			productDetails.put(findElementWait(product, By.cssSelector("h4.product-title")).getText(), 
					findElementWait(product, By.cssSelector("a.btn.btn-success")));
		}
		
		// create a hashmap for the required products details and select the buy button the appropriate amount of times
		Map<String, Integer> requiredProducts = new LinkedHashMap<String, Integer>();
				for(String key : products.keySet()) {
			requiredProducts.put(key, products.get(key));		
		}

		for(String key : requiredProducts.keySet()) {
			for(int i = 0; i < requiredProducts.get(key); i++) {
				productDetails.get(key).click();
			}
		}
	}

	public String buildCartHeader(String numberItems) {
		return String.format("There are %s items in your cart, you can Checkout or carry on Shopping.", numberItems);
	}

	public void submitContactForm() throws IOException {
		assertEquals(true, findElementWait(By.cssSelector("a.btn-contact")).isDisplayed());
		findElementWait(By.cssSelector("a.btn-contact")).click();
	}

	public void fillRequiredContactFields(List<String> valuesToEnter) throws IOException {
		List<WebElement> requiredFields = findElementsWait(By.xpath("//div[contains(@class, 'control-group')]//*[contains(@class, 'valid-required')]"));

		requiredFields.get(0).sendKeys(valuesToEnter.get(0));
		requiredFields.get(1).sendKeys(valuesToEnter.get(1));
		requiredFields.get(2).sendKeys(valuesToEnter.get(2));
	}

	public void navigateToContactPage() throws IOException, InterruptedException {
		WebElement contactButton = findElementWait(By.id("nav-contact"));
		contactButton.click();

		assertEquals(true, findElementWait(By.cssSelector("form.form-horizontal")).isDisplayed());
	}

	public void navigateToShopPage() throws IOException, InterruptedException {
		WebElement shopButton = findElementWait(By.id("nav-shop"));
		shopButton.click();

		assertEquals(true, findElementWait(By.cssSelector("div.products.ng-scope")).isDisplayed());
	}

	public void navigateToCartPage() throws IOException, InterruptedException {
		WebElement cartButton = findElementWait(By.id("nav-cart"));
		cartButton.click();
	}
	
	public WebElement findElementWait(By by) throws IOException {		
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
		wait.until(ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(by), ExpectedConditions.visibilityOfElementLocated(by)));
		WebElement element = getDriver().findElement(by);

		return element;
	}

	public WebElement findElementWait(WebElement parentElement, By by) throws IOException {		
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
		wait.until(ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(by), ExpectedConditions.visibilityOfElementLocated(by)));
		WebElement element = parentElement.findElement(by);

		return element;
	}

	public Boolean waitForFeedbackMask(By by) throws IOException {		
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));

		return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	public List<WebElement> findElementsWait(By by) throws IOException {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
		wait.until(ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(by), ExpectedConditions.visibilityOfElementLocated(by)));
		List<WebElement> elements = getDriver().findElements(by);

		return elements;
	}

	public List<WebElement> findElementsWait(List<WebElement> parentElements, By by) throws IOException {		
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
		wait.until(ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(by), ExpectedConditions.visibilityOfElementLocated(by)));

		List<WebElement> elements = new ArrayList<WebElement>();
		for (WebElement parentElement : parentElements) {
			elements.add(parentElement.findElement(by));
		}

		return elements;
	}
}