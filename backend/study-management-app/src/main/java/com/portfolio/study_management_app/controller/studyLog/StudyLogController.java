package com.portfolio.study_management_app.controller.studyLog;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.study_management_app.dto.common.ApiResponseDto;
import com.portfolio.study_management_app.dto.studyLog.CreateStudyLogRequsetDto;
import com.portfolio.study_management_app.dto.studyLog.StudyLogResponseDto;
import com.portfolio.study_management_app.service.studyLog.StudyLogService;

@RestController
@RequestMapping("/api/studyLog")
public class StudyLogController {
  private final StudyLogService studyLogService;

  public StudyLogController(StudyLogService studyLogService) {
    this.studyLogService = studyLogService;
  }
  @PostMapping
  public ApiResponseDto<StudyLogResponseDto> ctreateStudyLog(@RequestBody CreateStudyLogRequsetDto req) {
    StudyLogResponseDto res = studyLogService.createStudyLog(req);
    return new ApiResponseDto<>("SUCCESS", res, null);
  }
}
