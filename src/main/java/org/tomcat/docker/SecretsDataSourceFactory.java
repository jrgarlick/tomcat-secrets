package org.tomcat.docker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.Name;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory;

public class SecretsDataSourceFactory extends BasicDataSourceFactory {


    private static final Logger LOG = Logger.getLogger(SecretsDataSourceFactory.class.getCanonicalName());

    private String SECRET_PATH = "/run/secrets";

    public SecretsDataSourceFactory() {
        SECRET_PATH = System.getProperty("SECRET_PATH", SECRET_PATH);
    }

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
        Object o = super.getObjectInstance(obj, name, nameCtx, environment);
        BasicDataSource ds = null;
        if (o != null) {
            ds = (BasicDataSource) o;
            ds.setUrl(getSecretValueByEnvName(ds.getUrl()));
            ds.setUsername(getSecretValueByEnvName(ds.getUsername()));
            ds.setPassword(getSecretValueByEnvName(ds.getPassword()));
        }

        return ds;
    }

    String getSecretValueByEnvName(String name) throws IOException {
        String secretValue = name;
        if (name != null && name.length() > 0) {
            String secretName = getPropertyOrEnvironmentValue(name);
            if (secretExists(secretName)) {
                secretValue = readSecretValue(secretName);
                LOG.log(Level.INFO, "Secret named '"+secretName+"' found.");
            } else {
                LOG.log(Level.INFO, "Secret named '"+secretName+"' not found");
            }
        }
        return secretValue;
    }

    String getPropertyOrEnvironmentValue(String key) {
        String value = System.getProperty(key, System.getenv(key));
        LOG.log(Level.INFO, "Found env/prop: '"+key+"' = '"+value+"'");
        return value;
    }

    boolean secretExists(String secretName) {
        File secretFile = new File(SECRET_PATH + "/" + secretName);
        boolean exists = secretFile.exists();
        LOG.log(Level.INFO, "Looking for secret store named '"+secretFile.getAbsolutePath()+"' exists?: "+exists);
        return exists;
    }

    String readSecretValue(String secretName) throws IOException {
        List<String> lines = Files.readAllLines(new File(SECRET_PATH + "/" + secretName).toPath());
        return lines.stream().findFirst().get();
    }
}
