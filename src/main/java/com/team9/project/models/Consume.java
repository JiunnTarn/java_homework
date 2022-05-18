package com.team9.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Consume {
    private Integer id;
    private String type;
    private String reason;
    private Date time;
    private Integer amount;
    private Student student;
}
