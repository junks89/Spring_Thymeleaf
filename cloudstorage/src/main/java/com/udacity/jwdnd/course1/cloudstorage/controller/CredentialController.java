package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("credential")
public class CredentialController {

    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication,
            @ModelAttribute("newCredential") CredentialForm newCredential, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        model.addAttribute("credentials", this.credentialService.getCredentialList(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping("newCredential")
    public String newCredential(
            Authentication authentication,
            @ModelAttribute("newCredential") CredentialForm newCredential, Model model) {
        String username = authentication.getName();
        String url = newCredential.getUrl();
        String credentialId = newCredential.getCredentialId();
        String password = newCredential.getPassword();
        String usernameForm = newCredential.getUsername();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);


        if (credentialId.isEmpty()) {
            credentialService.addCredential(url, usernameForm, encodedKey, encryptedPassword,username );
        } else {
            Credentials updateCredential = getCredential(Integer.parseInt(credentialId));
            credentialService.updateCredential(updateCredential.getCredentialId(), usernameForm, url, encodedKey, encryptedPassword);
        }
        User user = userService.getUser(username);
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentials", credentialService.getCredentialList(user.getUserId()));
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping(value = "/getCredential/{credentialId}")
    public Credentials getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @GetMapping(value = "/delCredential/{credentialId}")
    public String deleteCredential(
            Authentication authentication, @PathVariable Integer credentialId,
            @ModelAttribute("newCredential") CredentialForm newCredential, Model model) {
        credentialService.deleteCredential(credentialId);
        String username = authentication.getName();
        User user = userService.getUser(username);
        model.addAttribute("credentials", credentialService.getCredentialList(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }
}