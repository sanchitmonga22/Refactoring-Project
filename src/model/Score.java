package model;

public class Score {

    private String nick;
    private String date;
    private String score;

	/**Score()
	 * Constructor for a score
	 * @param nick nickname of the bowler
	 * @param date date of the game
	 * @param score score of the game
	 */
    public Score( String nick, String date, String score ) {
		this.nick=nick;
		this.date=date;
		this.score=score;
    }

	/** getDate()
	 * @return the date of the game
	 */
	public String getDate() {
		return date;
	}

	/** getScore()
	 * @return the score of the game
	 */
	public String getScore() {
		return score;
	}

	/** toString()
	 * @return a string to display all of the score information
	 */
	public String toString() {
		return nick + "\t" + date + "\t" + score;
	}

}
