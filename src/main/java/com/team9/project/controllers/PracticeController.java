package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class PracticeController {
    @Autowired
    private PracticeService practiceService;

    @PostMapping("/practiceInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse practiceInit(@Valid @RequestBody DataRequest dataRequest) {
        return practiceService.practiceInit(dataRequest);
    }

    @PostMapping("/practiceQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse practiceQuery(@Valid @RequestBody DataRequest dataRequest) {
        return practiceService.practiceQuery(dataRequest);
    }

    @PostMapping("/practiceEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse practiceEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return practiceService.practiceEditInit(dataRequest);
    }

    @PostMapping("/practiceEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse practiceEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        return practiceService.practiceEditSubmit(dataRequest);
    }

    @PostMapping("/practiceDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse practiceDelete(@Valid @RequestBody DataRequest dataRequest) {
        return practiceService.practiceDelete(dataRequest);
    }
}
