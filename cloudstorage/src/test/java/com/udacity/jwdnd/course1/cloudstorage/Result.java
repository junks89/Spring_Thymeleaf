package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Result {
    private final JavascriptExecutor js;

    public Result(WebDriver driver) {
        PageFactory.initElements(driver, this);
        js = (JavascriptExecutor) driver;
    }

    @FindBy(id = "ResultSuccess")
    private WebElement ResultSuccess;

    public void accept() {
        js.executeScript("arguments[0].click();", ResultSuccess);
    }
}
