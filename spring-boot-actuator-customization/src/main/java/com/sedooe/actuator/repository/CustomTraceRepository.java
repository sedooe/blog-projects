package com.sedooe.actuator.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.InMemoryTraceRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CustomTraceRepository extends InMemoryTraceRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomTraceRepository.class);

    public CustomTraceRepository() {
        super.setCapacity(200);
    }

    @Override
    public void add(Map<String, Object> map) {
        super.add(map);

        log.info("traced object: {}", map);
    }
}
