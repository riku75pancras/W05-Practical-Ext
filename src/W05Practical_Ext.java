import java.io.IOException;

public class W05Practical_Ext {
    //minimal main method which creates an instance of SimilarityScores and calls a method which
    //prints the similarity score by passing on the filepath and the query string in the parameter
    public static void main(String[] args){
        //try catch block which catches exceptions thrown in the printSimilarityScore method
        try {

            //if statement to prevent the user from inputting more than 2 arguments (if the user
            //accidentally puts the whole sentence without the quotation mark, the first word in the sentence
            //is only checked)
            if (args.length == 2){
                SimilarityScores similarityScore = new SimilarityScores();
                similarityScore.printSimilarityScore(args[0], args[1]);
            }
            else{
                System.out.println("The query string is not set or is not bracketed in quotation marks. Try again");
            }

            //prints the stacktrace if there is no input file
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
