import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GuessTest {

	EvalGameInfo eval;
	GameInfo info;
	Server server;

	@BeforeEach
	void init() {
		eval = new EvalGameInfo();
		info = new GameInfo(1);

	}

	@Test
	void testClass(){
		assertEquals("EvalGameInfo", eval.getClass().getName(), "Incorrect class");
	}

	@Test
	void testInfoClass(){
		assertEquals("GameInfo", info.getClass().getName(), "incorrect class name");
	}

	@Test
	void testServerClass(){
		assertEquals("", info.currentWord, "inocrrect class name");
	}

	//test initial category is -1
	@Test
	void testInitCat(){
		assertEquals(-1, info.currentCategory, "incorrect default val");
	}

	//test initial lives
	@Test
	void testInitLives(){
		assertEquals(3, info.lives, "incorrect default value");
	}

	@Test
	void testNumGuesses(){
		assertEquals(0, info.getNumGuesses(), "incorrect default val");
	}

	@Test
	void testSendingCategory(){
		assertEquals(false, info.getSendingCategory(), "incorrect default val");
	}
	@Test
	void testSendingLetter(){
		assertEquals(false, info.getSendingLetter(), "incorrect default val");
	}
	@Test
	void testSendingWord(){
		assertEquals(false, info.getSendingWord(), "incorrect default val");
	}

	@Test
	void testCorrectWord(){
		assertEquals("", info.getCorrectWord(), "incorrect default value");
	}
}
