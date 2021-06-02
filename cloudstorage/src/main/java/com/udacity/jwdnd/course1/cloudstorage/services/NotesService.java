package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class NotesService {
    private final UserMapper userMapper;
    private final NotesMapper notesMapper;

    public NotesService(UserMapper userMapper, NotesMapper notesMapper) {
        this.userMapper = userMapper;
        this.notesMapper = notesMapper;
    }

    public Notes getNote(Integer noteId) {
        return notesMapper.getNote(noteId);
    }

    public void addNote(String title, String description, String username) {
        Integer userId = userMapper.getUser(username).getUserId();
        Notes note = new Notes(0, title, description, userId);
        notesMapper.insert(note);
    }

    public void updateNote(Integer noteId, String title, String description) {
        notesMapper.updateNote(noteId, title, description);
    }

    public Notes[] getNoteList(Integer userId) {
        return notesMapper.getNotesForUser(userId);
    }


    public void deleteNote(Integer noteId) {
        notesMapper.deleteNote(noteId);
    }


}
