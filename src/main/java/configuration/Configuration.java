package configuration;

import jakarta.inject.Singleton;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Properties;

@Singleton
@Log4j2
public class Configuration {

    private Properties p;

    public Configuration() {
        try {
            p = new Properties();
            p.load(Configuration.class.getClassLoader().getResourceAsStream("configs/configuration.properties"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getProperty(String key) {
        return p.getProperty(key);
    }
}
