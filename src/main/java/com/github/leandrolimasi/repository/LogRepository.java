package com.github.leandrolimasi.repository;

import com.github.leandrolimasi.model.LogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by leandrolimadasilva on 25/11/2017.
 */
public interface LogRepository extends CrudRepository<LogEntity, Long>{

}
