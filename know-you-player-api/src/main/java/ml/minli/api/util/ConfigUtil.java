package ml.minli.api.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConfigUtil {

    private static final Logger log = LogManager.getLogger(ConfigUtil.class.getName());

    public static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + ".know-you-player";

    public static final String CONFIG_FILE = CONFIG_PATH + File.separator + "know-you-player.properties";

    public static Properties properties = new Properties();

    public static void initConfig() throws Exception {
        File file = new File(CONFIG_PATH);
        boolean isExist;
        if (!file.exists()) {
            isExist = file.mkdir();
        } else {
            isExist = true;
        }
        boolean configIsExist;
        if (isExist) {
            File config = new File(CONFIG_FILE);
            if (!config.exists()) {
                configIsExist = config.createNewFile();
            } else {
                configIsExist = true;
            }
        } else {
            throw new Exception("directory init failure...");
        }
        if (!configIsExist) {
            throw new Exception("config init failure...");
        }
        ScheduledExecutorService scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        properties.load(new FileReader(CONFIG_FILE));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                store();
            } catch (Exception e) {
                log.error("Save Config Error", e);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public static void store() throws Exception {
        properties.store(new FileOutputStream(CONFIG_FILE), null);
    }

}
