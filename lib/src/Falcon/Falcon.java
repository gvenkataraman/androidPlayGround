package Falcon;

import com.sun.tools.javac.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Retrieval class for autocomplete suggestions
 * and instant results
 */
public class Falcon {
    private PatriciaTrie<String, String> inputStreamTrie;
    private static final Logger logger = Logger.getLogger(Falcon.class.getName());
    /**
     * Take the input stream and store it in internal data structure
     * assumes input file is csv
     * query_term, value
     * @param inputStream the input stream to save data
     */
    public Falcon(InputStream inputStream) throws IOException {
        inputStreamTrie = new PatriciaTrie<String, String>(new CharSequenceKeyAnalyzer());

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        logger.info("loading file ...");
        long startTime = System.currentTimeMillis();
        while ((line = reader.readLine()) != null) {
            String[] words = line.split(",");
            // TODO: check on error in input file
            inputStreamTrie.put(words[0], words[1]);
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
    public List<String> retrieve(String queryTerm) {
        Map<String, String> itemsFromTrie = inputStreamTrie.getPrefixedBy(queryTerm);
        List<String> matches = new ArrayList<String>();
        for (Map.Entry<String, String> stringStringEntry : itemsFromTrie.entrySet()) {
            matches.add(stringStringEntry.getValue());
        }
        return matches;
    }
}
