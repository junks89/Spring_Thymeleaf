package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;
    @FindBy(id = "nav-notes-tab")
    private WebElement navNotes;
    @FindBy(id = "nav-credentials-tab")
    private WebElement navCredentials;
    @FindBy(id = "noteTitle")
    private WebElement noteTitle;
    @FindBy(id = "noteDescription")
    private WebElement noteDescription;
    @FindBy(id = "showModal")
    private WebElement showModal;
    @FindBy(id = "note-title")
    private WebElement noteTitleInput;
    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;
    @FindBy(id = "NoteButtonEdit")
    private WebElement noteButtonEdit;
    @FindBy(id = "deleteNote")
    private WebElement deleteNote;
    @FindBy(id = "DelCredential")
    private WebElement DelCredential;
    @FindBy(id = "credentialUrl")
    private WebElement credentialUrlInput;

    @FindBy(id = "credentialUsername")
    private WebElement credentialUsernameInput;

    @FindBy(id = "credentialPassword")
    private WebElement credentialPasswordInput;
    @FindBy(id = "addCredentialButton")
    private WebElement addCredentialButton;
    @FindBy(id = "saveCredentialButton")
    private WebElement saveCredentialButton;
    @FindBy(id = "editCredentialButton")
    private WebElement editCredentialButton;
    @FindBy(id = "credential-username")
    private WebElement credentialUsernameInputField;
    @FindBy(id = "credential-password")
    private WebElement credentialPasswordInputField;
    @FindBy(id = "credential-url")
    private WebElement credentialUrlInputField;

    private final JavascriptExecutor js;

    private final WebDriverWait wait;

    public Home(WebDriver driver) {
        PageFactory.initElements(driver, this);
        js = (JavascriptExecutor) driver;
        wait = new WebDriverWait(driver, 500);
    }

    public void logout() {
        js.executeScript("arguments[0].click();", logoutButton);
    }
    @FindBy(id = "noteSubmit")
    private WebElement noteSubmit;
    public void navNotes() {
        js.executeScript("arguments[0].click();", navNotes);
    }
    public void navCredentials() {
        js.executeScript("arguments[0].click();", navCredentials);
    }
    public void addNewNote() {
        js.executeScript("arguments[0].click();", showModal);
    }

    public Notes getNote() {
        String title = wait.until(ExpectedConditions.elementToBeClickable(noteTitle)).getText();
        String description = noteDescription.getText();

        return new Notes(title, description);
    }
    public void setNoteTitle(String noteTitle) {
        js.executeScript("arguments[0].value='" + noteTitle + "';", noteTitleInput);
    }
    public void setNoteDescription(String noteDescription) {
        js.executeScript("arguments[0].value='"+ noteDescription +"';", noteDescriptionInput);
    }
    public void saveNote() {
        js.executeScript("arguments[0].click();", noteSubmit);
    }

    public void editNote() {
        js.executeScript("arguments[0].click();", noteButtonEdit);
    }
    public void adaptNoteTitle(String noteTitle) {
        wait.until(ExpectedConditions.elementToBeClickable(noteTitleInput)).clear();
        wait.until(ExpectedConditions.elementToBeClickable(noteTitleInput)).sendKeys(noteTitle);
    }

    public void adaptNoteDescription(String noteDescription) {
        wait.until(ExpectedConditions.elementToBeClickable(noteDescriptionInput)).clear();
        wait.until(ExpectedConditions.elementToBeClickable(noteDescriptionInput)).sendKeys(noteDescription);
    }
    public void deleteNote() {
        js.executeScript("arguments[0].click();", deleteNote);
    }
    public boolean notesExisting(WebDriver driver) {
        return !isElementExisting(By.id("noteTitle"), driver) && !isElementExisting(By.id("noteDescription"), driver);
    }

    public boolean credentialExisting(WebDriver driver) {
        return !isElementExisting(By.id("credentialUrl"), driver) &&
                !isElementExisting(By.id("credentialUsername"), driver) &&
                !isElementExisting(By.id("credentialPassword"), driver);
    }


    public boolean isElementExisting(By locatorKey, WebDriver driver) {
        try {
            driver.findElement(locatorKey);

            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public Credentials getCredential() {
        String url = wait.until(ExpectedConditions.elementToBeClickable(credentialUrlInput)).getText();
        String username = credentialUsernameInput.getText();
        String password = credentialPasswordInput.getText();

        return new Credentials(url, username, password);
    }

    public void deleteCredential() {
        js.executeScript("arguments[0].click();", DelCredential);
    }

    public void setCredentialUrl(String url) {
        js.executeScript("arguments[0].value='" + url + "';", credentialUrlInputField);
    }

    public void setCredentialUsername(String username) {
        js.executeScript("arguments[0].value='" + username + "';", credentialUsernameInputField);
    }

    public void setCredentialPassword(String password) {
        js.executeScript("arguments[0].value='" + password + "';",  credentialPasswordInputField);
    }

    public void addCredential() {
        js.executeScript("arguments[0].click();", addCredentialButton);
    }

    public void saveCredential() {
        js.executeScript("arguments[0].click();", saveCredentialButton);
    }
    public void editCredential() {
        js.executeScript("arguments[0].click();", editCredentialButton);
    }

}
