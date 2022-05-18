package com.team9.project.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class Reward {
    private Integer id;
    private String rewardName;
    private Date rewardTime;
    private String rewardId;
    private Student student;
    private Integer credit;
}
