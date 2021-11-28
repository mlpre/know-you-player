package ml.minli.api.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {

    public static void loadJarToCurrentThread(String jarPath) {
        try {
            File file = new File(jarPath);
            if (file.exists()) {
                JarFile jarFile = new JarFile(file);
                Enumeration<JarEntry> entries = jarFile.entries();
                String className = null;
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String jarName = jarEntry.getRealName();
                    if (jarName.endsWith(".class")
                            && !jarName.equals("module-info.class")
                            && !jarName.equals("package-info.class")) {
                        className = jarName;
                        break;
                    }
                }
                if (className != null) {
                    className = className.replace("/", ".").substring(0, className.length() - 6);
                }
                if (className != null) {
                    URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
                    Class.forName(className, true, urlClassLoader);
                    Thread.currentThread().setContextClassLoader(urlClassLoader);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
