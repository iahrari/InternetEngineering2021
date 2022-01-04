package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Roles {
    ROLE_ADMIN("ROLE_ADMIN", "/admin", "آموزش"),
    ROLE_INSTRUCTOR("ROLE_INSTRUCTOR", "/instructor", "استاد"),
    ROLE_STUDENT("ROLE_STUDENT", "/student", "دانشجو");

    @Getter
    private final String role;
    @Getter
    private final String url;
    @Getter
    private final String persianName;
}
