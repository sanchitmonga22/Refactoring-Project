package util;

import model.Score;
import java.util.*;
import java.io.*;

/**
 *	This class represents the database for storing, retrieving and updating the data in the Score file
 */
public class ScoreHistoryFile {

	private static String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

	/**
	 * This function is used to add scores of all the users into the database
	 * @param nick Nickname of the user
	 * @param date Current date
	 * @param score Score of the user
	 * @throws IOException	This exception is thrown when there occurs an error when writing into the file
	 */
	public static void addScore(String nick, String date, String score) throws IOException {
		String data = nick + "\t" + date + "\t" + score + "\n";

		RandomAccessFile out = new RandomAccessFile(SCOREHISTORY_DAT, "rw");
		out.skipBytes((int) out.length());
		out.writeBytes(data);
		out.close();
	}

	/**
	 * This function is used to retrieve data from the database
	 * @param nick Nickname of the bowler
	 * @return The list of all the previous scores
	 * @throws IOException 	This exception is thrown when there occurs an error when writing into the file
	 */
	public static ArrayList<Score> getScores(String nick) throws IOException {
		ArrayList<Score> scores = new ArrayList<>();

		BufferedReader in = new BufferedReader(new FileReader(SCOREHISTORY_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is "nick"+'\t'+"fname"+'\t'+"e-mail"
			String[] scoredata = data.split("\t");
			//"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
			if (nick.equals(scoredata[0])) {
				scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
			}
		}
		return scores;
	}
}
