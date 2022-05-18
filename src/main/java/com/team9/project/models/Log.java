package com.team9.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Log {
    private Integer id;
    private Student student;
    private String destination;
    private Date applyTime;
    private Date leaveTime;
    private Date returnTime;
    private String reason;
    private String status;
}
