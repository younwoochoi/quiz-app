package users;

import java.io.Serializable;
import java.util.UUID;

/**
 * A user in the study tool
 */
public class User implements Serializable {

    private static int numUsers = 0;
    private int id;
    private String username;
    private String password;
    private String email;
    private String guid;

    User(String username, String password, String email){
            this.username = username;
            this.password = password;
            this.email = email;
            this.guid = UUID.randomUUID().toString();

            this.id = numUsers++;
        }

        String getUsername() {
            return username;
        }

        String getPassword() {
            return password;
        }

        String getEmail() {
            return email;
        }

        String getGuid() {
            return guid;
        }

        void setPassword(String password) {
            this.password = password;
        }

        int getId() {
            return id;
        }

        static void setNumUsers(int numUsers) {
            User.numUsers = numUsers;
        }
    }
