package com.atguigu.springcloud;

import com.atguigu.springcloud.entities.Payment;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentApplicationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void queryPaymentRecordTest(){
        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/payment/get/31", String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
        Integer id = documentContext.read("$.data.id");
        assertThat(id).isEqualTo(31);
        String serialNo = documentContext.read("$.data.serial");
        assertThat(serialNo).isEqualTo("a00001");
    }

    @Test
    public void savePaymentRecordTest(){
        Payment newRecord = new Payment(null, "a12345");
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/payment/create",newRecord,String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
        Integer id = documentContext.read("$.data");
        assertThat(id).isNotNull();
        assertThat(id).isGreaterThan(0);
    }

}
