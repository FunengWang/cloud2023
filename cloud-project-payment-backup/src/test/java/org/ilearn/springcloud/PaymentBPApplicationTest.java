package org.ilearn.springcloud;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.ilearn.springcloud.entities.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentBPApplicationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void queryPaymentRecordTest(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/payment/get/31", String.class);
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
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/payment/create",newRecord,String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());
        Integer id = documentContext.read("$.data");
        assertThat(id).isGreaterThan(0);
    }

}
