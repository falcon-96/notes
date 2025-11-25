package com.sdx.notes.service;

import com.sdx.notes.model.Note;
import com.sdx.notes.model.User;
import com.sdx.notes.repository.NotesRepository;
import com.sdx.notes.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NotesService {
    private final NotesRepository notesRepository;
    private final UserRepository userRepository;

    public NotesService(NotesRepository notesRepository, UserRepository userRepository) {
        this.notesRepository = notesRepository;
        this.userRepository = userRepository;
    }

    public Note createNote(Long userId, Note note) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        note.setUser(user);
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
