package com.shareData.chainMarket.scan;

import com.shareData.chainMarket.classLoader.AppClassLoader;
import com.shareData.chainMarket.constant.Config;
import com.shareData.chainMarket.def.ResConfig;
import com.shareData.chainMarket.i.Central;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ScanControl {
    Map<String, String> myEntity = ResConfig.get().getMyEntity();
    private static final String CLASS_SUFFIX = ".class";
    ArrayList<String> listFileName = new ArrayList<>();

    public void start(String pack) throws IOException, ClassNotFoundException {
        String packageDirName = pack.replace('.', '/');
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        boolean showChildPackageFlag = true;
        while (dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            String protocol = url.getProtocol();
            if ("jar".equals(protocol)) {
                JarFile jarFile = null;
                try {
                    jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (jarFile != null) {
                    List<String> re = getAllClassNameByJar(jarFile, pack, showChildPackageFlag, listFileName);
                    okByJar(re);
                }
            } else {
                ok(url.getPath(), listFileName);
            }
        }
    }

    private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag,
                                                     List<String> result) {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if (name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if (flag) {
                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok
                    if (name.startsWith(packageName) && -1 == name.indexOf("$")) {
                        result.add(name);
                    }
                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if (packageName.equals(name.substring(0, name.lastIndexOf("."))) && -1 == name.indexOf("$")) {
                        result.add(name);
                    }
                }
            }
        }
        return result;
    }

    public void okByJar(List<String> list) throws ClassNotFoundException {
        for (String ur : list) {
            Class<?> myClass = Class.forName(ur);
            boolean bc = myClass.isAnnotationPresent(Central.class);
            if (bc) {
                Central central = myClass.getAnnotation(Central.class);
                String url = central.url();
                myEntity.put(url, ur);
            }
        }
    }

    public void ok(String path, ArrayList<String> listFileName) throws IOException {
        getAllFileName(path + "/", listFileName);
        for (String name : listFileName) {
            File f = new File(name);
            if (!f.isDirectory()) {
                InputStream is = new FileInputStream(f);
                byte[] br = new byte[is.available()];
                int t = 0;
                int b = 0;
                while ((t = is.read()) > -1) {
                    br[b] = (byte) t;
                    b++;
                }
                String ur = Config.getRootUrl();
                if (name.indexOf("/") < 0) {
                    name = name.replace("\\", ".");
                } else {
                    name = name.replace("/", ".");
                }
                int a = name.indexOf(ur);
                name = name.substring(a, name.length() - 6);
                System.out.println("name==" + name);
                Class<?> ma = AppClassLoader.getInstance().findClassByBytes(name, br);
                boolean bc = ma.isAnnotationPresent(Central.class);
                if (bc) {
                    Central central = ma.getAnnotation(Central.class);
                    String url = central.url();
                    myEntity.put(url, name);
                }
            }
        }
    }

    public void getAllFileName(String path, ArrayList<String> listFileName) {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null) {
            String[] completNames = new String[names.length];
            for (int i = 0; i < names.length; i++) {
                completNames[i] = path + names[i];
            }
            listFileName.addAll(Arrays.asList(completNames));
        }
        for (File a : files) {//files在JAR包下是空的
            if (a.isDirectory()) {//如果文件夹下有子文件夹，获取子文件夹下的所有文件全路径。
                getAllFileName(a.getAbsolutePath() + "\\", listFileName);
            }
        }
    }
}
