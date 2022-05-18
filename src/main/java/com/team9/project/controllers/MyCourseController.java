package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.MyCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class MyCourseController {
    @Autowired
    private MyCourseService myCourseService;

    @PostMapping("/myCourseInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCourseInit(@Valid @RequestBody DataRequest dataRequest) {
        return myCourseService.myCourseInit(dataRequest);
    }

    @PostMapping("/myCourseQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myCourseQuery(@Valid @RequestBody DataRequest dataRequest) {
        return myCourseService.myCourseQuery(dataRequest);
    }
}
