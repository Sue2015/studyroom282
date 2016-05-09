package com.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.content.TimeTable;

public interface TimeTableRepository extends MongoRepository<TimeTable, String> {
	//public Customer findByFirstName(String firstName);
    public TimeTable findByDate(String date);
    public List<TimeTable> findAll();
}
