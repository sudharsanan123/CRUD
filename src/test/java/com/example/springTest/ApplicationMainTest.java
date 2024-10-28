package com.example.springTest;

import com.example.spring.ApplicationMain; // Import your main application class
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ApplicationMain.class) // Specify the main application class
public class ApplicationMainTest {

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully.
    }

    @Test
    void main() {
        ApplicationMain.main(new String[] {}); // Call the main method
    }
}
