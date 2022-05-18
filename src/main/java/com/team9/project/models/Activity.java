package com.team9.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Activity {
    private Integer id;
    private String activityName;
    private String category;
    private String initiator;
    private Date startTime;
    private Date endTime;
    private List<Student> students;
}
