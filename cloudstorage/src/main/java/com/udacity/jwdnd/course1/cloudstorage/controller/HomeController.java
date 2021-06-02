package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NotesForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final NotesService notesService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final FileService fileService;


    public HomeController(UserService userService, NotesService notesService, FileService fileService, CredentialService credentialService , EncryptionService encryptionService) {
        this.userService = userService;
        this.notesService = notesService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication,
            @ModelAttribute("newNote") NotesForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model,@ModelAttribute("newFile") FileForm newFile) {
        Integer userId =  getUserId(authentication);
        if(userId == null){
            model.addAttribute("result", "loginError");
            return "result";
        }
        model.addAttribute("notes", notesService.getNoteList(userId));
        model.addAttribute("credentials", credentialService.getCredentialList(userId));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("files", this.fileService.getFileList(userId));
        return "home";
    }

    private Integer getUserId(Authentication authentication) {
        String username = authentication.getName();
        if(username != null) {
            User user = userService.getUser(username);
            if(null != user) {
                return user.getUserId();
            }
            else {
                return null;
            }
        }
        else{
            return null;
        }
    }


    @PostMapping
    public String newFile(
            Authentication authentication, @ModelAttribute("newFile") FileForm newFile,  Model model) throws IOException {
        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();

        String[] fileListings = fileService.getFileList(userId);
        MultipartFile multipartFile = newFile.getFile();
        if(multipartFile.isEmpty()){

            model.addAttribute("result", "noFile");
            return "result";
        }
        if(multipartFile.getSize() > 10485760){

            model.addAttribute("result", "toBigFile");
            return "result";
        }
        String fileName = multipartFile.getOriginalFilename();
        boolean fileIsDuplicate = false;
        for (String fileListing: fileListings) {
            if (fileListing.equals(fileName)) {
                fileIsDuplicate = true;
                break;
            }
        }
        if (!fileIsDuplicate) {
            fileService.addFile(multipartFile, username);
            model.addAttribute("result", "success");
        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "Duplicate File.");
        }
        model.addAttribute("files", fileService.getFileList(userId));

        return "result";
    }

    @GetMapping(
            value = "/getFile/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody
    byte[] getFile(@PathVariable String fileName) {
        return fileService.getFile(fileName).getFileData();
    }

    @GetMapping(value = "/delFile/{fileName}")
    public String deleteFile(
            Authentication authentication, @PathVariable String fileName, @ModelAttribute("newFile") FileForm newFile,
            Model model) {
        fileService.deleteFile(fileName);
        Integer userId = getUserId(authentication);
        model.addAttribute("files", fileService.getFileList(userId));
        model.addAttribute("result", "success");

        return "result";
    }
}