package com.rs.ErrorService;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
public class ErrorServiceApplication {

	private static final String TOPIC = "error";

	public static void main(String[] args) {
		SpringApplication.run(ErrorServiceApplication.class, args);
	}

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private ErrorMessageListener listener;

	@PostConstruct
	public void subscribeToTopic() {
		RTopic subscribeTopic = redissonClient.getTopic(TOPIC);
		subscribeTopic.addListener(RuntimeException.class, listener);
	}
}
