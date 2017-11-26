package com.github.leandrolimasi;

import com.github.leandrolimasi.exception.LogParserException;
import com.github.leandrolimasi.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

import java.io.File;
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
        parseLogFileArg(args).ifPresent(logService::persistRequestLog);
    }


    /**
     *
     * @param args
     * @return
     * @throws LogParserException
     */
    private Optional<File> parseLogFileArg(ApplicationArguments args) throws LogParserException {

        if (!args.containsOption("logfile")) {
            return Optional.empty();
        }

        return Optional.of(args.getOptionValues("logfile").get(0))
                .map(File::new)
                .filter(File::canRead)
                .map(Optional::of)
                .orElseThrow(() -> new LogParserException(
                        "The specified log file cannot be read"
                ));
    }

}
