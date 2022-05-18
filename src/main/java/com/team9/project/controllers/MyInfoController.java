package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.MyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class MyInfoController {
    @Autowired
    private MyInfoService myInfoService;

    @PostMapping("/myInfoInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myInfoInit(@Valid @RequestBody DataRequest dataRequest) {
        return myInfoService.myInfoInit(dataRequest);
    }

}
