package com.github.leandrolimasi.service;

import com.github.leandrolimasi.dto.AppArgumentsDTO;
import com.github.leandrolimasi.exception.LogParserException;
import com.github.leandrolimasi.model.LogEntity;
import com.github.leandrolimasi.repository.LogRepository;
import com.github.leandrolimasi.util.Utils;
import lombok.Builder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by leandrolimadasilva on 25/11/2017.
 */
@Component
@Log4j
public class LogService {

    @Autowired
    private LogRepository logRepository;

    /**
     *
     * @param file
     * @return
     */
    public void persistRequestLog(File file){

        List<LogEntity> requestLogs = mountRequestLogs(file);

        log.info("Objects count: " +requestLogs.size());

        requestLogs.forEach(item ->{
            try{
                logRepository.save(item);
            }catch(Exception e){
                log.error(e);
            }
        });

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        LocalDate localDate = LocalDate.parse(tokens[0], formatter);
        Date requestDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        final Optional<LogEntity> logEntity = Optional.ofNullable(LogEntity.builder()
                .requestDate(Utils.parseDate(tokens[0]).get()).ipAddress(tokens[1]).build());

        return logEntity;
    }



}
