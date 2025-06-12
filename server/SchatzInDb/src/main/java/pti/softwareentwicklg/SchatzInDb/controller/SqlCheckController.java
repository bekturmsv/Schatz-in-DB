package pti.softwareentwicklg.SchatzInDb.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pti.softwareentwicklg.SchatzInDb.dto.request.SqlCheckRequest;
import pti.softwareentwicklg.SchatzInDb.dto.request.TestCheckRequest;
import pti.softwareentwicklg.SchatzInDb.dto.response.SqlCheckResponse;
import pti.softwareentwicklg.SchatzInDb.service.task.SqlCheckService;

@RestController
@RequestMapping("/api/sql")
public class SqlCheckController {

    private final SqlCheckService sqlCheckService;

    public SqlCheckController(SqlCheckService sqlCheckService) {
        this.sqlCheckService = sqlCheckService;
    }

    @PostMapping("/validate")
    public SqlCheckResponse validateSql(@RequestBody SqlCheckRequest request) {
        return sqlCheckService.validateUserSql(request.getUserSql(), request.getTaskCode());
    }

    @PostMapping("/test/validate")
    public SqlCheckResponse validateTest(@RequestBody TestCheckRequest request) {
        return sqlCheckService.validateUserTest(request);
    }
}