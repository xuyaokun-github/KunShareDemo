package cn.com.kun;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class KunShareDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KunShareDemoApplication.class, args);
	}

}
