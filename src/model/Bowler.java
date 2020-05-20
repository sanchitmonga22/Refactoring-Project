package model;

/**
 *  Class that holds all bowler info
 */
public class Bowler {

    private String fullName;
    private String nickName;
    private String email;

    /** Bowler()
     * The constructor for a Bowler object
     * @param nick the bowler's nickname
     * @param full the bowler's full name
     * @param mail the bowler's email address
     */
    public Bowler( String nick, String full, String mail ) {
		nickName = nick;
		fullName = full;
  		email = mail;
    }

    /** getNickName()
     * @return the nickname of the bowler
     */
    public String getNickName() {
        return nickName;
    }

    /** getFullName()
     * @return the full name of the bowler
     */
	public String getFullName ( ) {
			return fullName;
	}

    /** getEmail()
     * @return the email of the bowler
     */
	public String getEmail ( ) {
		return email;	
	}

}