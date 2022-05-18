package com.team9.project.controllers;

import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.service.IntroduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;


// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class IntroduceController {
    @Autowired
    private IntroduceService introduceService;


    @PostMapping("/getStudentIntroduceData")
    @PreAuthorize(" hasRole('ADMIN')")
    public DataResponse getStudentIntroduceData(@Valid @RequestBody DataRequest dataRequest) {
        return introduceService.getStudentIntroduceData(dataRequest);
    }


    @PostMapping("/getStudentIntroducePdf")
    public ResponseEntity<StreamingResponseBody> getStudentIntroducePdf(@Valid @RequestBody DataRequest dataRequest) {
        return introduceService.getStudentIntroducePdf(dataRequest);
    }


}
