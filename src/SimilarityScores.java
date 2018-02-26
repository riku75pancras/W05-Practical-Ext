import java.io.IOException;
import java.util.*;

public class SimilarityScores {

    //creating several arraylists which are used for reading and storing the sentences in the input file,
    //then separating them into bigrams as well as separating the query string itself into bigrams
    ArrayList<String> sentencesList = new ArrayList<>();
    ArrayList<String> NgramListForString = new ArrayList<>();

    //the arraylist for bigram when storing sentences contains an arraylist inside as we need to store the
    //bigrams knowing which sentence they belong to
    ArrayList<ArrayList<String>> NgramListForSentence = new ArrayList<>();

    //arraylist containing instances of ScoredResult which contains the score and the sentence itself
    ArrayList<ScoredResult<String>> resultAndScoreList = new ArrayList<>();

    //private attributes which are used in the methods as well as initialised to avoid the use of magic constants
    private int lineCounter;
    private String sanitisedQueryWord;
    private int topPrintSize = 50;
    private int gramSize = 3;

    //method called from the main method which first reads each sentence in the input file and
    //calls another method which creates a bigram list then creates a bigram list for the query string too
    //lastly passes on the 3 lists by calling storeSimilarity score to calculate the similarity score and store them
    public void printSimilarityScore(String filepath, String queryString)throws IOException{
        SentenceReader sentenceReader = new SentenceReader();
        sentenceReader.readAllSentences(filepath, sentencesList);
        createNgramsForSentence(sentencesList, NgramListForSentence);
        sanitisedQueryWord = sentenceReader.sanitiseSentence(queryString);
        createNgramsForQuery(sanitisedQueryWord);
        storeSimilarityScore(sentencesList, NgramListForString, NgramListForSentence);

    }

    //creates a bigram arraylist for the sentence using two for loops. It obtains the bigram by using substring
    private void createNgramsForSentence(List<String> sentencesList, ArrayList<ArrayList<String>> bigramListForSentence) {
        for (String s : sentencesList) {
            bigramListForSentence.add(new ArrayList<>());
            if (s != "") {
                for (int j = 0; j < (s.length() - (gramSize -1)); j++) {
                    bigramListForSentence.get(lineCounter).add(s.substring(j, j + gramSize));
                }
            }
            lineCounter += 1;
        }
    }

    //creates a bigram arraylist for the query string using a similar approach as for the sentence
    private void createNgramsForQuery(String queryString) {
        for (int i = 0; i < (queryString.length() - (gramSize - 1)); i++) {
            NgramListForString.add(queryString.substring(i, i + gramSize));
        }
    }

    //important method which uses the three arraylists passed through the parameter
    private void storeSimilarityScore(ArrayList<String> sentencesList, ArrayList<String> bigramListForWord,
                                      ArrayList<ArrayList<String>> NgramListForSentence) {

        //the first loop which checks each sentence
        for (int i = 0; i < NgramListForSentence.size(); i++) {

            int sizeOfUnion;
            int sizeOfIntersection = 0;

            //create a union set which we add both bigram lists
            //as hashset does not allow duplicate values, by measuring the size of the unionSet
            //the size of union can be obtained
            Set<String> unionSet = new HashSet<>();

            unionSet.addAll(NgramListForSentence.get(i));
            unionSet.addAll(bigramListForWord);
            sizeOfUnion = unionSet.size();

            //another hashset which we add the bigram list for sentence which is used later
            Set<String> intersectionSet = new HashSet<>();
            intersectionSet.addAll(NgramListForSentence.get(i));

            //create another hashset which we add the bigramlist for word which is needed because the
            //query string may include duplicates of bigrams. If this is not considered, the program could
            //double count the intersection
            Set<String> queryStringBigramSet = new HashSet<>();
            queryStringBigramSet.addAll(bigramListForWord);

            //by using the for loop to reveal queryStringBigramSet and if the bigram inside the set is also in
            //the intersectionSet, then add 1 to the sizeOFIntersection
            for (String s : queryStringBigramSet) {
                if (intersectionSet.contains(s)) {
                    sizeOfIntersection += 1;
                }
            }

            //if statement to avoid calculating similarityScore if there is no intersection
            if (sizeOfIntersection != 0) {

                //calculating the similarityScore (Jaccard Index) and convert the JacCardIndex into double as
                //sizeOfIntersection and sizeOfUnion is int type
                double similarityScore = (double) sizeOfIntersection / sizeOfUnion;

                //create an instance of ScoredResult which passes the sentence and the similarityScore
                //in the constructor
                ScoredResult<String> scoredResult = new ScoredResult<>(sentencesList.get(i), similarityScore);

                //then add the instances to the resultAndScoreList which will be used later
                resultAndScoreList.add(scoredResult);
            }
        }

        //once the program runs through the loop (all sentences are checked), the resultAndScoreList is sorted into
        //scoredResult with highest similarityScore
        Collections.sort(resultAndScoreList);

        //for loop which prints the top 50 (magic constant) sentences wih the higherst similarityScores to 4 decimal
        //places
        for (int j = 0; j < topPrintSize; j++) {
            String similarityScore4dp = String.format("%.4f", resultAndScoreList.get(j).getScore());
            System.out.println(similarityScore4dp + " " + resultAndScoreList.get(j).getResult());
        }
    }

}