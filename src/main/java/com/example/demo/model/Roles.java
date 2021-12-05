package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Roles {
    ROLE_ADMIN("ROLE_ADMIN", "/admin"),
    ROLE_INSTRUCTOR("ROLE_INSTRUCTOR", "/instructor"),
    ROLE_STUDENT("ROLE_STUDENT", "/student");

    @Getter
    private final String role;
    @Getter
    private final String url;
}
