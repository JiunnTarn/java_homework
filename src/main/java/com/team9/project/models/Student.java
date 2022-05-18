package com.team9.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String studentId;
    private String citizenId;
    private String phone;
    private String hometown;
    private String highSchool;
    private String politicalStatus;
    private String classPosition;
    private String sex;
    private Integer age;
    private String dept;
    private Date birthday;
}
