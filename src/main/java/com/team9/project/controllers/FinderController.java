package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.FinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class FinderController {
    @Autowired
    private FinderService finderService;

    @PostMapping("/finderInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse finderInit(@Valid @RequestBody DataRequest dataRequest) {
        return finderService.finderInit();
    }

    @PostMapping("/finderQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse finderQuery(@Valid @RequestBody DataRequest dataRequest) {
        return finderService.finderQuery(dataRequest);
    }
}
