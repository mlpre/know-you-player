/*
 * Copyright 2021 Minli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.minli.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Minli
 */
public class ConfigUtil {

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
            throw new Exception("目录创建失败");
        }
        if (!configIsExist) {
            throw new Exception("配置创建失败");
        }
        ScheduledExecutorService scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        properties.load(new FileReader(CONFIG_FILE));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                store();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    public static void store() throws Exception {
        properties.store(new FileOutputStream(CONFIG_FILE), null);
    }

}
