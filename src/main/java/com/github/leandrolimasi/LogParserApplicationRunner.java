package com.github.leandrolimasi;

import com.github.leandrolimasi.dto.AppArgumentsDTO;
import com.github.leandrolimasi.enums.Duration;
import com.github.leandrolimasi.exception.LogParserException;
import com.github.leandrolimasi.service.LogService;
import com.github.leandrolimasi.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
@Log4j
public class LogParserApplicationRunner implements ApplicationRunner {

    @Autowired
    private LogService logService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Your application started with arguments :" + args.getOptionNames());
        Optional<AppArgumentsDTO> appArgumentsDTO = parseAppArgs(args);
        logService.persistRequestLog(appArgumentsDTO.get().getLogFile());
    }


    /**
     *
     * @param args
     * @return
     * @throws LogParserException
     */
    private Optional<AppArgumentsDTO> parseAppArgs(ApplicationArguments args) throws LogParserException {

        if (!args.containsOption("logfile") ||
                !args.containsOption("startDate") ||
                args.containsOption("duration") ||
                args.containsOption("threshold")) {
            return Optional.empty();
        }


        final Date startDate = Optional.of(args.getOptionValues("startDate").get(0))
                .filter(text -> !text.isEmpty())
                .flatMap(Utils::parseDate)
                .orElseThrow(() -> new LogParserException(
                        "The argument startDate is missing or invalid. The format is " +
                                "yyyy-MM-dd.HH:mm:ss, e.g. 2017-01-01.13:00:00."
                ));

        final Duration duration = Optional.of(args.getOptionValues("duration").get(0))
                .filter(text -> !text.isEmpty())
                .flatMap(Duration::parseFromParamName)
                .orElseThrow(() -> new LogParserException(
                        "The argument duration is missing or invalid. The possible values are " +
                                "hourly or daily."
                ));

        final Integer threshold = Optional.of(args.getOptionValues("threshold").get(0))
                .filter(text -> !text.isEmpty())
                .filter(text -> text.matches("\\d+"))
                .map(Integer::parseInt)
                .filter(val -> val > 0)
                .orElseThrow(() -> new LogParserException(
                        "The argument threshold is missing or invalid. Specify a natural value" +
                                " no less than 0."
                ));

        final File logFile = Optional.of(args.getOptionValues("logfile").get(0))
                .map(File::new)
                .filter(File::canRead)
                .map(Optional::of)
                .map(Optional::get)
                .orElseThrow(() -> new LogParserException(
                        "The specified log file cannot be read"
                ));

        return Optional.of(AppArgumentsDTO.builder()
                .startDate(startDate).duration(duration).threshold(threshold).logFile(logFile).build());
    }
}
