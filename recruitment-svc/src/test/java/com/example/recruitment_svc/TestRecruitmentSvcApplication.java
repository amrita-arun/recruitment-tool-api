package com.example.recruitment_svc;

import org.springframework.boot.SpringApplication;

public class TestRecruitmentSvcApplication {

	public static void main(String[] args) {
		SpringApplication.from(RecruitmentSvcApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
