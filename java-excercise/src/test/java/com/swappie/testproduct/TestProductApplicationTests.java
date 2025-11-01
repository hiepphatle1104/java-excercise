package com.swappie.testproduct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestProductApplicationTests {

    @Test
    void contextLoads() {
    }

    @SpringBootTest
    @AutoConfigureMockMvc
    class ProductControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;


        }
    }



