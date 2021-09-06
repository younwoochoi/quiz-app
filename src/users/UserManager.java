package users;

import filereadwriter.IGateway;
import filereadwriter.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * An instance of this class represents a user manager to handle users.
 * - Creates users
 * - Edits user information (username, password)
 * - Allows or rejects log-in attempts
 */
public class UserManager implements Serializable {

    private HashMap<User, UserType> userDictionary;
    private Map<String, User> idMapper;
    private Map<String, User> nameMapper;
    private HashMap<String, HashSet<String>> friendMapper;

    private HashMap<String, LocalDate[]> dateMapper; // Maps each id to (creation date, available from date, available to, last log in)
    private IGateway ig = new Serializer();

    public enum UserType {
        ADMIN,
        REGULAR,
        TRIAL,
        TEMPORARY
    }

    public UserManager() {
        this.userDictionary = new HashMap<>();
        this.idMapper = new HashMap<>();
        this.nameMapper = new HashMap<>();
        this.dateMapper = new HashMap<>();
        this.friendMapper = new HashMap<>();
    }

    public void loadFromMaps(HashMap<User, UserType> userDictionary, HashMap<String, LocalDate[]> dateMapper,
                             HashMap<String, HashSet<String>> friendMapper) {
        this.userDictionary = userDictionary;
        this.dateMapper = dateMapper;
        this.friendMapper = friendMapper;
        inferFromUsersDictionary();
    }

    public void saveToSer(String usersFilePath, String dateFilePath, String friendFilePath) throws IOException {
        ig.serializeObject(this.userDictionary, usersFilePath);
        ig.serializeObject(this.dateMapper, dateFilePath);
        ig.serializeObject(this.friendMapper, friendFilePath);
    }

    public boolean addFriend(String username, String friendName) {
        if (nameMapper.containsKey(username) && nameMapper.containsKey(friendName)) {
            if (!friendMapper.containsKey(username)) {
                friendMapper.put(username, new HashSet<>());
            }
            friendMapper.get(username).add(friendName);
            return true;
        }
        return false;
    }

    public boolean isFriend(String username, String friendName) {
        return friendMapper.containsKey(username) && friendMapper.get(username).contains(friendName);
    }

    public String findUserNameByID(String id) {
        return idMapper.get(id).getUsername();
    }

    /**
     * Creates user iff the user name is unique. Returns if successful creation.
     *
     * @param username
     * @param password
     * @param userType
     * @return true if user is successfully create; false if username taken
     */
    public boolean createUser(String username, String password, String email, UserType userType) {
        if (nameMapper.containsKey(username) || username.equalsIgnoreCase("q")) {
            return false;
        } else {
            Password verifier = new Password(password);
            if (!verifier.validatePassword()) {
                return false;
            }
            User newUser = new User(username, password, email);
            userDictionary.put(newUser, userType);
            idMapper.put(String.valueOf(newUser.getId()), newUser);
            nameMapper.put(username, newUser);
            if (userType.equals(UserType.TEMPORARY)) {
                this.dateMapper.put(String.valueOf(newUser.getId()), new LocalDate[]{LocalDate.now(), LocalDate.now(),
                        LocalDate.now().plusDays(30), LocalDate.of(9999, 12, 31)});
            } else
                this.dateMapper.put(String.valueOf(newUser.getId()), new LocalDate[]{LocalDate.now(), LocalDate.now(),
                        LocalDate.of(9999, 12, 31), LocalDate.of(9999, 12, 31)});
            return true;
        }
    }

    public void changePassword(String userID, String password) {
        idMapper.get(userID).setPassword(password);
    }


