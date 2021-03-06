package com.ninjadevops.tower.ws;

import com.ninjadevops.tower.model.DBConnection;
import com.ninjadevops.tower.model.JobConfig;
import com.ninjadevops.tower.model.runtime.JobInstance;
import com.ninjadevops.tower.service.ConfigService;
import com.ninjadevops.tower.service.JobExecutor;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by me@liguoliang.com on 3/5/2017.
 */
@Component
@Path("/status")
public class StatusEndpoint {
    private ConfigService configService;
    private JobExecutor jobExecutor;

    public StatusEndpoint(ConfigService configService, JobExecutor jobExecutor) {
        System.out.println(configService);
        this.configService = configService;
        this.jobExecutor = jobExecutor;
    }

    @GET
    public String getStatus() {
        String result = "";
        try {
            DBConnection dbConnection = configService.getDBConnectionById("db-sit2");

            result += dbConnection.getConnectionString();
            result += jobExecutor.execute(new JobInstance(JobConfig.newInstance("sql-job1", "SELECT 'SQL Tower'"), dbConnection)).getValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "DevOps Tower is running: " + result;
    }

}
