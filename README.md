# Tomcat Secrets Project

Docker provides a mechanism called 'Secrets' for sending encrypted pieces of information to your 
containers. The secrets are managed at the Docker cluster level and can be administered by 
the cluster administrators. They can be used to store pretty much any arbitrary piece of information,
but typically they're things like database passwords and SSL certificates. This project contains
Tomcat-specific objects that can be used to configure Tomcat with Secrets. To learn more about 
Docker secrets, refer to the Docker website.

## Tomcat Secrets Datasource Factory

This object lets you use Docker secrets to configure your JDBC connections when it's deployed in a 
Docker container. You can specify the connection URL and the JDBC username and password using 
Secret files. They names of the secret files are passed in by specifying the name of command line 
parameters in the `server.xml` file you place in your Tomcat's conf/ directory. It was done
this way so that you could send any number of secret values to your configuration.

In order to use the Secrets Data Source factory, you need to set the factory class to be 
`org.tomcat.docker.SecretsDataSourceFactory`.

The Docker Secrets documentation says that secret files get automatically mounted in the /run/secrets 
directory in your container. The library will prepend the secret file directory to the secret name
you specify in the environment variables. You can change the prefix by specifying an alternative
`SECRET_PATH` environment variable which will override the default.

Below is an example Resource definition that passes in the username and password values as secrets.

```
      <Resource auth="Container"
          driverClassName="oracle.jdbc.OracleDriver"
          url="jdbc:oracle:thin:@oracledb:1521:DBNAME"
          username="DB_USERNAME_SECRET"
          password="DB_PASSWORD_SECRET"
          name="jdbc/jndiname"
          maxActive="10"
          maxIdle="5"
          maxWait="10000"
          factory="org.tomcat.docker.SecretsDataSourceFactory"
          type="javax.sql.DataSource" />
```

The names of the secrets are passed in by defining environment variables in your container's startup
command. You can also specify those values in your docker-compose.yml file.

```
> docker run ... -e DB_USERNAME_SECRET='db_user_secret' -e DB_PASSWORD_SECRET='db_password_secret' ...
```

In order for the Factory to execute, you'll need to place the factory Jar into your Tomcat directory
when you are creating your docker image:

```
# Add Secrets JDBC Factory to Tomcat
ADD target/tomcat-secrets*.jar /usr/local/tomcat/lib/
# Add customized server.xml to tomcat
ADD server.xml /usr/local/tomcat/conf
```

### Other Secrets Ideas

* Pass SSL Certificates into Tomcat through secrets
* Others?
