package io.fdlessard.codebites.batch;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerJobController {

  private JobLauncher jobLauncher;

  private Job job;

  public CustomerJobController(JobLauncher jobLauncher, Job job) {
    this.jobLauncher = jobLauncher;
    this.job = job;
  }

  @RequestMapping("/startJob")
  public BatchStatus load() throws Exception {

    Map<String, JobParameter> parameters = new HashMap<>();
    parameters.put("time", new JobParameter(System.currentTimeMillis()));
    JobParameters jobParameters = new JobParameters(parameters);

    JobExecution jobExecution = jobLauncher.run(job, jobParameters);
    while ((jobExecution.isRunning())) {
      System.out.println("...");
    }

    return jobExecution.getStatus();
  }
}
