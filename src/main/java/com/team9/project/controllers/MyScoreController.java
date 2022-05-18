package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.MyScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class MyScoreController {
    @Autowired
    private MyScoreService myScoreService;

    @PostMapping("/myScoreInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myScoreInit(@Valid @RequestBody DataRequest dataRequest) {
        return myScoreService.myScoreInit(dataRequest);
    }

    @PostMapping("/myScoreQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myScoreQuery(@Valid @RequestBody DataRequest dataRequest) {
        return myScoreService.myScoreQuery(dataRequest);
    }
}
