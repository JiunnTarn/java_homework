package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/courseInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseInit(@Valid @RequestBody DataRequest dataRequest) {
        return courseService.courseInit(dataRequest);
    }

    @PostMapping("/courseQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseQuery(@Valid @RequestBody DataRequest dataRequest) {
        return courseService.courseQuery(dataRequest);
    }

    @PostMapping("/courseEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse courseEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return courseService.courseEditInit(dataRequest);
    }

    @PostMapping("/courseEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse courseEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        return courseService.courseEditSubmit(dataRequest);
    }

    @PostMapping("/courseDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest) {
        return courseService.courseDelete(dataRequest);
    }
}
