package nl.gottogo.gottogoapplication;

/**
 * Created by Merik on 05/04/2017.
 */

public class Logic {

    private String email;

    private static Logic instance = null;
    protected Logic(){

    }

    public static Logic getInstance(){
        if(instance == null){
            instance = new Logic();
        }
        return instance;
    }

    public String getUserMail(){
        return this.email;
    }

    public void setUserMail(String mail){
        this.email = mail;
    }

}
