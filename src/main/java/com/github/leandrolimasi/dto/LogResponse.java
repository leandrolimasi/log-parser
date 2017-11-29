package com.github.leandrolimasi.dto;

import com.github.leandrolimasi.enums.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.Date;

/**
 * Created by leandrolimadasilva on 26/11/2017.
 */
@Data
@Builder
@AllArgsConstructor
public class LogResponse {

    private String ip;
    private Long count;

}
