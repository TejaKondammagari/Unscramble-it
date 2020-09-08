import java.io.IOException;
import java.sql.SQLOutput;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WordGuessClient extends Application {
	private Client connection;
	private String IPAddress;
	private Integer portNumber;

	// GUI for login scene
	private Scene loginScene, gameScene, resultScene;
	private BorderPane loginPane, gamePane, resultPane;
	private TextField portTextField,ipTextField, playerGuessLetter, playerGuessWord;
	private Button connectButton, quitButton, category1, category2, category3, playAgain, leaveGame, resultBtn;
	private Button letterEnter, wordEnter, box2Cat1, box2Cat2, box2Cat3, box2changeCat;
	private VBox categoryBox, middleBox, againBox;
	private HBox categoryButton, box2CatBut;
	private ListView<String> messages; // used to display game information
	private Label currCat, currGuess;
	private int guessCount = 0;
	private int currCatNum = 0;
	String clientGuess;



	public static void main(String[] args){
		// TODO Auto-generated method stub
		launch(args);
	}

	//------------------------------------------------------------------------------------------------

	public Scene createLoginScene(){

		loginPane = new BorderPane();

		//------set the background of the Login Scene-----------------
		Image image = new Image("bgd1.jpg");
		BackgroundSize bgSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		BackgroundImage bgImage =
				new BackgroundImage(
						image,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						bgSize);
		Background background = new Background(bgImage);
		loginPane.setBackground(background);
		//------End for setting background----------------------------

		//------Contents inside the scene-----------------------------
		Label welcome = new Label("Welcome to Word Guessing Game");
		welcome.setStyle("-fx-text-fill: deepskyblue;");
		welcome.setFont(Font.font("Verdana", FontWeight.BOLD, 30));

		Label portLabelSet = new Label("Enter the port number:");
		portLabelSet.setStyle("-fx-text-fill: lightpink;");
		portLabelSet.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		portTextField = new TextField("5555");

		Label ipLabelSet = new Label("Enter the IP address:");
		ipLabelSet.setStyle("-fx-text-fill: lightpink;");
		ipLabelSet.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		ipTextField = new TextField("127.0.0.1");

		HBox portBox = new HBox(20, portLabelSet, portTextField);
		portBox.setPadding(new Insets(50));
		portBox.alignmentProperty().setValue(Pos.CENTER);

		HBox ipBox = new HBox(20, ipLabelSet, ipTextField);
		ipBox.setPadding(new Insets(50));
		ipBox.alignmentProperty().setValue(Pos.CENTER);

		VBox loginBox = new VBox(10, portBox, ipBox);

		connectButton = new Button("Connect to Server");
		connectButton.setStyle("-fx-base: gray;" + "-fx-font-size: 20;" );

		VBox mainBox = new VBox(20, welcome, loginBox, connectButton);
		mainBox.setPadding(new Insets(50));
		mainBox.alignmentProperty().setValue(Pos.CENTER);
		//-----------------End for contents inside the scene-------------

		this.IPAddress = ipTextField.getText();
		this.portNumber = Integer.valueOf(portTextField.getText());

		loginPane.setCenter(mainBox);
		loginScene = new Scene(loginPane, 1000, 700);
		return loginScene;
	}

	//------------------------------------------------------------------------------------------------

	public Scene createGameScene() {
		gamePane = new BorderPane();
		this.connection = createClient();
		try {
			this.connection.startConnection();
		}
		catch (Exception e)
		{
			System.out.println("Unable to create a client");
		}

		//--------------------set the background of the Game Scene-----------------
		Image image = new Image("bgd2.jpg");
		BackgroundSize bgSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		BackgroundImage bgImage =
				new BackgroundImage(
						image,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						bgSize);
		Background background = new Background(bgImage);
		gamePane.setBackground(background);
		//--------------End for setting background----------------------------

		//----------contents inside the scene-------------------------

		//---------------------------------Left side---------------------------------
		Label port = new Label("Port: " + portTextField.getText());
		port.setStyle("-fx-text-fill: snow");
		port.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Label ip = new Label("IP: " + ipTextField.getText());
		ip.setStyle("-fx-text-fill: snow");
		ip.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		messages = new ListView<>();
		messages.setStyle("-fx-control-inner-background: lightblue;");
		messages.getItems().add("Player successfully connect to the Server!");

		VBox leftBox = new VBox(20,port, ip, messages);
		leftBox.setAlignment(Pos.TOP_CENTER);
		gamePane.setLeft(leftBox);


		//----------------------------------Middle--------------------------------
		Label titleGame = new Label("Word Guessing Game");
		titleGame.setUnderline(true);
		titleGame.setStyle("-fx-text-fill: lightpink");
		titleGame.setFont(Font.font("Verdana", FontWeight.BOLD, 25));

		HBox titleBox = new HBox(5, titleGame);
		titleBox.setAlignment(Pos.TOP_CENTER);
		gamePane.setTop(titleBox);

		category1 = new Button("Category 1");
		category1.setStyle("-fx-base: lightpink;" + "-fx-font-size:15");
		category2 = new Button("Category 2");
		category2.setStyle("-fx-base: lightpink;" + "-fx-font-size:15");
		category3 = new Button("Category 3");
		category3.setStyle("-fx-base: lightpink;" + "-fx-font-size:15");

		categoryButton = new HBox(15, category1, category2, category3);
		categoryButton.setAlignment(Pos.CENTER);

		middleBox = new VBox(20, categoryButton);
		middleBox.setAlignment(Pos.CENTER);
		gamePane.setCenter(middleBox);

		// Middle box that will show up one the player has chosen a category
		currCat = new Label();
		currCat.setStyle("-fx-text-fill: royalblue");
		currCat.setFont(Font.font("Verdana", FontWeight.BOLD, 25));


		currGuess = new Label("Number Incorrec Guesses: " + guessCount);
		currGuess.setStyle("-fx-text-fill: royalblue");
		currGuess.setFont(Font.font("Verdana", FontWeight.BOLD, 25));


		VBox playStatus = new VBox(10,currCat, currGuess);
		playStatus.setAlignment(Pos.CENTER);
		currCat.setText("Category 0");

		Label guessLetter = new Label("Enter Guess Letter: ");
		guessLetter.setStyle("-fx-text-fill: snow");
		guessLetter.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		playerGuessLetter = new TextField();
		letterEnter = new Button("Enter");
		letterEnter.setOnAction(e->{
				currGuess.setText("Number Incorrect Guesses: " + guessCount);
//				messages.getItems().add("You guess Letter " + playerGuessLetter.getText());

				clientGuess = playerGuessLetter.getText().toUpperCase();
				try {
					// SEND MESSAGE TO SERVER
					connection.send(playerGuessLetter.getText().toUpperCase());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				playerGuessLetter.clear();

//			else{
//				messages.getItems().add("You used all your guesses for letter");
//				messages.getItems().add("You have to Guess word now!");
//			}
		});



		Label guessWord = new Label("Enter Guess Word: ");
		guessWord.setStyle("-fx-text-fill: snow");
		guessWord.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		playerGuessWord = new TextField();
		wordEnter = new Button("Enter");
		wordEnter.setOnAction(e->{
			playerGuessLetter.clear();
			messages.getItems().add("You guessed a word "+playerGuessWord.getText());
			try {
				connection.send(playerGuessWord.getText().toUpperCase());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		box2Cat1 = new Button("Category1");
		box2Cat1.setStyle("-fx-base: lightpink;" + "-fx-font-size:15");
		box2Cat2 = new Button("Category2");
		box2Cat2.setStyle("-fx-base: lightpink;" + "-fx-font-size:15");
		box2Cat3 = new Button("Category3");
		box2Cat3.setStyle("-fx-base: lightpink;" + "-fx-font-size:15");
		box2changeCat = new Button("Change Category");
		box2changeCat.setStyle("-fx-base: lightpink;" + "-fx-font-size:15");

		box2CatBut = new HBox(15, box2Cat1, box2Cat2, box2Cat3, box2changeCat);
		box2CatBut.setAlignment(Pos.CENTER);

		HBox gLetterBox = new HBox(10,guessLetter, playerGuessLetter, letterEnter);
		gLetterBox.setAlignment(Pos.CENTER);
		HBox gWordBox = new HBox(10, guessWord, playerGuessWord, wordEnter);
		gWordBox.setAlignment(Pos.CENTER);

		categoryBox = new VBox(30, playStatus, box2CatBut, gLetterBox, gWordBox);
		categoryBox.setAlignment(Pos.CENTER);


		//-------------------------Right Side----------------------------------------
		Label ruleTitle = new Label("Game Rule");
		ruleTitle.setUnderline(true);
		ruleTitle.setStyle("-fx-text-fill: white");
		ruleTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Label rules = new Label("1. Pick 1 category to start\n" +"2. Player must" +
				" guess 3 different\n   words in 3 different categories\n   to win\n" +
				"3. If player doesn't guess" +
				" the word\n    correctly, then player can pick\n    other categories or choose another \n    word" +
				"from the same category" +
				"\n4. If player guess the word correctly,\n    then player have to pick\n    other categories" +
				"\n5. Player can have total 6 incorrect\n    guesses per word" + "\n6. If the player guesses the letter or" +
				"\n    the letter incorrectly, that guess will \n    count as one wrong guess");
		rules.setStyle("-fx-text-fill: white");
		rules.setFont(Font.font("Verdana", FontWeight.BOLD, 11));

		quitButton = new Button("Quit");
		quitButton.setStyle("-fx-base: lightskyblue;" + "-fx-font-size: 20;");

		// Also need to add disconnected to the server !!!!
		quitButton.setOnAction(e->{
			System.exit(0);
		});

		resultBtn = new Button("Result Page");
		resultBtn.setStyle("-fx-base: lightskyblue;" + "-fx-font-size: 20;");
		resultBtn.setOnAction(e->{

		});

		VBox rightBox = new VBox(10, ruleTitle, rules, quitButton,resultBtn);
		rightBox.setAlignment(Pos.TOP_CENTER);

		gamePane.setRight(rightBox);


		//--------------------------------- End for contents---------------------------------
		gameScene = new Scene(gamePane, 1000, 700);
		return gameScene;
	}
	//------------------------------------------------------------------------------------------------
	public Scene createResultScene(int result){
		resultPane = new BorderPane();

		//------------------------------------Background------------------------------------------
		Image image = new Image("bgd3.jpg");
		BackgroundSize bgSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		BackgroundImage bgImage =
				new BackgroundImage(
						image,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						bgSize);
		Background background = new Background(bgImage);
		resultPane.setBackground(background);
		//--------------------------------------Contents------------------------------------------
		Label gameResult = new Label("");
		gameResult.setStyle("-fx-text-fill: orange");
		gameResult.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		// If result = 1, then Player Win
		if(result == 1){
			gameResult.setText("You Win");
		}
		// If result = 0, then Player Lose
		else{
			gameResult.setText("You Lose");
		}

		playAgain = new Button("Play Again");
		playAgain.setStyle("-fx-base: hotpink;" + "-fx-font-size: 20;");

		leaveGame = new Button("Leave Game");
		leaveGame.setStyle("-fx-base: hotpink;" + "-fx-font-size: 20;" );
		leaveGame.setOnAction(e->{
			System.exit(0);
		});


		HBox resultButton = new HBox(20,playAgain,leaveGame);
		resultButton.setAlignment(Pos.CENTER);
		VBox resultPage = new VBox(40, gameResult, resultButton);
		resultPage.setAlignment(Pos.CENTER);
		//-----------------------------------End---------------------------------------
		resultPane.setCenter(resultPage);

		resultScene = new Scene(resultPane, 1000,700);
		return  resultScene;
	}

	//------------------------------------------------------------------------------------------------

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception{
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Client) Word Guess!!!");

		loginScene = createLoginScene();
		gameScene = createGameScene();
		primaryStage.setScene(loginScene);
		primaryStage.show();


		connectButton.setOnAction(e->{
			primaryStage.setScene(gameScene);
			primaryStage.show();
		});




		category1.setOnAction(e->{
			try {
				connection.send("#CATEGORY1");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			messages.getItems().add("Player pick category 1");

			category2.setDisable(true);
			category3.setDisable(true);
			box2Cat1.setDisable(true);
			box2Cat2.setDisable(true);
			box2Cat3.setDisable(true);
			currCatNum = 1;

			currCat.setText("Category "+ currCatNum);
			gamePane.setCenter(categoryBox);
		});

		category2.setOnAction(e->{
			try {
				connection.send("#CATEGORY2");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			messages.getItems().add("Player pick category 2");
			category1.setDisable(true);
			category3.setDisable(true);
			box2Cat1.setDisable(true);
			box2Cat2.setDisable(true);
			box2Cat3.setDisable(true);
			currCatNum = 2;

			currCat.setText("Category "+ currCatNum);
			gamePane.setCenter(categoryBox);
		});


		category3.setOnAction(e->{
			try {
				connection.send("#CATEGORY3");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			messages.getItems().add("Player pick category 3");
			category1.setDisable(true);
			category2.setDisable(true);
			box2Cat1.setDisable(true);
			box2Cat2.setDisable(true);
			box2Cat3.setDisable(true);
			currCatNum = 3;

			currCat.setText("Category "+ currCatNum);
			gamePane.setCenter(categoryBox);
		});

		box2Cat1.setOnAction(e->{
			try {
				connection.send("#CATEGORY1");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			messages.getItems().add("Player pick category 1");
			box2Cat1.setDisable(true);
			box2Cat2.setDisable(true);
			box2Cat3.setDisable(true);
			playerGuessLetter.setDisable(false);
			playerGuessWord.setDisable(false);
			letterEnter.setDisable(false);
			wordEnter.setDisable(false);
			currCatNum = 1;

			currCat.setText("Category "+ currCatNum);
			gamePane.setCenter(categoryBox);
		});

		box2Cat2.setOnAction(e->{
			try {
				connection.send("#CATEGORY2");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			messages.getItems().add("Player pick category 2");
			box2Cat1.setDisable(true);
			box2Cat2.setDisable(true);
			box2Cat3.setDisable(true);
			playerGuessLetter.setDisable(false);
			playerGuessWord.setDisable(false);
			letterEnter.setDisable(false);
			wordEnter.setDisable(false);
			currCatNum = 2;

			currCat.setText("Category "+ currCatNum);
			gamePane.setCenter(categoryBox);
		});

		box2Cat3.setOnAction(e->{
			try {
				connection.send("#CATEGORY3");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			messages.getItems().add("Player pick category 3");
			box2Cat1.setDisable(true);
			box2Cat2.setDisable(true);
			box2Cat3.setDisable(true);
			playerGuessLetter.setDisable(false);
			playerGuessWord.setDisable(false);
			letterEnter.setDisable(false);
			wordEnter.setDisable(false);
			currCatNum = 3;

			currCat.setText("Category "+ currCatNum);
			gamePane.setCenter(categoryBox);
		});

		box2changeCat.setOnAction(e->{
			messages.getItems().add("Player Decide to change category");
			if(currCatNum == 1){
				box2Cat1.setDisable(false); // made change here
				box2Cat2.setDisable(false);
				box2Cat3.setDisable(false);
			}
			else if(currCatNum == 2 ){
				box2Cat1.setDisable(false);
				box2Cat3.setDisable(false);
			}
			else if(currCatNum == 3){
				box2Cat1.setDisable(false);
				box2Cat2.setDisable(false);
			}
			playerGuessLetter.setDisable(true);
			playerGuessWord.setDisable(true);
			letterEnter.setDisable(true);
			wordEnter.setDisable(true);

		});
//		playAgain.setOnAction(e->{
//			primaryStage.setScene(gameScene);
//			primaryStage.show();
//		});

		resultBtn.setOnAction(e->{
			primaryStage.setScene(createResultScene(0));
			primaryStage.show();
		});


	}
	private Client createClient()
	{
		return new Client(IPAddress, portNumber,
				info -> {
					Platform.runLater(()->{
						if (info.toString().contains( "#WRONGLETTER"))
						{
							messages.getItems().add(info.toString());
							guessCount++;
							currGuess.setText("Number Incorrect Guesses: " + guessCount);
							messages.getItems().add("You have " + (6 - guessCount) + " guesses left");
						}
						if (info.toString().contains("#WRONGLETTER#OUTOFGUESSES"))
						{
							guessCount = 0;
							currGuess.setText("Number Incorrect Guesses: " + guessCount);
							//messages.getItems().add(info.toString());
							messages.getItems().add("Please press change category to choose another category");
							// we need to take client back to category page
							//disable letter and word guess buttons
							letterEnter.setDisable(true);
							wordEnter.setDisable(true);

						}

						if (info.toString().contains("#LOSTGAME"))
						{
							messages.getItems().add("You lost the game, good bye");
							messages.getItems().add("Please press result page");
						}
						if (info.toString().contains("#CORRECTLETTER"))
						{
							messages.getItems().add("You guessed the correct letter");
							System.out.println("From client: " + info.toString() +  clientGuess ); // can you hear me?one sec no
							messages.getItems().add(info.toString());
							messages.getItems().add("You have " + (6 - guessCount) + " guesses left");
						}

						if (info.toString().contains("Word length"))
						{
							messages.getItems().add(info.toString());

						}

					});}
		);
	}
}