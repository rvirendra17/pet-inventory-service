package com.abc.xyz.PetStore;

import com.abc.xyz.PetStore.controllers.PetController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
* Context Startup Test - This a test to verify context (application context) startup/bootstrapping.
*  this ensures that the API/service has no conflicts in context and can be bootstrapped without issues.
* */

/*
* All the other tests under test/java folder for various packagee
* tests to verify business requirements/user stories/functional requirements/features
* */

/*
 * The @SpringBootTest annotation tells Spring Boot to look for a main configuration class (one with @SpringBootApplication, for instance)
 * and use that to start a Spring application context.
 *
 * To convince yourself that the context is creating your controller, you could add an assertion as below.
 * */

@SpringBootTest
class PetStoreApplicationTests {
    @Autowired
    PetController petController;

    // A simple sanity check test that will fail if the application context cannot start.

    @Test
    void contextLoads() {

        Assertions.assertNotNull(petController);
    }

}
