package integration.android;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/**
 * Copied from <a href="https://bit.ly/3NE6QsD">Appium repository</a>
 * and modified to use Selenide framework.
 */
class AndroidDragAndDropTest extends BaseApiDemosTest {
  @Test
  void dragAndDrop() {
    $(By.xpath(".//*[@text='Views']")).click();
    $(By.xpath(".//*[@text='Drag and Drop']")).click();

    SelenideElement from = $(By.id("io.appium.android.apis:id/drag_dot_1")).shouldBe(visible);
    SelenideElement to = $(By.id("io.appium.android.apis:id/drag_dot_2")).shouldBe(visible);
    By dragText = By.id("io.appium.android.apis:id/drag_result_text");

    $(dragText).shouldHave(exactText(""));

    from.dragAndDropTo(to);

    $(dragText)
        .shouldBe(visible)
        .shouldHave(text("Dropped!"));
  }
}