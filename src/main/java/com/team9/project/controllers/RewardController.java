package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.ParseException;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class RewardController {
    @Autowired
    private RewardService rewardService;

    @PostMapping("/rewardInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse rewardInit(@Valid @RequestBody DataRequest dataRequest) {
        return rewardService.rewardInit(dataRequest);
    }

    @PostMapping("/rewardQuery")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse rewardQuery(@Valid @RequestBody DataRequest dataRequest) {
        return rewardService.rewardQuery(dataRequest);
    }

    @PostMapping("/rewardEditInit")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse rewardEditInit(@Valid @RequestBody DataRequest dataRequest) {
        return rewardService.rewardEditInit(dataRequest);
    }

    @PostMapping("/rewardEditSubmit")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse rewardEditSubmit(@Valid @RequestBody DataRequest dataRequest) throws ParseException {
        return rewardService.rewardEditSubmit(dataRequest);
    }

    @PostMapping("/rewardDelete")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse rewardDelete(@Valid @RequestBody DataRequest dataRequest) {
        return rewardService.rewardDelete(dataRequest);
    }
}

