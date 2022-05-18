package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @PostMapping("/scoreInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse scoreInit(@Valid @RequestBody DataRequest dataRequest) {
        return scoreService.scoreInit(dataRequest);
    }

    @PostMapping("/scoreQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse scoreQuery(@Valid @RequestBody DataRequest dataRequest) {
        return scoreService.scoreQuery(dataRequest);
    }

    @PostMapping("/scoreEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse scoreEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return scoreService.scoreEditInit(dataRequest);
    }

    @PostMapping("/scoreEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse scoreEditSubmit(@Valid @RequestBody DataRequest dataRequest) {
        return scoreService.scoreEditSubmit(dataRequest);
    }

    @PostMapping("/scoreDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse scoreDelete(@Valid @RequestBody DataRequest dataRequest) {
        return scoreService.scoreDelete(dataRequest);
    }
}
