package com.team9.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Practice {
    private Integer id;
    private String practiceName;
    private String category;
    private Date startTime;
    private Date endTime;
    private Integer credit;
    private List<Student> students;
}
