package com.rds.gdpr.patterns;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = {UserRepository.class})
public class UserRepositoryTest {

    private final Faker faker = new Faker();

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void save() {
        User results = userRepository.save(User.builder()
                .name(faker.name().username())
                .email(faker.internet().emailAddress())
                .privateKey(faker.random().hex(100))
                .publicKey(faker.random().hex(20))
                .build());
        Assertions.assertThat(results).isNotNull();
    }

}