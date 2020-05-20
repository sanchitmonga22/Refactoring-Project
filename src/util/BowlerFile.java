package util;

import model.Bowler;
import java.util.*;
import java.io.*;

/**
 * Class used to retrieve and add the bowler information to and from the Database
 */
public class BowlerFile {

	/** The location of the bowler database */
	private static String BOWLER_DAT = "BOWLERS.DAT";

    /**
     * Retrieves bowler information from the database and returns a Bowler object with populated fields
     * @param nickName	the nickName of the bowler to retrieve
     * @return a Bowler object
	 * @throws IOException It will throw exception if there is nothing in the buffer
     */
	public static Bowler getBowlerInfo(String nickName) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is "nick"+'\t'+"fname"+'\t'+"e-mail"
			String[] bowler = data.split("\t");
			if (nickName.equals(bowler[0])) {
				System.out.println("Nick: " + bowler[0] +
						" Full: " + bowler[1]
						+ " email: " + bowler[2]);
				return (new Bowler(bowler[0], bowler[1], bowler[2])); // a new Bowler object with nickName, fullName and email
			}
		}
		System.out.println("Nick not found...");
		return null;
	}

    /**
     * Adds information of the bowler into the database
     * @param nickName	the NickName of the Bowler
     * @param fullName	the FullName of the Bowler
     * @param email	the E-mail Address of the Bowler
	 * @throws IOException It will throw exception if there is nothing in the buffer
     */
	public static void putBowlerInfo( String nickName, String fullName, String email) throws IOException{
		String data = nickName + "\t" + fullName + "\t" + email + "\n";
		RandomAccessFile out = new RandomAccessFile(BOWLER_DAT, "rw");
		out.skipBytes((int) out.length());
		out.writeBytes(data);
		out.close();
	}

    /**
     * Retrieves a list of nicknames from the bowler database
     * @return a Vector of Strings
	 * @throws IOException It will throw exception if there is nothing in the buffer
     */
	public static ArrayList<String> getBowlers() throws IOException{
		ArrayList<String> allBowlers = new ArrayList<>(); 					// Contains the Nick name of all the bowlers
		BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT));	// reading from the file
		String data;														// storing input from the buffer
		while ((data = in.readLine()) != null) {
			// File format is "nick"+'\t'+"fname"+'\t'+"e-mail"
			String[] bowler = data.split("\t");
			//"Nick: bowler[0] Full: bowler[1] email: bowler[2]
			allBowlers.add(bowler[0]);
		}
		return allBowlers;
	}
}