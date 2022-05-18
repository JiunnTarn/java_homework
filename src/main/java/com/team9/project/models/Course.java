package com.team9.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class Course {
    private Integer id;
    private String courseName;
    private String courseId;
    private String book;
    private String courseWare;
    private String resource;
    private String teacher;
    private String time;
    private String credit;
    private List<Student> students;
}
