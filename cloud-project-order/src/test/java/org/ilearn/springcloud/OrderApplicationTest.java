package org.ilearn.springcloud;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderApplicationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void createTest(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:"+port+"/consumer/payment/create?serial=c00001", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext context = JsonPath.parse(responseEntity.getBody());
        int id = context.read("$.data");
        assertThat(id).isGreaterThan(0);
    }

    @Test
    public void queryTest(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:"+port+"/consumer/payment/get/32", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext context = JsonPath.parse(responseEntity.getBody());
        int id = context.read("$.data.id");
        assertThat(id).isEqualTo(32);

        String serialNo = context.read("$.data.serial");
        assertThat(serialNo).isNotEmpty();
    }
}
