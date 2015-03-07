package test;

import Falcon.Falcon;
import junit.framework.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class FalconTest {

    private static String TMP_FILE = "/tmp/serialize_falcon.txt";

    @Test
    public void testWriteOutput() throws Exception {
        Falcon falcon = new Falcon();
        FileOutputStream fout = new FileOutputStream(TMP_FILE);
        Map<String, Object> objectMap = new HashMap<String, Object>();
        ComplexTest complex = new ComplexTest(1, 0);
        objectMap.put("complex", complex);
        falcon.writeOutput(fout, objectMap);
        fout.close();

        FileInputStream fin = new FileInputStream(TMP_FILE);
        ObjectInputStream objectInputStream = new ObjectInputStream(fin);
        Map<String, Object> deserializedObject = ((Map<String, Object>) objectInputStream.readObject());
        for (Map.Entry<String, Object> stringObjectEntry : deserializedObject.entrySet()) {
            Assert.assertEquals("complex", stringObjectEntry.getKey());
            ComplexTest complexTest = (ComplexTest) stringObjectEntry.getValue();
            Assert.assertEquals(1, complexTest.getReal());
            Assert.assertEquals(0, complexTest.getIm());
        }
    }

    @Test
    public void testTrie() throws IOException, ClassNotFoundException {
        Falcon falcon = new Falcon();
        FileOutputStream fout = new FileOutputStream(TMP_FILE);
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("95129", "san jose");
        objectMap.put("52246", "iowa city");
        objectMap.put("95054", "santa clara");
        objectMap.put("77840", "college station");
        objectMap.put("78705", "austin");
        falcon.writeOutput(fout, objectMap);
        fout.close();

        FileInputStream fin = new FileInputStream(TMP_FILE);
        falcon.read(fin);
        List<Object> retrievedQueries = falcon.retrieve("95");
        Assert.assertEquals(retrievedQueries.size(), 2);
        Set<String> cityNames = new HashSet<String>();
        for (Object retrievedQuery : retrievedQueries) {
            String cityName = (String) retrievedQuery;
            cityNames.add(cityName);
        }
        Assert.assertTrue(cityNames.contains("san jose"));
        Assert.assertTrue(cityNames.contains("santa clara"));

        retrievedQueries = falcon.retrieve("5");
        Assert.assertEquals(retrievedQueries.size(), 1);
        cityNames.clear();
        for (Object retrievedQuery : retrievedQueries) {
            String cityName = (String) retrievedQuery;
            cityNames.add(cityName);
        }
        Assert.assertTrue(cityNames.contains("iowa city"));
    }
}
