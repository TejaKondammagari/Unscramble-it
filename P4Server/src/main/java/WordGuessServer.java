import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

public class WordGuessServer extends Application {

	//---------------------------------------------------------------------
	// Server elements
	private Server connection;
	private Integer portNumber;
	char name;
	int name2;
	//---------------------------------------------------------------------
	// GUI elements for login scene
	Scene loginScene;
	BorderPane loginPane;
	private TextField portTextField; // takes in the port number used for the server
	private Button turnOnServerButton; // changes the scene and creates the server instance
	private Label errorLabel; // prints error messages in case an invalid port number is given

	// GUI elements for game scene
	Scene gameScene;
	BorderPane gamePane;
	private Label portLabel; // displays the port number the user entered in the login scene
	private Label numClientsLabel; // displays the number of clients connected to server
	private ListView<String> messages; // stores event messages (server log)
	private TableView<GameInfo> clientTable; // stores all important client information
	private ObservableList<GameInfo> data; // list of GameInfo objects (where each object corresponds to a client)
	private int clientCount ; // keeps track of how many clients are connected
	private ArrayList<String> status; // stores the connection status (each index corresponds with the client's number )
	String clientNum = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(server) Playing word guess!!!");

		loginScene = createLoginScene();
		gameScene = createGameScene();
		primaryStage.setScene(loginScene);
		primaryStage.show();

