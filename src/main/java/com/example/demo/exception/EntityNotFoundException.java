package com.example.demo.exception;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String instructorId;

}
