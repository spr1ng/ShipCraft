/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package shipcraft.ai;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import shipcraft.utils.Utils;

/**
 * @version $Id: AIManagerImplJar.java 76 2010-06-23 02:05:43Z spr1ng $
 * @author max
 */

public class AIManagerImplJar implements AIManager  {

    private String path; //Path where to search AI jar files

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getAIClassNames() {

        List<String> classNames = new ArrayList<String>();
        File p = new File(path);

        if (path.isEmpty() || !p.exists()) {
            Utils.log.warning("Path to AI folder is not set");
            return null;
        }
        FilenameFilter f = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar");
            }
        };

        List<URL> urls = new ArrayList<URL>();

        String[] jars = p.list(f);
        for (int i = 0; i < jars.length; i++) {
            try {
                URI jf = URI.create(jars[i]);
                urls.add(jf.toURL());

            } catch (IOException ex) {
               Utils.log.warning("Cannot load jar file " +jars[i] + ", reason: " + ex.getMessage());
            }
        }
        URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[0]));
        
        return classNames;
    }

     protected void searchAiInstances(JarEntry entry, List<String> instances, URLClassLoader testLoader) {
        // Enumeration<JarEntry> entries = entry.entries();
      //   while(entries.hasMoreElements()) {
      //      JarEntry je = entries.nextElement();
           // if(je.isDirectory()) {
          //      searchAiInstances(, instances, testLoader);
          //  }
       //  }
     }
}
