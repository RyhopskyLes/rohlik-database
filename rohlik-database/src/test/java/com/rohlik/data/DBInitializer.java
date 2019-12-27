package com.rohlik.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.CategoryKosikDao;
import com.rohlik.data.entities.Category;

@Service
@Profile("test")
public class DBInitializer {
private Logger logger = LoggerFactory.getLogger(DBInitializer.class);
@Autowired
CategoryKosikDao catKosikDao;
@Autowired
CategoryDao catDao;
@PostConstruct
public void initDB() {
	logger.info("Starting database initialization...");
	try {
		

		FileInputStream fi = new FileInputStream(new File("categories.txt"));
		ObjectInputStream oi = new ObjectInputStream(fi);

		for(int i=0; i<2143; i++) {
			Category category=(Category) oi.readObject(); 
			Category saved =catDao.save(category);
				}

		oi.close();
		fi.close();

	} catch (FileNotFoundException e) {
		System.out.println("File not found");
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	logger.info("Database initialization finished.");
}
}
