package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.CourseSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class CourseSelectController {
    @Autowired
    private CourseSelectService courseSelectService;

    @PostMapping("/courseSelectInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseSelectInit(@Valid @RequestBody DataRequest dataRequest) {
        return courseSelectService.courseSelectInit(dataRequest);
    }

    @PostMapping("/courseSelectQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseSelectQuery(@Valid @RequestBody DataRequest dataRequest) {
        return courseSelectService.courseSelectQuery(dataRequest);
    }
}
