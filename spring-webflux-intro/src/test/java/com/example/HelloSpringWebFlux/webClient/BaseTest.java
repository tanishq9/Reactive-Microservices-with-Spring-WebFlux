package com.example.HelloSpringWebFlux.webClient;

import org.springframework.boot.test.context.SpringBootTest;

// this below annotation would automatically start/stop server for tests
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BaseTest {
}
