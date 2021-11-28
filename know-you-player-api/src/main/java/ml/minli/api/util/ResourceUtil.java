package ml.minli.api.util;

import cn.hutool.core.io.FileUtil;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ResourceUtil {

    private static final Logger log = LogManager.getLogger(ResourceUtil.class.getName());

    public static final String downloadPath = System.getProperty("user.home") + File.separator + "Music";

    private static String jnaPath = System.getProperty("user.home") + File.separator + ".vlcj";

    static {
        try {
            String installDir = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\VideoLAN\\VLC", "InstallDir");
            if (installDir != null && !installDir.isBlank()) {
                File file = new File(installDir);
                if (file.exists() && file.isDirectory()) {
                    jnaPath = installDir;
                }
            }
            FileUtil.mkdir(downloadPath);
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
            log.error("VLC Init Error", e);
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
