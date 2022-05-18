package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.ParseException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @PostMapping("/activityInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityInit(@Valid @RequestBody DataRequest dataRequest) {
        return activityService.activityInit(dataRequest);
    }

    @PostMapping("/activityQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityQuery(@Valid @RequestBody DataRequest dataRequest) {
        return activityService.activityQuery(dataRequest);
    }

    @PostMapping("/activityEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse activityEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return activityService.activityEditInit(dataRequest);
    }

    @PostMapping("/activityEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse activityEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        return activityService.activityEditSubmit(dataRequest);
    }

    @PostMapping("/activityDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse activityDelete(@Valid @RequestBody DataRequest dataRequest) {
        return activityService.activityDelete(dataRequest);
    }
}
