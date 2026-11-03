package com.portfolio.study_management_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
@Import(TestcontainersConfiguration.class)
class StudyManagementAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
