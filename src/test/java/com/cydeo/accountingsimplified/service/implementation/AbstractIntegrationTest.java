package com.cydeo.accountingsimplified.service.implementation;

import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles({"test"})
@Testcontainers
@Sql(scripts = "classpath:drop-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AbstractIntegrationTest {
    /*
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("cacoon")
            .withUsername("postgres")
            .withPassword("admin");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

*/
    private static final  PostgreSQLContainer postgre;

    static {
        postgre = new PostgreSQLContainer(DockerImageName.parse("postgres:11.1"));
        postgre.start();
    }


    /**
     * Ensure all 3 persistence units get the same connection to the same docker container
     *
     * @param registry
     */
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgre::getJdbcUrl);
        registry.add("spring.datasource.username", postgre::getUsername);
        registry.add("spring.datasource.password", postgre::getPassword);

    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void teardown() {


//
//        TODO, cannot get entity manager to work
//        var em = factory.createEntityManager();
//        // manually wipe down data as often data is populated async and in uncontrollable transaction so cannot simply rollback
//        em.createQuery("DELETE FROM BankAccount");
//        em.createQuery("DELETE FROM BankAccountBalance");
//        em.createQuery("DELETE FROM Transaction");

    }
}
