package com.team9.project.repository.mapper;

import com.team9.project.models.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> getCourseById(Integer id);
    List<Course> getCourseByCourseInfo(String info);
    List<Course> getCourseByStudentInfo(String studentInfo);
    List<Course> findAll();
    Integer getMaxId();
    void addCourse(Course course);
    void deleteCourseById(Integer id);
    void updateCourse(Course course);
    void deleteAllStudent(int cid);
    int insertStudent(int sid, int cid);
    void withdrawCourse(@Param("cid") Integer cid, @Param("sid") Integer sid);
    void selectCourse(@Param("cid") Integer cid, @Param("sid") Integer sid);
}
