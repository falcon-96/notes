package com.sdx.notes.controller;

import com.sdx.notes.model.Note;
import com.sdx.notes.service.NotesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note")
public class NotesController {
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestHeader(name = "x-user-id") Long userId, @RequestBody Note note) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notesService.createNote(userId, note));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@RequestHeader(name = "x-user-id") Long userId, @PathVariable(name = "id") Long noteId) {
        return ResponseEntity
                .ok(notesService.getNote(userId, noteId));
    }

    @PutMapping
    public ResponseEntity<Note> updateNote(@RequestHeader(name = "x-user-id") Long userId, @RequestBody Note note) {
        return ResponseEntity
                .ok(notesService.updateNote(userId, note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@RequestHeader(name = "x-user-id") Long userId, @PathVariable(name = "id") Long noteId) {
        notesService.deleteNote(userId, noteId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
