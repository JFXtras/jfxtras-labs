package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DataUtilities {

    /**
     * Returns a map of attribute name as key and the attribute value as the map value.
     * This version is better than the string array version as it doesn't require a list of attribute names
     * that must be alphabetically ordered.
     * 
     * @param aNode
     * @param nodeName
     * @return
     */
        public static Map<String, String> getAttributes(Node aNode, String nodeName) {
            Map<String, String> attributeMap = new HashMap<String, String>();
            NamedNodeMap attributesList = aNode.getAttributes();
            if (attributesList != null) {
                for (int a = 0; a < attributesList.getLength(); a++) {
                    String key = attributesList.item(a).getNodeName();
                    String value = attributesList.item(a).getNodeValue();
            //        System.out.println(key + " " + value);
                    attributeMap.put(key, value);
                }
            }
            return attributeMap;
        }
        
        /** 
         * Gets a value from a map, then converts it into a list of integers
         * value must be a space-delimited string
         * 
         * @param map
         * @param key
         * @param errorString
         * @return
         */
        public static List<Integer> myGetList(Map<String,String> map, String key, String errorString) {
            String keysString = myGet(map, key, errorString);
            String[] keysStringArray = keysString.split(" ");
//            return Arrays.stream(keysStringArray) // may work not tested
//                .map(a -> Integer.parseInt(a))
//                .collect(Collectors.toList());
            return makeList(keysString, errorString);
        }
        
        /**
         *  replacement for Map get that returns blank string instead of null if key is not found.
         * 
         * @param map
         * @param key
         * @param errorMessage
         * @return
         */
        public static <V> V myGet(Map<String,V> map, String key, String errorMessage) {
            V value = map.get(key);
            if (value == null) {
                throw new InvalidParameterException("Missing attribute: " + key + ". " + errorMessage);
//                InvalidParameterException e = new InvalidParameterException("Missing attribute: " + key + ". " + errorMessage);
//                Main.log.log(Level.WARNING, "Missing data field: ", e);
//                return (V) "";
            }
            return value;
        }
        
        /**
         * Converts a list of integers in a space-delimited string into a List<Integer>
         * TODO - REPLACE WITH STREAM
         * @param integerString
         * @param errorString
         * @return
         */
        public static List<Integer> makeList(String integerString, String errorString) {
            List<Integer> stringList = new ArrayList<Integer>();
            if (! integerString.equals("")) {
                String[] keysStringArray = integerString.split(" ");
                for (int n=0; n < keysStringArray.length; n++) {
                    stringList.add(Integer.parseInt(keysStringArray[n])); }
            }
            return stringList;
        }
        
        /**
         * Convert Java8 Time LocalDate and LocalTime to strings using formatters from Settings
         * 
         * @param myDate
         * @return
         */
        public static String myFormatLocalDate(LocalDate myDate) {
            return (myDate != null) ? myDate.format(Settings.DATE_FORMAT1) : "";
        }
        public static String myFormatLocalTime(LocalTime myTime) {
            return (myTime != null) ? myTime.format(Settings.TIME_FORMAT_AGENDA) : "";
        }
        public static String myFormatLocalDateTime(LocalDateTime myTime) {
            return (myTime != null) ? myTime.format(Settings.DATE_FORMAT_AGENDA) : "";
        }
        
        
        /**
         * Writes a org.w3c.dom.Document to a output file.
         * 
         * @param doc
         * @param file
         * @throws TransformerException
         */
        public static void writeDocument(Document doc, Path file) throws TransformerException {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            if (Settings.PRETTY_XML) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            }
            
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file.toFile());
            transformer.transform(source, result);
        }
        
        
        public static Integer myParseInt(String value)
        {
            if (value == null || value.equals("") || value.equals("null"))
                return -1;  // -1 causes empty cells
            else 
                return Integer.valueOf(value);
        }
        
        private static LocalDate myParseLocalDate(String s, DateTimeFormatter f)
        {
            try {
                return LocalDate.parse(s, f);
            } catch (Exception e) {
                return null;
            }
        }
        public static LocalDate myParseLocalDate(String s)
        {
            return myParseLocalDate(s, Settings.DATE_FORMAT1);
        }
        
        public static LocalTime myParseLocalTime(String s, DateTimeFormatter f)
        {
            try {
                return LocalTime.parse(s, f);
            } catch (Exception e) {
                return null;
            }
        }
        
        /**
         * Gets a value from a map, then converts it into a set of LocalDate
         * value must be a space-delimited string
         * 
         * @param map
         * @param key
         * @param errorString
         * @param dateFormat
         * @return
         */
        public static Set<LocalDate> myGetSet(Map<String,String> map, String key, String errorString, DateTimeFormatter dateFormat) {
            String localDateListString = myGet(map, key, errorString);
            return makeSet(localDateListString, errorString, dateFormat);
        }

        /**
         * Converts a set of integers in a space-delimited string into a Set<Integer>
         * 
         * @param localDateListString
         * @param errorString
         * @return
         */
        public static Set<LocalDate> makeSet(String localDateListString, String errorString, DateTimeFormatter dateFormat) {
            Set<LocalDate> set = new HashSet<LocalDate>();
            if (! localDateListString.equals("")) {
                String[] listStringArray = localDateListString.split(" ");
                for (int n=0; n < listStringArray.length; n++) {
                    set.add(LocalDate.parse(listStringArray[n], dateFormat));
                }
            }
            return set;
        }
        
        /**
         * Checks a nextKey value (ie nextStyleKey) to insure it is larger than the largest key value
         * from its data map.  If so then it returns nextKey.  If not, it returns a value one larger
         * than the largest key.
         * 
         * @param nextKey
         * @param keySet
         * @param variableName
         * @return
         */
        public static Integer checkNextKey(Integer nextKey, Set<Integer> keySet, String variableName) {
            Integer maxKey=0;
            for (Integer myKey: keySet) {
                maxKey = Math.max(myKey, maxKey);
            }
            if (nextKey <= maxKey) {
                maxKey++;
//                Main.log.log(Level.WARNING, variableName + " is lower than highest key.  " 
//                        + variableName + "=" + nextKey + ", Highest key=" + maxKey
//                        + " Changing " + variableName + " to " + ++maxKey);
                return maxKey;
            } else {
                return nextKey;
            }
        }
        
        public static Boolean myParseBoolean(String value) {
            if (value == null || value.equals(""))
                return false;
            else
                return Boolean.valueOf(value);
        }
        
        public static String myInt2String(Integer value)
        {
            if (value == null)
                return "null";
            else
                return Integer.toString(value);
        }
        
        /**
         * List directory contents for a resource folder. Not recursive.
         * This is basically a brute-force implementation.
         * Works for regular files and also JARs.
         * 
         * @author Greg Briggs
         * @param clazz Any java class that lives in the same place as the resources you want.
         * @param path Should end with "/", but not start with one.
         * @return Just the name of each member item, not the full paths.
         * @throws URISyntaxException 
         * @throws IOException 
         */
        public static String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
            URL dirURL = clazz.getClassLoader().getResource(path);
            if (dirURL != null && dirURL.getProtocol().equals("file")) {
              /* A file path: easy enough */
              return new File(dirURL.toURI()).list();
            } 

            if (dirURL == null) {
              /* 
               * In case of a jar file, we can't actually find a directory.
               * Have to assume the same jar as clazz.
               */
              String me = clazz.getName().replace(".", "/")+".class";
              dirURL = clazz.getClassLoader().getResource(me);
            }
            
            if (dirURL.getProtocol().equals("jar")) {
              /* A JAR path */
              String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
              JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
              Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
              Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
              while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                  String entry = name.substring(path.length());
                  int checkSubdir = entry.indexOf("/");
                  if (checkSubdir >= 0) {
                    // if it is a subdirectory, we just return the directory name
                    entry = entry.substring(0, checkSubdir);
                  }
                  result.add(entry);
                }
              }
              return result.toArray(new String[result.size()]);
            } 
              
            throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
        }
        
}
