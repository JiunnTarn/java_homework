package com.team9.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Score {
    private Integer id;
    private Student student;
    private Course course;
    private Integer score;
}
