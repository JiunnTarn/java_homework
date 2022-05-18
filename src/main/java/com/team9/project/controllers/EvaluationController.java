package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class EvaluationController {
    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/evaluationInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse evaluationInit(@Valid @RequestBody DataRequest dataRequest) {
        return evaluationService.evaluationInit(dataRequest);
    }

}
