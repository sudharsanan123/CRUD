package com.example.springTest.controller;

import com.example.spring.ApplicationMain; //  main application class
import com.example.spring.controller.HelloController; //  HelloController
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ApplicationMain.class) //  main application class
public class HelloControllerTest {

    @Autowired
    private HelloController helloController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(helloController).build();
    }

    @Test
    void testGreet() throws Exception {
        // Perform a GET request to the "/" endpoint
        mockMvc.perform(get("/"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().string(org.hamcrest.Matchers.startsWith("Welcome "))); // Expect response to start with "Welcome "
    }
}

