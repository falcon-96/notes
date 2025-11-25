package com.sdx.notes.service;

import com.sdx.notes.exception.AccessDeniedException;
import com.sdx.notes.model.Note;
import com.sdx.notes.model.User;
import com.sdx.notes.repository.NotesRepository;
import com.sdx.notes.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotesService notesService;

    private User user1;
    private Note note;

    private static final Long AUTHORIZED_USER_ID = 1L;
    private static final Long UNAUTHORIZED_USER_ID = 100L;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(AUTHORIZED_USER_ID);

        note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setUser(user1);
    }

    @Test
    void createNote() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(notesRepository.save(any(Note.class))).thenReturn(note);

        Note newNote = new Note();
        Note createdNote = notesService.createNote(AUTHORIZED_USER_ID, newNote);

        assertNotNull(createdNote);
        assertEquals(user1, createdNote.getUser());
    }

    @Test
    void createNote_userNotFound() {
        when(userRepository.findById(UNAUTHORIZED_USER_ID)).thenReturn(Optional.empty());
        Note newNote = new Note();
        assertThrows(EntityNotFoundException.class, () -> notesService.createNote(UNAUTHORIZED_USER_ID, newNote));
    }

    @Test
    void getNote() {
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        Note foundNote = notesService.getNote(AUTHORIZED_USER_ID, 1L);
        assertNotNull(foundNote);
        assertEquals(note.getId(), foundNote.getId());
    }

    @Test
    void getNote_accessDenied() {
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        assertThrows(AccessDeniedException.class, () -> notesService.getNote(UNAUTHORIZED_USER_ID, 1L));
    }

    @Test
    void updateNote() {
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        when(notesRepository.saveAndFlush(any(Note.class))).thenReturn(note);

        Note updatedNote = notesService.updateNote(AUTHORIZED_USER_ID, note);

        assertNotNull(updatedNote);
    }

    @Test
    void updateNote_accessDenied() {
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        assertThrows(AccessDeniedException.class, () -> notesService.updateNote(UNAUTHORIZED_USER_ID, note));
    }

    @Test
    void deleteNote() {
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        doNothing().when(notesRepository).deleteById(1L);
        notesService.deleteNote(AUTHORIZED_USER_ID, 1L);
        verify(notesRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNote_accessDenied() {
        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));
        assertThrows(AccessDeniedException.class, () -> notesService.deleteNote(UNAUTHORIZED_USER_ID, 1L));
    }
}
