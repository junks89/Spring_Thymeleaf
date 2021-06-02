package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void checkAccessBeforeLogin() {
        driver.get("http://localhost:" + this.port + "/Home");
        Assertions.assertEquals("Login", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void checkAccessBeforeLoginSignup() {
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void createNewUserCheckAccess() {

        driver.get("http://localhost:" + this.port + "/signup");
        SignUpPage signupPage = new SignUpPage(driver);
        signupPage.setPassword("pass");
        signupPage.setFirstName("max");
        signupPage.setUserName("maxiMuster");
        signupPage.setLastName("Muster");

        signupPage.signUp();
        driver.get("http://localhost:" + this.port + "/login");
        Login loginPage = new Login(driver);
        loginPage.setUserName("maxiMuster");
        loginPage.setPassword("pass");
        loginPage.login();

        Assertions.assertEquals("Home", driver.getTitle());
        Home homePage = new Home(driver);
        homePage.logout();
        Assertions.assertEquals("Login", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/Home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void createAndDeleteNote() {
        String noteTitle = "New Note";
        String noteDescription = "New Note Description";
        Home home = signUpAndLogin();
        createNewNote(noteTitle, noteDescription, home);
        home.navNotes();
        home = new Home(driver);
        Notes note = home.getNote();
        Assertions.assertEquals(noteTitle, note.getNoteTitle());
        Assertions.assertEquals(noteDescription, note.getNoteDescription());
        Assertions.assertFalse(home.notesExisting(driver));
        home.deleteNote();
        Result result = new Result(driver);
        result.accept();
        home.navNotes();

        Assertions.assertTrue(home.notesExisting(driver));
    }

    @Test
    public void createAndDisplayNote() {
        String noteTitle = "New Note";
        String noteDescription = "New Note Description";
        Home home = signUpAndLogin();
        createNewNote(noteTitle, noteDescription, home);
        home.navNotes();
        home = new Home(driver);
        Notes note = home.getNote();
        Assertions.assertEquals(noteTitle, note.getNoteTitle());
        Assertions.assertEquals(noteDescription, note.getNoteDescription());
        home.deleteNote();
    }


    @Test
    public void editAndDisplayNote() {
        String noteTitle = "New Note";
        String noteDescription = "New Note Description";
        Home home = signUpAndLogin();
        createNewNote(noteTitle, noteDescription, home);
        home.navNotes();
        home = new Home(driver);
        home.editNote();
        noteTitle = "edit note title";
        home.adaptNoteTitle(noteTitle);
        noteDescription = "edit note description";
        home.adaptNoteDescription(noteDescription);
        home.saveNote();
        Result result = new Result(driver);
        result.accept();
        home.navNotes();
        Notes note = home.getNote();
        Assertions.assertEquals(noteTitle, note.getNoteTitle());
        Assertions.assertEquals(noteDescription, note.getNoteDescription());
        home.deleteNote();
    }



    @Test
    public void createCredentialTest() {
        Home home = signUpAndLogin();
        createCheckCredential("www.google.at", "user", "1234", home);
        home.deleteCredential();
        Result result = new Result(driver);
        result.accept();

    }
    @Test
    public void deleteCredential() {
        Home home = signUpAndLogin();

        createCredential("google.at", "user", "1", home);
        Assertions.assertFalse(home.credentialExisting(driver));
        home.deleteCredential();
        Result result = new Result(driver);
        result.accept();
        home.navCredentials();
        Assertions.assertTrue(home.credentialExisting(driver));

    }
    @Test
    public void editCredential() {
        String url="www.google.at";
        String user= "user";
        String password= "1234";
        Home home = signUpAndLogin();
        createCheckCredential(url, user, password, home);
        Credentials credential = home.getCredential();
        String  originalPassword = credential.getPassword();
        home.editCredential();
        String newUrl = url+"new";
        String newUsername = user+"new";
        String newPassword = password+"new";
        home.setCredentialUrl(newUrl);
        home.setCredentialUsername(newUsername);
        home.setCredentialPassword(newPassword);
        home.saveCredential();
        Result result = new Result(driver);
        result.accept();
        home.navCredentials();
        Credentials adaptedCredential = home.getCredential();
        Assertions.assertEquals(newUrl, adaptedCredential.getUrl());
        Assertions.assertEquals(newUsername, adaptedCredential.getUsername());
        String adaptedPassword = adaptedCredential.getPassword();
        Assertions.assertNotEquals(newPassword, adaptedPassword);
        Assertions.assertNotEquals(originalPassword, adaptedPassword);
        home.deleteCredential();
        result.accept();
    }

    private void createNewNote(String noteTitle, String noteDescription, Home home) {
        home.navNotes();
        home.addNewNote();
        home.setNoteTitle(noteTitle);
        home.setNoteDescription(noteDescription);
        home.saveNote();
        Result resultPage = new Result(driver);
        resultPage.accept();
        home.navNotes();
    }

    private void createCheckCredential(String url, String username, String password, Home home) {
        createCredential(url, username, password, home);
        home.navCredentials();
        Credentials credential = home.getCredential();
        Assertions.assertEquals(url, credential.getUrl());
        Assertions.assertEquals(username, credential.getUsername());
        Assertions.assertNotEquals(password, credential.getPassword());
    }

    private void createCredential(String url, String username, String password, Home home) {
        home.navCredentials();
        home.addCredential();
        home.setCredentialUrl(url);
        home.setCredentialUsername(username);
        home.setCredentialPassword(password);
        home.saveCredential();
        Result result = new Result(driver);
        result.accept();
        home.navCredentials();
    }

    private Home signUpAndLogin() {
        driver.get("http://localhost:" + this.port + "/signup");
        SignUpPage signupPage = new SignUpPage(driver);
        signupPage.setPassword("1");
        signupPage.setFirstName("max");
        signupPage.setUserName("user");
        signupPage.setLastName("Muster");

        signupPage.signUp();
        driver.get("http://localhost:" + this.port + "/login");
        Login loginPage = new Login(driver);
        loginPage.setUserName("user");
        loginPage.setPassword("1");
        loginPage.login();

        return new Home(driver);
    }
}
