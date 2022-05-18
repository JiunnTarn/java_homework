package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.ConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class ConsumeController {
    @Autowired
    private ConsumeService consumeService;

    @PostMapping("/consumeInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse consumeInit(@Valid @RequestBody DataRequest dataRequest) {
        return consumeService.consumeInit(dataRequest);
    }

    @PostMapping("/consumeQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse consumeQuery(@Valid @RequestBody DataRequest dataRequest) {
        return consumeService.consumeQuery(dataRequest);
    }

    @PostMapping("/consumeEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse consumeEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return consumeService.consumeEditInit(dataRequest);
    }

    @PostMapping("/consumeEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse consumeEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        return consumeService.consumeEditSubmit(dataRequest);
    }

    @PostMapping("/consumeDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse consumeDelete(@Valid @RequestBody DataRequest dataRequest) {
        return consumeService.consumeDelete(dataRequest);
    }
}
