package com.team9.project.repository.mapper;

import com.team9.project.models.Reward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RewardMapper {
    List<Reward> getRewardById(int id);
    List<Reward> getRewardByRewardInfo(String info);
    List<Reward> getRewardByStudentInfo(String studentInfo);
    List<Reward> findAll();
    Integer getMaxId();
    void addReward(Reward reward);
    void deleteReward(Integer id);
    void updateReward(Reward reward);
    void deleteAllStudent(int rid);
    Integer insertStudent(int sid, int rid);
}
