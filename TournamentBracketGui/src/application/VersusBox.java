/////////////////////////////////////////////////////////////////////////////
// Semester: CS400 Spring 2018
// PROJECT: Team Project, Milestone 3
// FILES: Main.java
// Scoreable.java
// Team.java
// TypeOfMatch.java
// VersusBox.java
// application.css
// teams.txt
//
// Authors: Zach Kremer, Ege Kula, Patrick Lacina, Nathan Kolbow, Jong Kim
// Due date: 10:00 PM on Thursday, May 3rd
// Outside sources: None
//
// Instructor: Deb Deppeler (deppeler@cs.wisc.edu)
// Bugs: No known bugs
//
//////////////////////////// 80 columns wide //////////////////////////////////

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
	// the VersusBox the winner of the match will be sent to
	private VersusBox next;

	// scores for each team
	private int teamScore1;
	private int teamScore2;

	// teams competing in a round
	private Team team1;
	private Team team2;

	// boxes for each team's Label and TextField
	private HBox team1Box;
	private HBox team2Box;

	// box for the submit button
	private HBox btnBox;

	// label for each team
	private Label team1Lbl;
	private Label team2Lbl;

	// text field to input the score of each team
	private TextField team1TxtField;
	private TextField team2TxtField;

	// button to submit the scores
	private Button submitBtn;

	// describes the round (semi-final, final, etc.)
	private TypeOfMatch matchType;

	// describes the position of the box in its pairing of two matches. Determines
	// whether the winning team will be sent to the top or bottom slot of the next
	// round.
	boolean topGame;
	
	// whether the game match has been completed and the winning team has been sent to the next round
	boolean isSent = false;

	/**
	 * The no-args constructor for VersusBox. Places appropriate GUI elements in the
	 * VersusBox, but leaves elements blank. Used for spacing.
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
			if (teamScore1 < 0 || teamScore2 < 0) {
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Constructor for VersusBox when the match type and positioning of the
	 * VersusBox is known. Used for creating VersusBox past the first round.
	 * 
	 * @param match
	 *            the round "type" (e.g. quarter-final, semi-final, etc.)
	 * @param isTop
	 *            the position of the box in its pairing of two matches. Determines
	 *            whether the winning team will be sent to the top or bottom slot of
	 *            the next round.
	 */
	public VersusBox(TypeOfMatch match, boolean isTop) {
		this();
		this.topGame = isTop;
		setMatchType(match);
	}

	/**
	 * Constructor for VersusBox when the teams playing a match are known. Used to
	 * create matches in the first round (when team match-ups are known).
	 * 
	 * @param match
	 *            the round "type" (e.g. quarter-final, semi-final, etc.)
	 * @param team1
	 *            the first team to be competing in a match (shown on top)
	 * @param team2
	 *            the second team to be competing in a match (shown on bottom)
	 * @param isTop
	 *            the position of the box in its pairing of two matches. Determines
	 *            whether the winning team will be sent to the top or bottom slot of
	 *            the next round.
	 */
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

	/**
	 * Returns the winner of a match. The winner is the team that scored more
	 * points.
	 * 
	 * @return the winning team. If there is a tie, null is returned. If there was
	 *         bad input, a badInputAlert is shown.
	 */
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

	/**
	 * Returns the loser of a match. The loser is the team that scored fewer points.
	 * 
	 * @return the losing team. If there is a tie, null is returned. If there was
	 *         bad input, a badInputAlert is shown.
	 */
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
	 * 
	 * @param box
	 *            The versus box to send the winning team to
	 */
	public void setNext(VersusBox box) {
		if (this != box)
			this.next = box;
	}

	/**
	 * Setter for team1.
	 * 
	 * @param team
	 *            the "top" team in the match
	 */
	public void setTeam1(Team team) {
		if (team != team1 && team1 != null) {
			switch (matchType) {
			case SEMI_FINAL:
				Main.setThirdPlace(null, 0, false);
				break;
			case GRAND_CHAMPIONSHIP:
				Main.setSecondPlace(null);
				Main.setFirstPlace(null);
				break;
			default:
				break;
			}
		}
		
		if (team == team1 || team1 == null) {
			// normal initial team setter
		} else if (team != team1 && next != null) {
			// if one of the teams is removed this round, it must be removed from all
			// upcoming matches, as it has no longer won this match and is no longer playing
			// in future matches.
			if (topGame)
				next.setTeam1(null);
			else
				next.setTeam2(null);
		}

		// if a team has been removed from this match all of the stats and teams get
		// reset
		if (team == null) {
			teamScore1 = 0;
			teamScore2 = 0;
			team1 = null;
			team1Lbl.setText("TBD");
			team1TxtField.setText("");

			if (next != null) {
				if (topGame)
					next.setTeam1(null);
				else
					next.setTeam2(null);
			}
		} else {
			team1 = team;
			team1Lbl.setText(team.toString());
		}
	}

	/**
	 * Setter for team2.
	 * 
	 * @param team
	 *            the "bottom" team in the match
	 */
	public void setTeam2(Team team) {
		if (team != team2 && team2 != null) {
			switch (matchType) {
			case SEMI_FINAL:
				Main.setThirdPlace(null, 0, false);
				break;
			case GRAND_CHAMPIONSHIP:
				Main.setSecondPlace(null);
				Main.setFirstPlace(null);
				break;
			default:
				break;
			}
		}

		if (team == team2 || team2 == null) {
			// normal initial team setter
		} else if (team != team2 && next != null) {
			// if one of the teams is removed this round, it must be removed from all
			// upcoming matches, as it has no longer won this match and is no longer playing
			// in future matches.
			if (topGame)
				next.setTeam1(null);
			else
				next.setTeam2(null);
		}

		// if a team has been removed from this match all of the stats and teams get
		// reset
		if (team == null) {
			teamScore1 = 0;
			teamScore2 = 0;
			team2 = null;
			team2Lbl.setText("TBD");
			team2TxtField.setText("");

			if (next != null) {
				if (topGame)
					next.setTeam1(null);
				else
					next.setTeam2(null);
			}
		} else {
			team2 = team;
			team2Lbl.setText(team.toString());
		}
	}

	/**
	 * Getter for teamScore1.
	 * 
	 * @return the number of points team1 scored
	 */
	public int getTeamScore1() {
		return teamScore1;
	}

	/**
	 * Setter for teamScore2.
	 * 
	 * @param teamScore1
	 *            the number of points team1 scored
	 */
	public void setTeamScore1(int teamScore1) {
		this.teamScore1 = teamScore1;
	}

	/**
	 * Getter for teamScore2.
	 * 
	 * @return teamScore 2 the number of points team2 scored
	 */
	public int getTeamScore2() {
		return teamScore2;
	}

	/**
	 * Setter for teamScore2.
	 * 
	 * @param teamScore2
	 *            the number of points team2 scored
	 */
	public void setTeamScore2(int teamScore2) {
		this.teamScore2 = teamScore2;
	}

	/**
	 * Getter for matchType
	 * 
	 * @return the round "type" (e.g. quarter-final, semi-final, etc.)
	 */
	public TypeOfMatch getMatchType() {
		return matchType;
	}

	/**
	 * Setter for matchType
	 * 
	 * @param matchType
	 *            the round "type" (e.g. quarter-final, semi-final, etc.)
	 */
	public void setMatchType(TypeOfMatch matchType) {
		this.matchType = matchType;
	}

	/**
	 * Handles Button and TextField action events. Sends the winning team of a match
	 * to the next round, and updates the first/second/third place labels if
	 * appropriate.
	 */
	@Override
	public void handle(ActionEvent e) {

		if (team1 != null && team2 != null && !(getWinner() == null) && isSent==false) {
			isSent = true;
			team1TxtField.setDisable(true);
			team2TxtField.setDisable(true);
			switch (matchType) {
			case GRAND_CHAMPIONSHIP:
				Main.setFirstPlace(getWinner());
				Main.setSecondPlace(getLoser());
				break;
			case SEMI_FINAL:
				Main.setThirdPlace(getLoser(), (teamScore1 > teamScore2) ? teamScore2 : teamScore1, topGame);
				sendWinner(getWinner());
				break;
			default:
				sendWinner(getWinner());
				break;
			}
		}
	}

}
