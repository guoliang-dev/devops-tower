package com.ninjadevops.tower.ws;

import com.ninjadevops.tower.model.runtime.JobInstance;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.assertThat;

/**
 * Created by me@liguoliang.com on 3/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebServiceIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getStatus() throws Exception {
        String wsStatus = restTemplate.getForObject("http://localhost:" + port + "/status", String.class);

        assertThat(wsStatus, CoreMatchers.containsString("DevOps Tower is running"));
    }

    @Test
    public void testSqlExec() throws Exception {
        String url = "http://localhost:" + port + "/sql-exec?conn=db-sit2&sql-job-id=get-env-name";
        String resultString = restTemplate.getForObject(url, String.class);

        assertThat(resultString, CoreMatchers.containsString("SIT"));
    }

    @Test
    public void testSqlExecPost() throws Exception {
        String randomString = UUID.randomUUID().toString();
        String url = "http://localhost:" + port + "/sql-exec";
        String input = "{\"env\":\"db-sit2\",\"query\":\"select '%s' \"}";
        input = String.format(input, randomString);

        String resultString = restTemplate.postForObject(url, input, String.class);
        assertThat(resultString, CoreMatchers.containsString(randomString));

    }
}