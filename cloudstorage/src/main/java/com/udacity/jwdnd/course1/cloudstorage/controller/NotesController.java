package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.NotesForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("note")
public class NotesController {

    private final NotesService notesService;
    private final UserService userService;

    public NotesController(NotesService notesService, UserService userService) {
        this.notesService = notesService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication, @ModelAttribute("newNote") NotesForm newNote,
            Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("notes", this.notesService.getNoteList(userId));

        return "home";
    }

    private Integer getUserId(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        return user.getUserId();
    }

    @PostMapping("newNote")
    public String newNote(Authentication authentication, @ModelAttribute("newNote") NotesForm newNote, Model model) {
        String username = authentication.getName();
        String title = newNote.getTitle();
        String noteId = newNote.getNoteId();
        String description = newNote.getDescription();
        if (noteId.isEmpty()) {
            notesService.addNote(title, description, username);
        } else {
            Notes existingNote = notesService.getNote(Integer.parseInt(noteId));
            notesService.updateNote(existingNote.getNoteId(), title, description);
        }
        Integer userId = getUserId(authentication);
        model.addAttribute("notes", notesService.getNoteList(userId));
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping(value = "/getNote/{noteId}")
    public Notes getNote(@PathVariable Integer noteId) {
        return notesService.getNote(noteId);
    }

    @GetMapping(value = "/delNote/{noteId}")
    public String deleteNote(
            Authentication authentication, @ModelAttribute("newNote") NotesForm newNote,
            @PathVariable Integer noteId, Model model) {
        notesService.deleteNote(noteId);
        Integer userId = getUserId(authentication);
        model.addAttribute("notes", notesService.getNoteList(userId));
        model.addAttribute("result", "success");
        return "result";
    }
}
