package com.g7match.rdg7;

import com.g7match.rdg7.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class Rdg7ApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
		assertNotNull(userService);
	}

    @Test
    void mainMethodRuns() {
        Rdg7Application.main(new String[] {});
        assertTrue(true);
    }
}

