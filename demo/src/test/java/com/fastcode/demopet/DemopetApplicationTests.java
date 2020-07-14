package com.fastcode.demopet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@EnableAutoConfiguration(excludeName = "org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration")
@SpringBootTest
public class DemopetApplicationTests {

	@Test
	public void contextLoads() {
	}

}