		// Event handlers
		turnOnServerButton.setOnAction(e->{
			// Error-check port number - TODO -----------------------------------------------------
			portLabel.setText(portTextField.getText()); // update portLabel to display server's port

			primaryStage.setScene(gameScene);
			primaryStage.show();
		});


	}
	//---------------------------------------------------------------------
	// Server code
	@Override
	public void stop() throws Exception
	{
		connection.closeConnection();
	}

	// add list view here instead of messages
	private Server createServer() {

		return new Server(this.portNumber,
				data-> {Platform.runLater(()->{
//					clientNum+= data.toString().charAt(8);
//					System.out.println("Message from new server: + " + clientNum);

					this.messages.getItems().add(data.toString());
				});}
		);

	}


	//---------------------------------------------------------------------

	public Scene createLoginScene(){
		loginPane = new BorderPane();
		loginPane.setStyle("-fx-background-color: rgba(50,150,255,0.5)");
//		loginPane.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, bSize)));

		Label welcomeLabel = new Label("Word-Guessing");
		welcomeLabel.setPrefHeight(100);
		welcomeLabel.setFont(Font.font("Courier New", FontWeight.BOLD,32));

		Label portLabel = new Label("Enter the port number:");
		portLabel.setFont(Font.font("Courier New",24));
		portTextField = new TextField("5555");

		HBox portBox = new HBox(20, portLabel, portTextField);
		portBox.setPadding(new Insets(30));
		portBox.alignmentProperty().setValue(Pos.CENTER);

		turnOnServerButton = new Button("Turn on Server");
		turnOnServerButton.setFont(Font.font("Courier New", 20));
		errorLabel = new Label("");
		errorLabel.setFont(Font.font("Courier New", 20));
		errorLabel.setTextFill(Color.web("#ff0000",0.8));

		VBox mainBox = new VBox(20, welcomeLabel, portBox, turnOnServerButton, errorLabel);
		mainBox.setPadding(new Insets(50));
		mainBox.alignmentProperty().setValue(Pos.CENTER);

		loginPane.setCenter(mainBox);
		loginScene = new Scene(loginPane, 1200, 700);

		this.portNumber = Integer.parseInt(portTextField.getText());
		this.connection = createServer();
		try{
			this.connection.startConnection();
		}catch (Exception e)
		{
			//do nothing
		}
		return loginScene;
	}

	public Scene createGameScene(){
		// initialize list
		clientCount = 0;
		data = FXCollections.observableArrayList();

		// create a test client -----------------------------------------------[
//		for(int i = 0; i < 20; i++){
//			clientCount++;
//			data.add(new GameInfo(clientCount));
//			System.out.println(data.get(clientCount-1).getPlayerNum() + ", "
//					+ data.get(clientCount-1).getCategory() + ", "
//					+ data.get(clientCount-1).getGuessLetter());
//		}
//		clientCount++;
//		data.add(new GameInfo(clientCount));
		// --------------------------------------------------------------------]

		gamePane = new BorderPane();
		gamePane.setStyle("-fx-background-color: rgba(50,150,255,0.5)");

		// Left panel elements --------------------------------------------------------------------
		Label portTextLabel = new Label("Port: ");
		portTextLabel.setFont(Font.font("Courier New", FontWeight.BOLD,17));
		portLabel = new Label();
		portLabel.setFont(Font.font("Courier New", FontWeight.BOLD,17));
		HBox portBox = new HBox(10, portTextLabel, portLabel);
		portBox.setPadding(new Insets(10));

//		Label numClientsTextLabel = new Label("Number of clients: ");
//		numClientsTextLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
//		numClientsLabel = new Label(clientCount + "");
//		numClientsLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
//		HBox numClientsBox = new HBox(10, numClientsTextLabel, numClientsLabel);
//		numClientsBox.setPadding(new Insets(10));

		Label messageLabel = new Label("Event log:");
		messageLabel.setFont(Font.font("Courier New", FontWeight.BOLD,17));
		messageLabel.prefWidth(100);
		messageLabel.setAlignment(Pos.CENTER_LEFT);
		messages = new ListView<>();
		messages.prefHeight(600);
		messages.prefWidth(400);
		VBox messageBox = new VBox(10, messageLabel, messages);
		messageBox.setAlignment(Pos.CENTER);
		messageBox.setPadding(new Insets(10,5,10,5));
		messageBox.setMinHeight(515.0);

		VBox leftBox = new VBox(0, portBox);
		leftBox.setStyle("-fx-background-color: rgba(50,150,255,0.7)");
		gamePane.setLeft(leftBox);

		// Center panel elements ------------------------------------------------------------------
//		TableColumn<GameInfo, Integer> clientNumColumn = new TableColumn<>("Client number");
//		clientNumColumn.setMinWidth(95.0);
//		clientNumColumn.setCellValueFactory(new PropertyValueFactory<>("playerNum"));
//		TableColumn<GameInfo, Integer> currCategoryColumn = new TableColumn<>("Current Category");
//		currCategoryColumn.setMinWidth(120.0);
//		currCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("currentCategory"));
//		TableColumn<GameInfo, Character> guessingLetterColumn = new TableColumn<>("Guessing Letter");
//		guessingLetterColumn.setMinWidth(120.0);
//		guessingLetterColumn.setCellValueFactory(new PropertyValueFactory<>("guessLetter"));
//		TableColumn<GameInfo, Integer> numGuessesColumn = new TableColumn<>("Number Guesses Used");
//		numGuessesColumn.setMinWidth(140.0);
//		numGuessesColumn.setCellValueFactory(new PropertyValueFactory<>("numGuesses"));
//		TableColumn<GameInfo, Boolean> cat1SolvedColumn = new TableColumn<>("Category 1");
//		cat1SolvedColumn.setMinWidth(110.0);
//		cat1SolvedColumn.setCellValueFactory(new PropertyValueFactory<>("category1Solved"));
//		TableColumn<GameInfo, Boolean> cat2SolvedColumn = new TableColumn<>("Category 2");
//		cat2SolvedColumn.setMinWidth(110.0);
//		cat2SolvedColumn.setCellValueFactory(new PropertyValueFactory<>("category2Solved"));
//		TableColumn<GameInfo, Boolean> cat3SolvedColumn = new TableColumn<>("Category 3");
//		cat3SolvedColumn.setMinWidth(110.0);
//		cat3SolvedColumn.setCellValueFactory(new PropertyValueFactory<>("category3Solved"));

//		clientTable = new TableView<>();
//		clientTable.setItems(data);
//		clientTable.getColumns().addAll(clientNumColumn, currCategoryColumn, guessingLetterColumn, numGuessesColumn,
//				cat1SolvedColumn, cat2SolvedColumn, cat3SolvedColumn);
//		VBox tableBox = new VBox(10, clientTable);
//		tableBox.setPrefHeight(500.0);
//		tableBox.setAlignment(Pos.CENTER);
//		tableBox.setPadding(new Insets(5));
//		gamePane.setCenter(tableBox);
		VBox.setVgrow(messages, Priority.ALWAYS);
		gamePane.setCenter(messageBox);

		gameScene = new Scene(gamePane, 1200, 700);

		// ---------------------------------------------------------------[
		messages.getItems().add("Scene created");
//		clientCount++;
//		data.add(new GameInfo(clientCount));
//		numClientsLabel.setText(clientCount + "");
//		data.get(clientCount-1).setGuessLetter('B');
//		data.get(10).setGuessLetter('C');
//		data.get(10).setNumGuesses(1);
//		data.get(15).setNumGuesses(1);
//		data.get(7).setCategorySolved(2);
		// ---------------------------------------------------------------]
		return gameScene;
	}

}