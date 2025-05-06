package com.prog.datenbankspiel.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SubmitQueryRequest {
    private Long taskId;
    private String queryAnswer;
}

