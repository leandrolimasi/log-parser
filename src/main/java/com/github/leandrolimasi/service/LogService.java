package com.github.leandrolimasi.service;

import com.github.leandrolimasi.dto.AppArgumentsRequest;
import com.github.leandrolimasi.dto.LogResponse;
import com.github.leandrolimasi.enums.Duration;
import com.github.leandrolimasi.exception.LogParserException;
import com.github.leandrolimasi.model.LogEntity;
import com.github.leandrolimasi.repository.LogRepository;
import com.github.leandrolimasi.util.Utils;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by leandrolimadasilva on 25/11/2017.
 */
@Component
@Log4j
public class LogService {

    @Autowired
    private LogRepository logRepository;

    /** Persist the logs entries in database
     *
     * @param file
     * @return
     */
    public void persistRequestLog(File file){

        List<LogEntity> requestLogs = mountRequestLogs(file);

        log.info("=========================================");
        log.info("objects count in log file: " +requestLogs.size());

        requestLogs.forEach(item ->{
            try{
                logRepository.save(item);
            }catch(Exception e){
                log.error(e);
            }
        });

    }


    /** Find the Requeted Logs in database
     *
     * @param appArguments
     * @return
     */
    public Optional<List<LogResponse>> findResquestLogs(AppArgumentsRequest appArguments){

        LocalDateTime localDateTimeEnd = appArguments.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (Duration.HOURLY.equals(appArguments.getDuration())){
            localDateTimeEnd = localDateTimeEnd.plusHours(1);
        } else {
            localDateTimeEnd = localDateTimeEnd.plusDays(1);
        }

        appArguments.setEndDate(Date.from(localDateTimeEnd.atZone(ZoneId.systemDefault()).toInstant()));

        return Optional.ofNullable(logRepository.findRangeDateAndThreshold(
                appArguments.getStartDate(), appArguments.getEndDate(), appArguments.getThreshold()));
    }


    /** Print the log entries found
     *
     * @param logEntities
     */
    public void printRequestLogsFiltered(final List<LogResponse> logEntities){

        logEntities.stream()
                .map(logResponse -> String.format(
                        "%s: \t%d requests",
                        logResponse.getIp(),
                        logResponse.getCount())
                )
                .forEach(log::info);


        log.info("=========================================");
        log.info(String.format(" %d ips found", logEntities.size()));
        log.info("=========================================");
    }


    private List<LogEntity> mountRequestLogs(File file){
        Scanner scanner = readFile(file);
        return parsRequestLogs(scanner);
    }

    private List<LogEntity> parsRequestLogs(Scanner scanner){
        scanner.useDelimiter("\\s*\\|\\s*");

        List<LogEntity> listLogs = new LinkedList<>();

        while(scanner.hasNext()) {
            parseRequestLog(scanner.next())
                    .ifPresent(listLogs::add);
        }

        scanner.close();

        return listLogs;
    }

    private Scanner readFile(File file){
        log.info("Starting read file: " + file.getAbsolutePath());
        try {
            return new Scanner(file, "UTF-8");
        }catch (FileNotFoundException ex){
            throw new LogParserException(String.format(
                    "The log file '%s' was not found",
                    file
            ));
        }
    }

    private Optional<LogEntity> parseRequestLog(String token) {
        log.info(token);
        final String[] tokens = token.split("\\s+");

        final Optional<LogEntity> logEntity = Optional.ofNullable(LogEntity.builder()
                .requestDate(Utils.parseDate(tokens[0]).get()).ipAddress(tokens[1]).build());

        return logEntity;
    }



}
