package org.tomcat.docker;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class SecretsDataSourceFactoryTests {

    private static final String SECRET_PATH = "SECRET_PATH";
    private static final String SECRET_PATH_VALUE = "target/dockerSecretsTests";
    private static final String SECRET_NAME = "testSecret";
    private static final String SECRET_VALUE = "testSecretValue";


    @Before
    public void setupSecretsFile() throws IOException {
        System.getProperties().put(SECRET_PATH, SECRET_PATH_VALUE);
        System.getProperties().put(SECRET_NAME, SECRET_NAME);
        SecretsTestFile secretFile = new SecretsTestFile(SECRET_PATH_VALUE, SECRET_NAME);
        secretFile.reset(SECRET_VALUE);
    }

    public void getObjectInstanceTest() {
        //TODO: not sure how to test the object instance yet.
    }

    @Test
    public void getSecretValueByEnvNameTest() throws IOException {
        SecretsDataSourceFactory factory = new SecretsDataSourceFactory();
        assertTrue(SECRET_VALUE.equals(factory.getSecretValueByEnvName(SECRET_NAME)));

    }

    @Test
    public void getPropertyOrEnvironmentValueTest() {
        SecretsDataSourceFactory factory = new SecretsDataSourceFactory();
        assertTrue(SECRET_NAME.equals(factory.getPropertyOrEnvironmentValue(SECRET_NAME)));
    }

    @Test
    public void secretValueExistsTest() {
        SecretsDataSourceFactory factory = new SecretsDataSourceFactory();
        assertTrue(factory.secretExists(SECRET_NAME));
    }

    @Test
    public void readSecretValueTest() throws IOException {
        SecretsDataSourceFactory factory = new SecretsDataSourceFactory();
        assertTrue(SECRET_VALUE.equals(factory.readSecretValue(SECRET_NAME)));
    }

}
