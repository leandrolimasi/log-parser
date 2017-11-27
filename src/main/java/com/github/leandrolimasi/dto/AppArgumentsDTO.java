package com.github.leandrolimasi.dto;

import com.github.leandrolimasi.enums.Duration;
import com.github.leandrolimasi.model.LogEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by leandrolimadasilva on 26/11/2017.
 */
@Data
@Builder
@AllArgsConstructor
public class AppArgumentsDTO {

    private Date startDate;

    private Date endDate;

    private Duration duration;

    private Integer threshold;

    private File logFile;

    private List<LogEntity> listLogs;

}