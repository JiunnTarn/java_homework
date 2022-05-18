package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/studentInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentInit(@Valid @RequestBody DataRequest dataRequest) {
        return studentService.studentInit(dataRequest);
    }

    @PostMapping("/studentQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentQuery(@Valid @RequestBody DataRequest dataRequest) {
        return studentService.studentQuery(dataRequest);
    }

    @PostMapping("/studentEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse studentEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return studentService.studentEditInit(dataRequest);
    }

    @PostMapping("/studentEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        return studentService.studentEditSubmit(dataRequest);
    }

    @PostMapping("/studentDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse studentDelete(@Valid @RequestBody DataRequest dataRequest) {
        return studentService.studentDelete(dataRequest);
    }
}
