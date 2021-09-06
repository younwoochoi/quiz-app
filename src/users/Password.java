package users;

/*
Password checker conditions:
- is at least 10 characters
- no whitespaces
- at least one capital letter
- at least one lowercase letter
- at least one digit
- at least one special character
 */



public class Password {
    private String password;

    public Password(String password){
        this.password = password;
    }

    public boolean validatePassword(){
        String complexityValidator = "^(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])(?=.*[\\-_@#$%^&+=])(?=\\S+$).{10,}$";
        if(password.matches(complexityValidator)){
            return true;
        }
        else{
            return false;
        }
    }


}
