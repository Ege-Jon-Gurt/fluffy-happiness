package application;

import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

/**
 * Class that contains all the GUI elements for each match in the tournament.
 * Also includes methods to get the winner of a match and send them to the next
 * round.
 * 
 * @author Zach Kremer, Nathan K
 *
 */
public class VersusBox extends VBox implements Scoreable, EventHandler<ActionEvent> {
	private VersusBox next;

	private int teamScore1;
	private int teamScore2;

	private Team team1;
	private Team team2;

	private HBox team1Box;
	private HBox team2Box;

	private HBox btnBox;

	private Label team1Lbl;
	private Label team2Lbl;

	private TextField team1TxtField;
	private TextField team2TxtField;

	private Button submitBtn;

	private TypeOfMatch matchType;
	boolean topGame;

	/**
	 * The no-args constructor for VersusBox. Places appropriate GUI elements in the
	 * VersusBox, but leaves elements blank.
	 */
	public VersusBox() {
		this.setMinHeight(100);
		this.setMaxHeight(100);
		this.setPrefHeight(100);

		team1Box = new HBox();
		team2Box = new HBox();
		btnBox = new HBox();

		team1Lbl = new Label("TBD");
		team2Lbl = new Label("TBD");

		team1TxtField = new TextField();
		team1TxtField.setOnAction(this);
		team2TxtField = new TextField();
		team2TxtField.setOnAction(this);

		submitBtn = new Button("Submit");
		submitBtn.setOnAction(this);

		team1Box.getChildren().addAll(team1Lbl, team1TxtField);
		team2Box.getChildren().addAll(team2Lbl, team2TxtField);
		btnBox.getChildren().add(submitBtn);

		this.getChildren().addAll(team1Box, team2Box, btnBox);
	}

	/**
	 * Parses the scores from the text field.
	 * 
	 * @return returns whether the method could parse the scores as ints
	 */
	public boolean parseScores() {
		try {
			teamScore1 = Integer.parseInt(team1TxtField.getText());
			teamScore2 = Integer.parseInt(team2TxtField.getText());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public VersusBox(TypeOfMatch match, boolean isTop) {
		this();
		this.topGame = isTop;
		setMatchType(match);
	}

	public VersusBox(TypeOfMatch match, Team team1, Team team2, boolean isTop) {
		this(match, isTop);
		this.team1 = team1;
		this.team2 = team2;
		team1Lbl.setText(team1.toString());
		team2Lbl.setText(team2.toString());
	}

	/**
	 * Creates an alert box if the input is invalid.
	 */
	private void badInputAlert() {
		Alert abox = new Alert(AlertType.ERROR, "Invalid input, please make sure you entered the scores correctly.");
		abox.showAndWait();
	}

	@Override
	public Team getWinner() {
		if (parseScores()) {
			// return null if it's a tie
			return (teamScore1 > teamScore2) ? team1 : ((teamScore2 > teamScore1) ? team2 : null);
		} else {
			// bad input--return null
			badInputAlert();
			return null;
		}
	}

	@Override
	public Team getLoser() {
		if (parseScores()) {
			// return null if it's a tie
			return (teamScore1 > teamScore2) ? team2 : ((teamScore2 > teamScore1) ? team1 : null);
		} else {
			// bad input--return null
			badInputAlert();
			return null;
		}
	}

	/**
	 * Sends the winning team to the next round.
	 * 
	 * @param winner
	 *            The winner of the match
	 */
	public void sendWinner(Team winner) {
		if (next == null || winner == null)
			return;

		if (topGame)
			next.setTeam1(winner);
		else
			next.setTeam2(winner);
	}

	/**
	 * Setter for next.
	 * @param box The versus box to send the winning team to
	 */
	public void setNext(VersusBox box) {
		this.next = box;
	}

	/**
	 * Setter for team1.
	 * @param team the "top" team in the match
	 */
	public void setTeam1(Team team) {
		team1 = team;
		team1Lbl.setText(team.toString());
	}

	/**
	 * Setter for team2.
	 * @param team the "bottom" team in the match
	 */
	public void setTeam2(Team team) {
		team2 = team;
		team2Lbl.setText(team.toString());
	}

	/**
	 * Getter for teamScore1.
	 * @return the number of points team1 scored
	 */
	public int getTeamScore1() {
		return teamScore1;
	}

	/**
	 * Setter for teamScore2.
	 * @param teamScore1 the number of points team1 scored
	 */
	public void setTeamScore1(int teamScore1) {
		this.teamScore1 = teamScore1;
	}

	/**
	 * Getter for teamScore2.
	 * @return teamScore 2 the number of points team2 scored
	 */
	public int getTeamScore2() {
		return teamScore2;
	}

	/**
	 * Setter for teamScore2.
	 * @param teamScore2 the number of points team2 scored
	 */
	public void setTeamScore2(int teamScore2) {
		this.teamScore2 = teamScore2;
	}

	/**
	 * Getter for matchType
	 * @return the round "type" (e.g. quarter-final, semi-final, etc.)
	 */
	public TypeOfMatch getMatchType() {
		return matchType;
	}

	/**
	 * Setter for matchType
	 * @param matchType the round "type" (e.g. quarter-final, semi-final, etc.)
	 */
	public void setMatchType(TypeOfMatch matchType) {
		this.matchType = matchType;
	}

	@Override
	public void handle(ActionEvent e) {

		switch (matchType) {
		case GRAND_CHAMPIONSHIP:
			Main.setFirstPlace(getWinner());
			Main.setSecondPlace(getLoser());
			break;
		case SEMI_FINAL:
			Main.setThirdPlace(getLoser(), (teamScore1 > teamScore2) ? teamScore2 : teamScore1);
			sendWinner(getWinner());
			break;
		default:
			sendWinner(getWinner());
			break;
		}
	}

}
