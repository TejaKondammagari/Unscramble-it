import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GameInfo {
    private int playerNum; //holds the players num so server can differentiate clients
    private int numGuesses; //number of incorrect guesses made by player
    public int currentCategory; //the category of the current round chosen by player
    private char guessLetter; //holds the letter when player attempts to guess one letter
    private String guessWord; //holds the word when player attempts to guess entire word
    private String correctWord; //holds the word which the player has to guess
    public String currentWord; //holds the word which the player has to guess
    private char[] wordSoFar; //holds the word guessed so far
    private ArrayList<HashSet<String>> playedWords; //tracks words played in each category with 3 hashsets in the arraylist
    public List<Boolean> validCategory; //array tracks if the category is valid for player to choose from
    private int[] categoryWordsCount; //array tracks how many words chosen from each category
    private boolean[] categorySolved; //array tracks if a word has correctly been guessed in each category
    private boolean sendingCategory; //true when client is sending/receiving info about the category
    private boolean sendingLetter; //true when client is sending/receiving info about the letter guess
    private boolean sendingWord; //true when client is sending/receiving info about the word guess
    private boolean isGuessCorrect;  //true when players guess was correct

    public ArrayList <String> category1; // contains category 1 word to be guessed
    public int category1Counter; // counter to see which word in the array is client guessing
    public ArrayList <String> category2; // contains word
    public int category2Counter;
    public ArrayList <String> category3;
    public int category3Counter;

    //----------
    //returns the wordSoFar as a string
    public String getWordSoFar(){ return new String(wordSoFar); }

    //-------

    public int lives;
    //constructor that takes an int which determines the players number
    GameInfo(int playerNum){
        this.playerNum = playerNum;
        numGuesses = 0;
        currentCategory = -1;
        guessWord = "";
        correctWord = "";
        currentWord = "";
        playedWords = new ArrayList<>();
        validCategory = new ArrayList<>();
        validCategory.add(true);//defaults to true
        validCategory.add(true);//defaults to true
        validCategory.add(true);//defaults to true
        categoryWordsCount = new int[3]; //defaults to 0
        categorySolved = new boolean[3]; //defaults to false
        sendingCategory = false;
        sendingLetter = false;
        sendingWord = false;
        lives = 3;

        // initializing the array lists
        category1 = new ArrayList<>();
        category1.add("LEXUS");
        category1.add("PORSCHE");
        category1.add("KOENIGSEGG");
        category1Counter = -1;

        category2 = new ArrayList<>();
        category2.add("GIRAFFE");
        category2.add("RHINOCEROS");
        category2.add("ZEBRA");
        category2Counter = -1;

        category3 = new ArrayList<>();
        category3.add("HALLENBECK");
        category3.add("MOBASHERI");
        category3.add("DEITZ");
        category3Counter = -1;

        // need to initialize the categories solved to an array of boolean values

    }

    //sets player num
    public void setPlayerNum(int playerNum){
        this.playerNum = playerNum;
    }

    //returns player num
    public int getPlayerNum(){
        return this.playerNum;
    }

    //sets numGuesses
    public void setNumGuesses(int numGuesses){ this.numGuesses = numGuesses; }

    //gets num guesses made
    public int getNumGuesses(){
        return this.numGuesses;
    }

    //sets current category
    public void setCategory(int category){
        this.currentCategory = category;
    }

    //gets current category
    public int getCategory(){
        return this.currentCategory;
    }

    //set players guess character
    public void setGuessLetter(char guess){ this.guessLetter = guess; }

    //get players guess character
    public char getGuessLetter(){ return this.guessLetter; }

    //sets the players word guess
    public void setWordGuess(String word){ this.guessWord = word; }

    //gets the players word guess
    public String getWordGuess() { return this.guessWord; }

    //sets the players correct word to be guessed
    public void setCorrectWord(String word){ this.correctWord = word; }

    //gets the players correct word to be guessed
    public String getCorrectWord() { return this.correctWord; }

    //toggles the category to valid/invalid for the given index
    public void setValidCategory(int i){ validCategory.set(i, false); }

    //gets the validity of the category of a given index
    public boolean getValidCategory(int i ) { return validCategory.get(i); }

    //sets the category word count to the given value at the given index
    public void setCategoryCount(int index, int value){ categoryWordsCount[index] = value; }

    //gets the category word count from the given index
    public int getCategoryCount(int index){ return categoryWordsCount[index]; }

    //toggle the category to solved/unsolved for the given index
    public void setCategorySolved(int i) { categorySolved[i] = !categorySolved[i]; }

    //used by server to set the initial wordSoFar with blank characters with the length of the word
    public void initializeWordSoFar(int size) {
        wordSoFar = new char[size];
        for(int i = 0; i < size; i++){
            wordSoFar[i] = '_';
        }
    }

    //used by server to update word with the correct letter at given index if player guessed correctly
    public void updateWord(int i){ wordSoFar[i] = guessLetter; }

    //toggles sending category
    public void setSendingCategory(){ sendingCategory = !sendingCategory; }

    //returns sending category
    public boolean getSendingCategory() { return sendingCategory; }

    //toggles sending word
    public void setSendingWord(){ sendingWord = !sendingWord; }

    //gets sending word
    public boolean getSendingWord() { return sendingWord; }

    //toggles sending letter
    public void setSendingLetter() { sendingLetter = !sendingLetter; }

    //gets sending letter
    public boolean getSendingLetter() { return sendingLetter; }

    //toggles correct guess
    public void setIsGuessCorrect() { isGuessCorrect = !isGuessCorrect; }

    //gets is guess correct
    public boolean getIsGuessCorrect() { return isGuessCorrect; }


    //----------------------------game logic----------------------------
    //client chooses category, presses button to send to server
    //if category is invalid (check valid category array for chosen category) then show alert to choose dif category
    //else
    //increment categoryWordCount array for appropriate category, set validCategory to false if count is 3
    //send info object to server
    //server chooses word, checks that its not been played already (search in hashset), then adds it to hashset
    //server initializes the word so far char arr to length of chosen word
    //server updates its own arraylist with current word
    //sends info object to client
    //-----------guessing begins------------
    //client will guess a letter, update object and send to server
    //if letter is correct, server sets correctGuess to true, updates servers word
    //updates clients word so far
    //if incorrect server increments num guesses and sends message on update clients
    //client receives updated info object, if guessCorrect is false,
    //      if numGuesses >= 6 disable letter guess, only word guess enabled
    //if true, let client guess again and reset it to false
    //if client guesses the word ->
    //server receives word guess, if correct, update guessedWordCorrect array
    //client receives object
    // if guessedWordCorrect is false still, player guessed wrong,
    //      if categoryWordsCount is 3, game over
    //      else  let client choose new category
    //if player guessed word correctly,
    //      if player guessed word correct in all 3 categories, game over player wins
    //      else let client choose next category


}
