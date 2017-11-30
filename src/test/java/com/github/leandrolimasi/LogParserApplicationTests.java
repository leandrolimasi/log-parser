package com.github.leandrolimasi;

import com.github.leandrolimasi.dto.AppArgumentsRequest;
import com.github.leandrolimasi.dto.LogResponse;
import com.github.leandrolimasi.enums.Duration;
import com.github.leandrolimasi.exception.LogParserException;
import com.github.leandrolimasi.model.LogEntity;
import com.github.leandrolimasi.repository.LogRepository;
import com.github.leandrolimasi.service.LogService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogParserApplicationTests {

	@TestConfiguration
	static class LogServiceTestContextConfiguration {

		@Bean
		public LogService logService() {
			return new LogService();
		}
	}

	@Autowired
	private LogService logService;

    @MockBean
    private LogRepository logRepository;


    @Before
    public void setUp() {
        LogEntity log = new LogEntity();
        log.setIpAddress("192.168.0.1");
        log.setRequestDate(new Date());

        Mockito.when(logRepository.save(log)).thenReturn(log);
    }

	@Test(expected = LogParserException.class)
	public void testUnkonwFile() {
        File logFile = new File("fail");
        logService.persistRequestLog(logFile);
	}

    @Test
    public void testEmptyFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File logFile = new File(classLoader.getResource("empty.log").getFile());
        List<LogEntity> result = logService.persistRequestLog(logFile);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 0);
    }

    @Test(expected = DateTimeParseException.class)
    public void testErrorDateFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File logFile = new File(classLoader.getResource("error.log").getFile());
        logService.persistRequestLog(logFile);
    }

    @Test(expected = LogParserException.class)
    public void testErrorIpFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File logFile = new File(classLoader.getResource("error2.log").getFile());
        logService.persistRequestLog(logFile);
    }

    @Test
    public void testPersistFile() {

        ClassLoader classLoader = getClass().getClassLoader();
        File logFile = new File(classLoader.getResource("test.log").getFile());

        LogEntity log = new LogEntity();
        log.setIpAddress("192.168.0.1");
        log.setRequestDate(new Date());

        Mockito.when(logRepository.save(log)).thenReturn(log);

        List<LogEntity> result = logService.persistRequestLog(logFile);

        Assert.assertEquals(result.size(), 5);
    }

    @Test
    public void testFindResquestLogs() {

        List<LogResponse> response = new ArrayList<>();
        response.add(LogResponse.builder().ip("192.168.0.1").count(1L).build());

        LocalDate startLocalDate = LocalDate.of(2017, Month.NOVEMBER, 1);
        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        AppArgumentsRequest appArgumentsRequest = AppArgumentsRequest.builder()
                .duration(Duration.DAILY).startDate(startDate).threshold(2L).build();

        Mockito.when(logRepository.findRangeDateAndThreshold(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);

        List<LogResponse> result = logService.findResquestLogs(appArgumentsRequest).get();

        logService.printRequestLogsFiltered(result);

        Assert.assertNotNull(result);

        Assert.assertEquals(result.size(), 1);
    }

}
