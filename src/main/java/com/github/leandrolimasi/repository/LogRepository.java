package com.github.leandrolimasi.repository;

import com.github.leandrolimasi.model.LogEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by leandrolimadasilva on 25/11/2017.
 */
public interface LogRepository extends CrudRepository<LogEntity, Long>{

    @Query("select l.ipAddress from LogEntity l where l.requestDate >= ?1 group by l.ipAddress having count(l.ipAddress) >= ?2")
    List<LogEntity> findRangeDateAndThreshold(Date startDate, Long threshold);

}
