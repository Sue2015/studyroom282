package com.model;

import com.content.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {
	public Student findById(String sid);
	public Long deleteById(String sid);
	public List<Student> findAll();
}