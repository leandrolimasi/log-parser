package com.github.leandrolimasi.repository;

import com.github.leandrolimasi.dto.LogResponse;
import com.github.leandrolimasi.model.LogEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by leandrolimadasilva on 25/11/2017.
 */
public interface LogRepository extends CrudRepository<LogEntity, Long>{

    @Query("select new com.github.leandrolimasi.dto.LogResponse(l.ipAddress, count(l.ipAddress)) " +
            "from LogEntity l where l.requestDate >= ?1 and l.requestDate <= ?2 group by l.ipAddress having count(l.ipAddress) >= ?3")
    List<LogResponse> findRangeDateAndThreshold(Date startDate, Date endDate, Long threshold);

}
