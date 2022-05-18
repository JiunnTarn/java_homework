package com.team9.project.service;

import com.openhtmltopdf.extend.impl.FSDefaultCacheStore;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.team9.project.models.*;
import com.team9.project.payload.request.DataRequest;
import com.team9.project.payload.response.DataResponse;
import com.team9.project.repository.mapper.StudentMapper;
import com.team9.project.util.CommonMethod;
import com.team9.project.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IntroduceService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    CourseService courseService;
    @Autowired
    RewardService rewardService;
    @Autowired
    PracticeService practiceService;
    @Autowired
    ScoreService scoreService;
    @Autowired
    private ResourceLoader resourceLoader;
    private final FSDefaultCacheStore fSDefaultCacheStore = new FSDefaultCacheStore();


    private final String[] creditStr = {"", "学院级", "校级", "省三等奖", "省二等奖", "省一等奖", "国家三等奖", "国家二等奖", "国家一等奖", "世界级"};

    public Map<String, String> getErrorMap() {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"Zh-CN\">\n" +
                "<head>\n" +
                "  <title>错误</title>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, shrink-to-fit=no\" />\n" +
                "  <meta name=\"renderer\" content=\"webkit\" />\n" +
                "  <meta name=\"force-rendering\" content=\"webkit\" />\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />\n" +
                "  <style>\n" +
                "    html,\n" +
                "    body {\n" +
                "      height: 100%;\n" +
                "      background-color: #c70000;\n" +
                "      background-image: radial-gradient(at 52% 40%, hsla(11, 86%, 64%, 1) 0, hsla(11, 86%, 64%, 0) 50%);\n" +
                "    }\n" +
                "    .wrapper {\n" +
                "      padding-top: 100px;\n" +
                "      padding-left: 20px;\n" +
                "      padding-right: 20px;\n" +
                "    }\n" +
                "    .form-signin {\n" +
                "      max-width: 280px;\n" +
                "      margin: 0 auto;\n" +
                "      background-color: #fff;\n" +
                "      padding: 30px 40px 190px 40px;\n" +
                "      border-radius: 8px;\n" +
                "      box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 5px 15px rgba(0, 0, 0, 0.35);\n" +
                "    }\n" +
                "    .form-signin .form-signin-heading,\n" +
                "    .form-signin .checkbox {\n" +
                "      font-size: 30px;\n" +
                "      margin-bottom: 30px;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "    .desc {\n" +
                "      font-size: 15px;\n" +
                "      font-weight: 400;\n" +
                "    }\n" +
                "    .btn-block {\n" +
                "      background-color: #1472e4;\n" +
                "      border: #1472e4;\n" +
                "      color: white;\n" +
                "      float: right;\n" +
                "      margin-top: 30px;\n" +
                "      align-items: flex-end;\n" +
                "      height: 40px;\n" +
                "      font-weight: bold;\n" +
                "      font-size: 15px;\n" +
                "      width: 80px !important;\n" +
                "      border-radius: 20px;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"wrapper\">\n" +
                "    <form class=\"form-signin\" action=\"index.html\" method=\"GET\">\n" +
                "      <br>\n" +
                "      <div align=\"center\">\n" +
                "        <a target=\"_blank\"><img src=\"https://s2.loli.net/2022/04/29/Xbw3gY58sT71rGO.gif\"></a>\n" +
                "      </div><br>\n" +
                "      <h2 class=\"form-signin-heading\">找不到该学生</span></h2>\n" +
                "      <h4 class=\"desc\"><span style=\"word-break:break-all;\" th:text=\"${msg}\"></span></h4>\n" +
                "    </form>\n" +
                "  </div>\n" +
                "</body>";
        Map<String, String> data = new HashMap<>();
        data.put("html", html);
        return data;
    }

    public Map<String, String> getIntroduceDataMap(String studentId) {
        StringBuilder html;

        Student student = studentMapper.getStudentByStudentInfo(studentId).get(0);
        List<Course> courses = courseService.getCourseByStudentId(studentId);
        List<Reward> rewards = rewardService.getRewardByStudentInfo(studentId);
        List<Practice> practices = practiceService.getPracticeByStudentInfo(studentId);
        List<Score> scores = scoreService.getScoreByStudentInfo(studentId);

        StringBuilder coursesStr = new StringBuilder();
        StringBuilder rewardsStr = new StringBuilder();
        StringBuilder practicesStr = new StringBuilder();
        for (int i = 0; i < courses.size(); i++) {
            coursesStr.append(courses.get(i).getCourseName());
            if (i % 5 != 0) {
                if (i != courses.size() - 1) {
                    coursesStr.append("、");
                }
            } else {
                coursesStr.append("<br/>");
            }
        }
        for (Reward r : rewards) {
            rewardsStr.append(r.getRewardName()).append(" ");
            if(r.getCredit()!=null) {
                rewardsStr.append(creditStr[r.getCredit()]);
            }
            rewardsStr.append("<br />");
        }
        for (Practice p : practices) {
            practicesStr.append(p.getPracticeName()).append("<br/>");
        }

        Map<String, String> data = new HashMap<>();
        html = new StringBuilder("<!DOCTYPE html>\n" +
                "<html lang=\"Zh-CN\">\n" +
                "<head>\n" +
                "<meta charset='UTF-8' />" +
                "  <style>\n" +
                "html { font-family: \"SourceHanSansSC\", \"Open Sans\";}"+

        "    * {\n" +
                "      padding: 0;\n" +
                "      margin: 0;\n" +
                "      box-sizing: border-box;\n" +
                "    }\n" +
                "    .form-signin .form-signin-heading,\n" +
                "    .form-signin .checkbox {\n" +
                "      font-size: 30px;\n" +
                "      margin-bottom: 15px;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "    body {\n" +
                "      display: flex;\n" +
                "      justify-content: center;\n" +
                "      align-items: center;\n" +
                "      background-color: #6498f3;\n" +
                "      background-repeat:no-repeat;\n" +
                "    }\n" +
                "    .container {\n" +
                "      padding: 20px;\n" +
                "      border-radius: 12px;\n" +
                "      overflow: hidden;\n" +
                "      margin: 100px 20px;\n" +
                "      background-color: #fff;\n" +
                "      box-shadow: 0 0 50px rgba(0, 0, 0, 0.5);\n" +
                "    }\n" +
                "    .container table {\n" +
                "      border-collapse: collapse;\n" +
                "    }\n" +
                "    .container table thead tr th {\n" +
                "      color: #fff;\n" +
                "      background-color: #4fc3a1;\n" +
                "    }\n" +
                "    .container table thead tr th:nth-child(odd) {\n" +
                "      background-color: #324960;\n" +
                "    }\n" +
                "    .container table thead tr th,\n" +
                "    .container table tbody tr td {\n" +
                "      text-align: center;\n" +
                "      padding: 20px 20px;\n" +
                "    }\n" +
                "    .container table tbody tr td {\n" +
                "      border: 1px solid rgb(200, 200, 200);\n" +
                "    }\n" +
                "    .container table tbody tr:nth-child(odd) {\n" +
                "      background-color: #f8f8f8;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <form class=\"form-signin\" action=\"index.html\" method=\"GET\">\n" +
                "      <table width=\"1100\" height=\"500\">\n" +
                "        <caption>\n" +
                "          <h2 class=\"form-signin-heading\">个人简历</h2>\n" +
                "        </caption>\n" +
                "        <tr>\n" +
                "          <td width=\"70\">姓名</td>\n" +
                "          <td width=\"100\">" + student.getName() + "</td>\n" +
                "          <td width=\"70\">出生日期</td>\n" +
                "          <td width=\"50\">" + TimeUtil.parseStr(student.getBirthday(), "yyyy-MM-dd") + "</td>\n" +
                "          <td width=\"70\">性别</td>\n" +
                "          <td width=\"90\">" + student.getSex() + "</td>\n" +
                "          <td width=\"110\" rowspan=\"4\" background=\"img/qq.jpg\">贴照片处</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td>年龄</td>\n" +
                "          <td>" + student.getAge() + "</td>\n" +
                "          <td>学历</td>\n" +
                "          <td>本科</td>\n" +
                "          <td>专业</td>\n" +
                "          <td>软件工程</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td>学校</td>\n" +
                "          <td>山东大学</td>\n" +
                "          <td>政治面貌</td>\n" +
                "          <td>" + student.getPoliticalStatus() + "</td>\n" +
                "          <td>联系方式</td>\n" +
                "          <td>" + student.getPhone() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td>籍贯</td>\n" +
                "          <td>" + student.getHometown() + "</td>\n" +
                "          <td></td>\n" +
                "          <td></td>\n" +
                "        </tr>\n" +
                "        <tr height=\"100\">\n" +
                "          <td>主修课程</td>\n" +
                "          <td colspan=\"6\">\n" +
                coursesStr +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td>技能证书</td>\n" +
                "          <td colspan=\"6\">\n" +
                rewardsStr +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td>实践活动</td>\n" +
                "          <td colspan=\"6\">\n" +
                practicesStr +
                "          </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "          <td colspan=\"10\" align=\"center\"><b>学业成绩</b></td>\n" +
                "        </tr>\n");
        for (Score s : scores) {
            html.append("        <tr>\n" + "          <td colspan=\"4\">").append(s.getCourse().getCourseName()).append("</td>\n").append("          <td colspan=\"5\">").append(s.getScore()).append("</td>\n").append("        </tr>\n");
        }

        String desc;
        if (!"无".equals(student.getClassPosition())) {
            desc = "我在大学期间任职" + student.getClassPosition() + "，工作认真负责，深受同学喜爱，";
        } else {
            desc = "我";
        }

        html.append("        <tr>\n" + "          <td colspan=\"7\" align=\"center\"><b>自我评价</b></td>\n" + "        </tr>\n" + "        <tr>\n" + "          <td colspan=\"7\" height=\"200\">\n").append(desc).append("在学习中，踏实学习本专业知识，和小组合作时，<br/>负责积极，").append("具有强烈的团队合作精神和工作能力。\n").append("          </td>\n").append("        </tr>\n").append("      </table>\n").append("    </form>\n").append("  </div>\n").append("</body></html>");
        data.put("html", html.toString());
        return data;
    }

    public ResponseEntity<StreamingResponseBody> getPdfDataFromHtml(String htmlContent) {
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.useFastMode();
            builder.useCacheStore(PdfRendererBuilder.CacheStore.PDF_FONT_METRICS, fSDefaultCacheStore);
            Resource resource = resourceLoader.getResource("classpath:font/SourceHanSansSC-Regular.ttf");
            InputStream fontInput = resource.getInputStream();
            builder.useFont(() -> fontInput, "SourceHanSansSC");
            StreamingResponseBody stream = outputStream -> {
                builder.toStream(outputStream);
                builder.run();
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(stream);

        }
        catch (Exception e) {
            return  ResponseEntity.internalServerError().build();
        }
    }

    public DataResponse getStudentIntroduceData(@Valid @RequestBody DataRequest dataRequest) {
        Map<String, String> data;
        String studentId = dataRequest.getString("studentId");
        if("".equals(studentId)||studentId==null) {
            data = getErrorMap();
        } else {
            data = getIntroduceDataMap(studentId);
        }
        return CommonMethod.getReturnData(data);
    }

    public ResponseEntity<StreamingResponseBody> getStudentIntroducePdf(DataRequest dataRequest) {
        String studentId = dataRequest.getString("studentId");
        Map<String, String> data = getIntroduceDataMap(studentId);
        String content= data.get("html");
        return getPdfDataFromHtml(content);
    }


}
