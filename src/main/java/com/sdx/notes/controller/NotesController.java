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
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notesService.createNote(note));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity
                .ok(notesService.getNote(id));
    }

    @PutMapping
    public ResponseEntity<Note> updateNote(@RequestBody Note note) {
        return ResponseEntity
                .ok(notesService.updateNote(note));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") Long id) {
        notesService.deleteNote(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
