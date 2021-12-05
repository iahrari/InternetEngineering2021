package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Term {
    @EmbeddedId
    TermDate termDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date enrollStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date enrollEnd;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date examStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date examEnd;

    @Embeddable
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TermDate implements Serializable {
        @Digits(integer = 4, fraction = 0)
        @DecimalMin(value = "1397")
        @DecimalMax(value = "1500")
        private String year;

        @Max(1)
        @Min(0)
        private Integer term;
    }
}
