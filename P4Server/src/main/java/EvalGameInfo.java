
import java.util.ArrayList;
import java.util.HashMap;

public class EvalGameInfo {


    //----------------------------------------------------------------------------------------------
    // Need function to evaluate player guess with correct word if one letter is entered
    // Need function to evaluate player guess with correct word if whole word is entered


    //----------------------------------------------------------------------------------------------

    //evaluates the players letter guess against the correct word
    public String evalLetterGuess(GameInfo play) {
        //convert correct word to char array to make evaluation
        char[] corrWord = play.getCorrectWord().toCharArray();
        //grab the players guess
        char guess = play.getGuessLetter();
        String temp = "";
        //search word to see if guess matches
        for (int i = 0; i < corrWord.length; i++) {
            //if player guessed correct letter
            if (corrWord[i] == guess) {
                //toggle isGuessCorrect to true
                play.setIsGuessCorrect();
                //update current in word to * so same letter not repeated
                corrWord[i] = '*';
                //update original string
                play.setCorrectWord(String.valueOf(corrWord));

                //update players wordSoFar with correct letter
                //play.updateWord(i);
                for (int j = 0; j < corrWord.length; j++) {
                    if(corrWord[j]!='*')
                        //return the index of correct letter

                        return "#CORRECTLETTER:" + guess; // made change here
                }
                play.setValidCategory(play.currentCategory);
                return "#CORRECTWORD";
            }
        }
        //end of word reached, letter did not match
        //increment guess count
        play.setNumGuesses(play.getNumGuesses() + 1);

        // 6 guesses are done. Warn client
        if (play.getNumGuesses() == 6) {
            play.lives--;
            return "#WRONGLETTER#OUTOFGUESSES";
        }
    return "#WRONGLETTER:"+guess;
}

    // logic to evaluate if the user guessed the correct word or not
    public String evalWholeWord(GameInfo play)
    {
        if(play.getWordGuess().equalsIgnoreCase(play.currentWord)) {
            play.setValidCategory(play.currentCategory);
            return "#CORRECTWORD";
        }
        //increment guess count
        play.setNumGuesses(play.getNumGuesses() + 1);
        if (play.getNumGuesses() == 7) {
            play.lives--;
            return "#WRONGWORD#OUTOFGUESSES";
        }
        return "#WRONGWORD";
    }





//    //evaluates the players word guess against the correct word
//    public boolean evalWordGuess(GameInfo play, String correctWord){
//        String guess = play.getWordGuess();
//        //if player guessed word correctly
//        if(guess == correctWord){
//            //set category solved to true
//            play.setIsWordSolved(true);
//            // mark the categorySolved to true
//            play.setCategorySolved(play.getCategory() - 1, true);
//            //mark the category as invalid
//            play.setValidCategory(play.getCategory()-1, false);
//            return true;
//        }
//        //else word not solved
//        //set is word solved to false
//        play.setIsWordSolved(false);
//        return false;
//    }

    //evaluate word guess will be similar to above

}
