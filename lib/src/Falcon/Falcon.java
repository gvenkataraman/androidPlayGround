package Falcon;

import com.sun.tools.javac.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Retrieval class for autocomplete suggestions
 * and instant results
 */
public class Falcon {
    private Map<String, Object> inputObjects;
    private PatriciaTrie<String, Object> inputStreamTrie;
    private static final Logger logger = Logger.getLogger(Falcon.class.getName());

    /**
     * Dumb ctor for now
     */
    public Falcon() {

    }

    /**
     * Write query and object to output stream
     * one line at a time, so the object and query term are written one line at a time
     * @param outputStream output stream
     */
    public void writeOutput(OutputStream outputStream, Map<String, Object> queryObjectMap) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(queryObjectMap);
    }
    /**
     * Take the input stream and store it in internal data structure
     * assumes input file is csv
     * query_term, value
     * @param inputStream the input stream to save data
     */
    public void read(InputStream inputStream) throws IOException, ClassNotFoundException {
        if (inputObjects == null) {
            inputObjects = new HashMap<String, Object>();
        }
        inputObjects.clear();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        long startTime = System.currentTimeMillis();
        logger.info("loading file ...");
        inputObjects = ((Map<String, Object>) objectInputStream.readObject());
        inputStreamTrie = new PatriciaTrie<String, Object>(new CharSequenceKeyAnalyzer());
        for (Map.Entry<String, Object> stringObjectEntry : inputObjects.entrySet()) {
            inputStreamTrie.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        long endTime = System.currentTimeMillis();
        long totalTimeInMs = endTime - startTime;
        logger.info("Total time taken to load data: " + totalTimeInMs + " ms");
        logger.info("Finished loading file");
    }

    /**
     * Look up the trie and retrieve the items
     * @param queryTerm input query term
     * @return list of items retrieved
     * TODO: modify this to return based on ranking if ranking is present
     * should it be returning a dictionary after deserializing?
     */
    public List<Object> retrieve(String queryTerm) {
        Map<String, Object> itemsFromTrie = inputStreamTrie.getPrefixedBy(queryTerm);
        List<Object> matches = new ArrayList<Object>();
        for (Map.Entry<String, Object> stringObjectEntry : itemsFromTrie.entrySet()) {
            matches.add(stringObjectEntry.getValue());
        }
        return matches;
    }
}
