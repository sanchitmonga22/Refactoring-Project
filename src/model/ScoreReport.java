package model;
/**
 * SMTP implementation based on code by Réal Gagnon mailto:real@rgagnon.com
 */
import util.PrintableText;
import util.ScoreHistoryFile;
import java.io.*;
import java.util.List;
import java.net.*;
import java.awt.print.*;

public class ScoreReport {

	private String content;

	/** ScoreReport()
	 * Constructor for a ScoreReport
	 * @param bowler the bowler who's score is being reported
	 * @param scores the bowler's scores
	 * @param games the number of games the bowler played
	 */
	public ScoreReport(Bowler bowler, int[] scores, int games ) {
		String nick = bowler.getNickName();
		String full = bowler.getFullName();
		List<Score> scoreList= null;
		try{
			scoreList = ScoreHistoryFile.getScores(nick);
		} catch (Exception e){System.err.println("Error: " + e);}

		content = "";
		content += "--Lucky Strike Bowling Alley Score Report--\n";
		content += "\n";
		content += "Report for " + full + ", aka \"" + nick + "\":\n";
		content += "\n";
		content += "Final scores for this session: ";
		content += scores[0];
		for (int i = 1; i < games; i++){
			content += ", " + scores[i];
		}
		content += ".\n";
		content += "\n";
		content += "\n";
		content += "Previous scores by date: \n";
		for(Score score: scoreList){
			content += "  " + score.getDate() + " - " +  score.getScore();
			content += "\n";
		}
		content += "\n\n";
		content += "Thank you for your continuing patronage.";

	}

	/** sendEmail()
	 * Sends an email containing the score report to a specified recipient
	 * @param recipient the email of the recipient
	 */
	public void sendEmail(String recipient) {
		try {
			Socket s = new Socket("osfmail.rit.edu", 25);
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(s.getInputStream(), "8859_1"));
			BufferedWriter out =
				new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream(), "8859_1"));

			// here you are supposed to send your username
			sendln(in, out, "HELLO world");
			sendln(in, out, "MAIL FROM: <abc1234@rit.edu>");
			sendln(in, out, "RCPT TO: <" + recipient + ">");
			sendln(in, out, "DATA");
			sendln(out, "Subject: Bowling Score Report ");
			sendln(out, "From: <Lucky Strikes Bowling Club>");

			sendln(out, "Content-Type: text/plain; charset=\"us-ascii\"\r\n");
			sendln(out, content + "\n\n");
			sendln(out, "\r\n");

			sendln(in, out, ".");
			sendln(in, out, "QUIT");
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** sendPrintOut()
	 *
	 */
	public void sendPrintout() {
		PrinterJob job = PrinterJob.getPrinterJob();

		PrintableText printText = new PrintableText(content);

		job.setPrintable(printText);

		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException e) {
				System.out.println(e);
			}
		}

	}

	/** sendln()
	 *  sends a single line while taking a specified input
	 * @param in the line taken in
	 * @param out the line to be sent out
	 * @param s the string to be sent
	 */
	public void sendln(BufferedReader in, BufferedWriter out, String s) {
		try {
			out.write(s + "\r\n");
			out.flush();
			in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** sendln()
	 * sends a single line
	 * @param out the line to be sent out
	 * @param s the string to be sent
	 */
	public void sendln(BufferedWriter out, String s) {
		try {
			out.write(s + "\r\n");
			out.flush();
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
