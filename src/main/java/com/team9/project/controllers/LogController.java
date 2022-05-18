package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class LogController {
    @Autowired
    private LogService logService;

    @PostMapping("/logInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse logInit(@Valid @RequestBody DataRequest dataRequest) {
        return logService.logInit(dataRequest);
    }

    @PostMapping("/logQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse logQuery(@Valid @RequestBody DataRequest dataRequest) {
        return logService.logQuery(dataRequest);
    }

    @PostMapping("/logEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse logEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return logService.logEditInit(dataRequest);
    }

    @PostMapping("/logEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse logEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        return logService.logEditSubmit(dataRequest);
    }

    @PostMapping("/logDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse logDelete(@Valid @RequestBody DataRequest dataRequest) {
        return logService.logDelete(dataRequest);
    }

}
