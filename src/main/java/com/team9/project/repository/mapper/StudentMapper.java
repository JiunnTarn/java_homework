package com.team9.project.repository.mapper;

import com.team9.project.models.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> getStudentById(Integer id);
    List<Student> getStudentByStudentInfo(String studentInfo);
    List<Student> findAll();
    Integer getMaxId();
    void addStudent(Student student);
    void deleteStudentById(Integer id);
    void deleteStudentCourseById(Integer id);
    void updateStudent(Student student);
}
