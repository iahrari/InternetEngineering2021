package com.example.demo.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeTableDTO {
    private final InstructorCourse[] saturday = new InstructorCourse[6];
    private final InstructorCourse[] sunday = new InstructorCourse[6];
    private final InstructorCourse[] monday = new InstructorCourse[6];
    private final InstructorCourse[] thursday = new InstructorCourse[6];
    private final InstructorCourse[] wednesday = new InstructorCourse[6];
    private final InstructorCourse[] tuesday = new InstructorCourse[6];

    public static TimeTableDTO convertFromTimeTables(List<InstructorCourse> list){
        var dto = new TimeTableDTO();

        for(var c: list){
            addToDTO(dto, c);
        }

        return dto;
    }

    private static void addToDTO(TimeTableDTO dto, InstructorCourse ic){
        if (ic.getTimeTable().getSaturday() != null){
            var index = (ic.getTimeTable().getSaturday() - 8) / 2;
            dto.saturday[index] = ic;
        }

        if (ic.getTimeTable().getSunday() != null){
            var index = (ic.getTimeTable().getSunday() - 8) / 2;
            dto.sunday[index] = ic;
        }

        if (ic.getTimeTable().getMonday() != null){
            var index = (ic.getTimeTable().getMonday() - 8) / 2;
            dto.monday[index] = ic;
        }

        if (ic.getTimeTable().getTuesday() != null){
            var index = (ic.getTimeTable().getTuesday() - 8) / 2;
            dto.tuesday[index] = ic;
        }

        if (ic.getTimeTable().getWednesday() != null){
            var index = (ic.getTimeTable().getWednesday() - 8) / 2;
            dto.wednesday[index] = ic;
        }

        if (ic.getTimeTable().getThursday() != null){
            var index = (ic.getTimeTable().getThursday() - 8) / 2;
            dto.thursday[index] = ic;
        }
    }
}
