/*
 * Copyright 2020 Minli
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
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Minli
 */
public class ResourceUtil {

    private static final String jnaPath = System.getProperty("user.home") + File.separator + ".vlcj";

    static {
        try {
            File jnaLib = new File(jnaPath);
            if (!jnaLib.exists()) {
                ZipInputStream zipInputStream = new ZipInputStream(ResourceUtil.getInputStream("vlc/win64.zip"));
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    File file = new File(jnaPath + File.separator + zipEntry.getName());
                    if (zipEntry.isDirectory()) {
                        file.mkdirs();
                    } else {
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(zipInputStream.readAllBytes());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.setProperty("jna.library.path", jnaPath);
    }

    public static URL getResource(String resource) {
        return ResourceUtil.class.getClassLoader().getResource(resource);
    }

    public static String getExternalForm(String resource) {
        return ResourceUtil.getResource(resource).toExternalForm();
    }

    public static InputStream getInputStream(String resource) {
        return ResourceUtil.class.getClassLoader().getResourceAsStream(resource);
    }

}
