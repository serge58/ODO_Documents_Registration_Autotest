package common.listeners;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import static common.listeners.IHighlightConstants.SCRIPT_GET_ELEMENT_BORDER;
import static common.listeners.IHighlightConstants.SCRIPT_UNHIGHLIGHT_ELEMENT;

public class HighlightElements extends AbstractWebDriverEventListener{
	private static WebElement lastElem = null;
    private static String lastBorder = null;

    @Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
    	highlightElement(element, driver);
	}

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
    	highlightElement(element, driver);
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		highlightElement(element, driver);
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		highlightElement(element, driver);
	}
    
    public static void highlightElement(WebElement elem, WebDriver driver) {
    	JavascriptExecutor js = ((JavascriptExecutor) driver);
        unhighlightLast(js);

        // remember the new element
        lastElem = elem;
        lastBorder = (String)(js.executeScript(SCRIPT_GET_ELEMENT_BORDER, elem));
    }

    public static void unhighlightLast(JavascriptExecutor js) {
        if (lastElem != null) {
            try {
                // if there already is a highlighted element, unhighlight it
                js.executeScript(SCRIPT_UNHIGHLIGHT_ELEMENT, lastElem, lastBorder);
            } catch (StaleElementReferenceException ignored) {
                // the page got reloaded, the element isn't there
            } finally {
                // element either restored or wasn't valid, nullify in both cases
                lastElem = null;
            }
        }
    }
}
