package com.lee.shopping.infrastracture.configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.lee.shopping.infrastracture.repository.jpa")
public class JpaConfiguration {
}
