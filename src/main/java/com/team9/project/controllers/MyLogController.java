package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.MyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class MyLogController {
    @Autowired
    private MyLogService myLogService;

    @PostMapping("/myLogInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myLogInit(@Valid @RequestBody DataRequest dataRequest) {
        return myLogService.myLogInit(dataRequest);
    }

    @PostMapping("/myLogQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myLogQuery(@Valid @RequestBody DataRequest dataRequest) {
        return myLogService.myLogQuery(dataRequest);
    }

    @PostMapping("/myLogEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myLogEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return myLogService.myLogEditInit(dataRequest);
    }

    @PostMapping("/myLogEditSubmit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse myLogEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        return myLogService.myLogEditSubmit(dataRequest);
    }
}
