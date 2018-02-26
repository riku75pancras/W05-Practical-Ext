
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SentenceReader {
    private String eachCharacter;

    /**
     * Given a file path, this method will read the entire contents of the file,
     * split the text into sentences, and return a list of sentences.
     * <p>
     * The sentence splitting is very basic: we just split on the full-stop character.
     * <p>
     * The contents of each sentence are sanitised as well.
     * Sanitisation is done by replacing each character with the corresponding lower case character,
     * removing all punctuation characters, etc.
     *
     * @param filepath The file path for the input file
     * @return A list of all sentences in the file
     * @throws IOException May throw an IOException while reading the file
     */
    public void readAllSentences(String filepath, ArrayList<String> sentencesList) throws IOException {
        String sentence = "";

        //use scanner to read the sentences
        Scanner scanner = new Scanner(new File(filepath));

        //by using the delimter(""), scanner reads every character of the line
        scanner.useDelimiter("");

        //while loop to keep reading the file if there are lines after
        while (scanner.hasNext()) {
            //scans the next character and assigns it to the attribute
            eachCharacter = scanner.next();

            //if statement which checks if the character is a full stop, which means that it is the end of a sentence
            //in this case, the sentence which has concatenated each character is added to sentenceList and
            //sentence is assigned to an empty string
            if (eachCharacter.equals(".")) {

                //before the sentence is added to the list, it calls sanitiseSentence() method to sanitise the sentence
                sentence = sanitiseSentence(sentence);
                sentencesList.add(sentence);
                sentence = "";
            } else {
                //this is where the concatenation of eachCharacter to sentence is carried out
                sentence = sentence.concat(eachCharacter);
            }
        }

        //close scanner
        scanner.close();

    }


    /**
     * Given a string, this method will sanitise it.
     * Sanitisation is done by replacing each character with the corresponding lower case character,
     * removing all punctuation characters, etc.
     *
     * @param sentence The input string
     * @return The output string
     */
    public String sanitiseSentence(String sentence) {
        List<String> words = new ArrayList<>();
        for (String word: sentence.split("\\s+")) {
            String sanitised = word.toLowerCase().replaceAll("[^a-z]+", "");
            if (!sanitised.isEmpty()) {
                words.add(sanitised);
            }
        }

        return joinWords(words);
    }

    /**
     * This is a private method to join a list of words into a sentence.
     *
     * @param words The list of words
     * @return A string which contains the words separated by spaces
     */
    private String joinWords(List<String> words) {
        String joined = "";
        if (words.size() > 0) {
            joined = words.get(0);
        }
        for (int i = 1; i < words.size(); i++) {
            joined += " " + words.get(i);
        }
        return joined;
    }

}
