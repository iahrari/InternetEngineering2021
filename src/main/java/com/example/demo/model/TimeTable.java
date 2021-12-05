package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeTable implements Serializable {
    @Min(8)
    @Max(18)
    private Short saturday;

    @Min(8)
    @Max(18)
    private Short sunday;

    @Min(8)
    @Max(18)
    private Short monday;

    @Min(8)
    @Max(18)
    private Short tuesday;

    @Min(8)
    @Max(18)
    private Short wednesday;

    @Min(8)
    @Max(18)
    private Short thursday;

    public boolean conflict(TimeTable other){
        return  (saturday != null && other.saturday != null && Objects.equals(saturday, other.saturday)) ||
                (sunday != null && other.sunday!= null && Objects.equals(sunday, other.sunday)) ||
                (monday != null && other.monday != null && Objects.equals(monday, other.monday)) ||
                (tuesday != null && other.tuesday != null && Objects.equals(tuesday, other.tuesday)) ||
                (wednesday != null && other.wednesday != null &&Objects.equals(wednesday, other.wednesday)) ||
                (thursday != null && other.thursday != null && Objects.equals(thursday, other.thursday));
    }
}
