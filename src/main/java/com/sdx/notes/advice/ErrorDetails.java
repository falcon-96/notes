package com.sdx.notes.advice;

import java.time.LocalDateTime;

public record ErrorDetails(LocalDateTime dateTime, String message) {

}