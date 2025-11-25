package com.sdx.notes.service;

import com.sdx.notes.exception.AccessDeniedException;
import com.sdx.notes.model.Note;
import com.sdx.notes.model.User;
import com.sdx.notes.repository.NotesRepository;
import com.sdx.notes.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotesService {
    private final NotesRepository notesRepository;
    private final UserRepository userRepository;
    private static final String NOT_FOUND_MESSAGE = "Note Not Found";

    public NotesService(NotesRepository notesRepository, UserRepository userRepository) {
        this.notesRepository = notesRepository;
        this.userRepository = userRepository;
    }

    public Note createNote(Long userId, Note note) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        note.setUser(user);
        return notesRepository.save(note);
    }

    public Note getNote(Long userId, long noteId) {
        Note note = notesRepository.findById(noteId).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
        if (!Objects.equals(note.getUser().getId(), userId)) {
            throw new AccessDeniedException("User not authorized to view this resource.");
        }

        return note;
    }

    public Note updateNote(Long userId, Note note) {
        Note existingNote = notesRepository.findById(note.getId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));

        if (!Objects.equals(existingNote.getUser().getId(), userId)) {
            throw new AccessDeniedException("Unauthorized access to update a resource.");
        }

        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());

        return notesRepository.saveAndFlush(existingNote);
    }

    public void deleteNote(Long userId, long noteId) {
        long existingUserId = notesRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE))
                .getUser()
                .getId();

        if (existingUserId != userId) {
            throw new AccessDeniedException("Unauthorized access to delete a resource.");
        }

        notesRepository.deleteById(noteId);
    }
}
