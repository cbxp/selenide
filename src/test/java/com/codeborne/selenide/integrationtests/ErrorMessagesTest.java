package com.codeborne.selenide.integrationtests;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementShouldNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.timeout;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Thread.currentThread;
import static org.junit.Assert.*;

public class ErrorMessagesTest {
  PageObject pageObject = open(currentThread().getContextClassLoader().getResource("page_with_selects_without_jquery.html"), PageObject.class);

  @Before
  public final void setTimeout() {
    timeout = 1500;
  }

  @After
  public final void restoreTimeout() {
    timeout = 4000;
  }

  @Test
  public void elementNotFound() {
    try {
      $("h9").shouldHave(text("expected text"));
      fail("Expected ElementNotFound");
    } catch (ElementNotFound expected) {
      assertEquals("Element not found {By.selector: h9}\n" +
          "Expected: text 'expected text'\n" +
          "Timeout: 1.500 s.", expected.toString());
    }
  }

  @Test
  public void elementTextDoesNotMatch() {
    try {
      $("h2").shouldHave(text("expected text"));
      fail("Expected ElementShould");
    } catch (ElementShould expected) {
      assertEquals("Element should have text 'expected text' {By.selector: h2}\n" +
          "Element: '<h2>Dropdown list</h2>'\n" +
          "Timeout: 1.500 s.", expected.toString());
    }
  }

  @Test
  public void elementAttributeDoesNotMatch() {
    try {
      $("h2").shouldHave(attribute("name", "header"));
      fail("Expected ElementShould");
    } catch (ElementShould expected) {
      assertEquals("Element should have attribute name=header {By.selector: h2}\n" +
          "Element: '<h2>Dropdown list</h2>'\n" +
          "Timeout: 1.500 s.", expected.toString());
    }
  }


  @Test
  public void wrapperTextDoesNotMatch() {
    try {
      $(getElement(By.tagName("h2"))).shouldHave(text("expected text"));
      fail("Expected ElementShould");
    } catch (ElementShould expected) {
      assertEquals("Element should have text 'expected text' {By.tagName: h2}\n" +
          "Element: '<h2>Dropdown list</h2>'\n" +
          "Timeout: 1.500 s.", expected.toString());
    }
  }

  @Test
  public void pageObjectElementTextDoesNotMatch() {
    try {
      $(pageObject.header1).shouldHave(text("expected text"));
      fail("Expected ElementShould");
    } catch (ElementShould expected) {
      assertEquals("Element should have text 'expected text' {By.tagName: h2}\n" +
          "Element: '<h2>Dropdown list</h2>'\n" +
          "Timeout: 1.500 s.", expected.toString());
    }
  }

  @Test
  public void pageObjectWrapperTextDoesNotMatch() {
    try {
      $(pageObject.header2).shouldHave(text("expected text"));
      fail("Expected ElementShould");
    } catch (ElementShould expected) {
      assertEquals("Element should have text 'expected text' {By.tagName: h2}\n" +
          "Element: '<h2>Dropdown list</h2>'\n" +
          "Timeout: 1.500 s.", expected.toString());
    }
  }

  @Test
  public void selectOptionFromUnexistingList() {
    try {
      $(pageObject.categoryDropdown).selectOption("SomeOption");
    } catch (ElementNotFound e) {
      assertContains(e, "Element not found {By.id: invalid_id}", "Expected: exist");
    }
  }

  @Test
  public void clickUnexistingWrappedElement() {
    try {
      $(pageObject.categoryDropdown).click();
      fail("Expected ElementNotFound");
    } catch (ElementNotFound e) {
      assertEquals("Element not found {By.id: invalid_id}\n" +
          "Expected: visible\n" +
          "Timeout: 1.500 s.", e.toString());
    }
  }

  @Test
  public void existingElementShouldNotBePresent() {
    try {
      $("h2").shouldNot(exist);
      fail("Expected ElementFound");
    } catch (ElementShouldNot e) {
      assertEquals("Element should not exist {By.selector: h2}\n" +
          "Element: '<h2>Dropdown list</h2>'\n" +
          "Timeout: 1.500 s.", e.toString());
    }
  }

  @Test
  public void nonExistingElementShouldNotBeHidden() {
    try {
      $("h14").shouldNotBe(hidden);
      fail("Expected ElementNotFound");
    } catch (ElementNotFound e) {
      assertEquals("Element not found {By.selector: h14}\n" +
          "Expected: not(hidden)\n" +
          "Timeout: 1.500 s.", e.toString());
    }
  }

  private void assertContains(AssertionError e, String... expectedTexts) {
    for (String expectedText : expectedTexts) {
      assertTrue("Text not found: " + expectedText + " in error message: " + e,
          e.toString().contains(expectedText));
    }
  }

  public static class PageObject {
    @FindBy(tagName = "h2")
    public SelenideElement header1;

    @FindBy(tagName = "h2")
    public WebElement header2;

    @FindBy(id = "invalid_id")
    private WebElement categoryDropdown;
  }
}