    /**
     * Searches the document for username and password combo and returns true if combo exists, false otherwise.
     *
     * @param username
     * @param password
     * @return true if username and password combo exists in documents, false otherwise
     */
    public boolean validateUsernameAndPassword(String username, String password) {
        try {
            return nameMapper.get(username).getPassword().equals(password);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public UserType getUserType(String username) {
        return userDictionary.get(nameMapper.get(username));
    }

    /**
     * Searches for the username in dictionary.
     *
     * @param username
     * @return true if username exists, false otherwise.
     */
    public String getUserID(String username) {
        try {
            return String.valueOf(nameMapper.get(username).getId());
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getUserEmail(String username) {
        return idMapper.get(nameMapper.get(username)).getEmail();
    }

    public String getUserGuid(String username) {
        return idMapper.get(nameMapper.get(username)).getGuid();
    }

    public boolean existsUser(String username) {
        return (nameMapper.containsKey(username));
    }

    public String findIDByName(String name) {
        return String.valueOf(this.nameMapper.get(name).getId());
    }

    public List<Map<String, String>> getUsers(int numDaysFrozen) {
        List<Map<String, String>> retList = new ArrayList<>();
        for (String userID : idMapper.keySet()) {
            Map<String, String> retMap = new HashMap<>();
            retMap.put("userID", userID);
            retMap.put("name", findUserNameByID(userID));
            retMap.put("frozen", String.valueOf(isFrozenUser(userID, numDaysFrozen)));
            retMap.put("suspended", String.valueOf(!isAvailableUser(findUserNameByID(userID))));
            retMap.put("suspendedFor", String.valueOf(suspendedFor(findUserNameByID(userID))));
            retMap.put("lastLogIn", String.valueOf(dateMapper.get(userID)[3]));
            retList.add(retMap);
        }
        return retList;
    }

    /**
     * Returns if the date now is after the user's available from and before the available to date
     *
     * @param name username of the user to check for availability
     * @return true if user is allowed to log in, false otherwise
     */
    public boolean isAvailableUser(String name) {

        LocalDate dateNow = LocalDate.now(ZoneId.of("GMT-4"));
        String id = findIDByName(name);
        return (dateNow.isAfter(dateMapper.get(id)[1]) || dateNow.isEqual(dateMapper.get(id)[1])) &&
                (dateNow.isBefore(dateMapper.get(id)[2]) || dateNow.isEqual(dateMapper.get(id)[2]));
    }

    /**
     * Suspends a user, i.e. make the available from date numDays days from now
     *
     * @param name    username of user to suspend
     * @param numDays number of days to suspend the user
     */
    public void suspendUser(String name, int numDays) {

        String id = findIDByName(name);
        LocalDate dateNow = LocalDate.now(ZoneId.of("GMT-4"));
        dateMapper.get(id)[1] = dateNow.plusDays(numDays);
    }

    /**
     * Returns how long the user is suspended for.
     * @param name The user's name.
     * @return The number of days more this user is suspended.
     */
    private int suspendedFor(String name) {
        int suspendedFor = (int)ChronoUnit.DAYS.between(LocalDate.now(ZoneId.of("GMT-4")),
                dateMapper.get(findIDByName(name))[1]);
        if (suspendedFor >= 0) {
            return suspendedFor;
        } else {
            return 0;
        }
    }

    public List<String> frozenUsers(int numDaysFrozen) {
        List<String> retArray = new ArrayList<>();
        for (String userID : idMapper.keySet()) {
            if (isFrozenUser(userID, numDaysFrozen)) {
                retArray.add(userID);
            }
        }
        return retArray;
    }

    public List<String> unfrozenUsers(int numDaysFrozen) {
        List<String> retArray = new ArrayList<>();
        for (String userID : idMapper.keySet()) {
            if (!isFrozenUser(userID, numDaysFrozen)) {
                retArray.add(userID);
            }
        }
        return retArray;
    }

    public void recordLogIn(String userID) {
        dateMapper.get(userID)[3] = LocalDate.now(ZoneId.of("GMT-4"));
    }

    private boolean isFrozenUser(String userID, int numDaysFrozen) {
        LocalDate lastLogIn = dateMapper.get(userID)[3];
        long daysBetween = ChronoUnit.DAYS.between(lastLogIn, LocalDate.now(ZoneId.of("GMT-4")));
        if (daysBetween >= 0) {
            return daysBetween > numDaysFrozen;
        } else {
            return false;
        }
    }

    private void inferFromUsersDictionary() {
        // Sets idMapper and update user count
        int highestCount = 0;
        for (User u : userDictionary.keySet()) {
            idMapper.put(String.valueOf(u.getId()), u);
            nameMapper.put(u.getUsername(), u);
            if (u.getId() > highestCount) highestCount = u.getId();
        }
        User.setNumUsers(highestCount + 1);
    }
}
