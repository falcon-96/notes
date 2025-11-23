package com.sdx.notes.service;

import com.sdx.notes.model.Note;
import com.sdx.notes.repository.NotesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NotesService {
    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public Note createNote(Note note) {
        return notesRepository.save(note);
    }

    public Note getNote(long id) {
        return notesRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Note updateNote(Note note) {
        return notesRepository.saveAndFlush(note);
    }

    public void deleteNote(long id) {
        if (!notesRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        notesRepository.deleteById(id);
    }
}
