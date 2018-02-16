package org.tomcat.docker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SecretsTestFile {

    private String secretsDirName;
    private String secretsFilename;

    public SecretsTestFile(String dir, String filename) {
        this.secretsDirName = dir;
        this.secretsFilename = filename;
    }

    public void reset(String secretContent) throws IOException {
        File secretDir = new File(secretsDirName);
        if (!secretDir.exists()) {
            secretDir.mkdirs();
        }
        File secretFile = new File(secretsDirName+"/"+secretsFilename);
        if (secretFile.exists()) {
            secretFile.delete();
        }
        Files.write(secretFile.toPath(), secretContent.getBytes());
    }

}
