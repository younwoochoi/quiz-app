package managementsystems;

import filereadwriter.PromptsLoader;
import filereadwriter.Serializer;
import filereadwriter.TextReadWriter;
import studytools.CollabManager;
import studytools.StudyToolManager;
import templates.TemplateManager;
import textui.LogInPresenter;
import textui.PresenterPrompts;
import users.UserManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

/**
 * An instance of this class represents a log in management system to handle any functionalities
 * pertaining to logging in to access the program.
 */
public class LogInManagementSystem implements ManagementRunnable {
    UserManager userManager = new UserManager();
    TemplateManager templateManager = new TemplateManager();
    StudyToolManager studyToolManager = new StudyToolManager();
    CollabManager collabManager = new CollabManager();
    Map<PresenterPrompts, String> promptsMap;
    LogInPresenter logInPresenter;
    PromptsLoader promptsLoader;
    Serializer serializer;
    TextReadWriter textReadWriter;
    ManagementRunnable managementRunnable;
    UserHandlingManagementSystem userHandlingManagementSystem;
    String userID;
    UserManager.UserType userType;
    String ADMIN_KEY;
    String LANGUAGE;

    /**  Helper class for making this class observable  */
    private PropertyChangeSupport observable;

    final static String TRIAL_PASSWORD = "test1@#$TEST";
    final static String TRIAL_EMAIL = "test@gmail.com";

    Scanner scanner = new Scanner(System.in);

    /**
     * Create an instance of LogInManagementSystem.
     * @param language The current language the program is running with.
     */
    public LogInManagementSystem(String language) {
        this.LANGUAGE = language;
        this.observable = new PropertyChangeSupport(this);
    }

    /**
     * Runs the log in management system. It calls to load the game, and save
     * the progress after studying. It passes all managers into the
     * sub-management systems to ensure only one unique game progress exists.
     */
    public void run() {
        promptsLoader = new TextReadWriter();
        promptsMap = promptsLoader.promptsLoader(LANGUAGE);
        userHandlingManagementSystem = new UserHandlingManagementSystem(promptsMap, userManager,
                studyToolManager, scanner, userID);
        logInPresenter = new LogInPresenter(promptsMap);
        textReadWriter = new TextReadWriter();
        serializer = new Serializer(userManager, templateManager, studyToolManager,
                collabManager, userHandlingManagementSystem);
        addObserver(serializer);
        addObserver(userHandlingManagementSystem);

        loadGame();

        logInPresenter.welcomer();

        createAccountOrLogIn();

        if (userType == UserManager.UserType.ADMIN) {
            managementRunnable = new AdminManagementSystem(userID, userManager, templateManager, studyToolManager,
                    collabManager, promptsMap, ADMIN_KEY, LANGUAGE, userHandlingManagementSystem);
        } else {
            managementRunnable = new RegularManagementSystem(userID, userManager, templateManager, studyToolManager,
                    collabManager, promptsMap, scanner);
        }

        logInPresenter.displayUserInfo(userManager.findUserNameByID(userID), userID, userType);
        notifyObservers(new PropertyChangeEvent(this, "logIn", null, userID));
        managementRunnable.run();
        saveProgress();     // update
        logInPresenter.exiter();
    }

    /**
     * Notifies observers to load the user manager, template manager, study tool manager,
     * collaboration manager and user handling management system from files.
     */
    private void loadGame() {
        notifyObservers(new PropertyChangeEvent(this, "load", null, null));
        ADMIN_KEY = textReadWriter.loadAdminKey();
    }

    /**
     * Notifies observers to save the game progress in their respective files.
     */
    private void saveProgress() {
        if (userType != UserManager.UserType.TRIAL) {
            logInPresenter.saveOrNotPrompter();
            String save = scanner.nextLine().trim();

            if (save.equalsIgnoreCase("y")) {
                notifyObservers(new PropertyChangeEvent(this, "save", null, null));
            }
        }
    }

    /**
     * Runs the main menu of the LogInManagementSystem allowing users to select what they
     * would like to do to enter the program of the following options:
     *      1, Log In.
     *      2, Create an account / start a trial.
     *      3, Reset a password for account.
     */
    private void createAccountOrLogIn() {
        boolean entered = false;
        while (!entered) {
            logInPresenter.lCreateAccountOrLoginPrompt();
            String selection = scanner.nextLine();

            switch (selection) {
                case "1":  // log in
                    entered = login();
                    break;
                case "2":  // create account
                    entered = createAccount();
                    break;
                case "3":  // reset password
                    resetPassword();
                    break;
                default:
                    logInPresenter.lSayInvalidInput();
                    break;
            }
        }
    }

    private void addObserver(PropertyChangeListener observer) {
        observable.addPropertyChangeListener("save", observer);
    }

    private void notifyObservers (PropertyChangeEvent event) {
        for (PropertyChangeListener observer : observable.getPropertyChangeListeners())
            observer.propertyChange(event);
    }

    /**
     * Prompts user for information to create an account or start a trial
     * @return true if account was created or trial was started, false otherwise
     */
    private boolean createAccount() {
        logInPresenter.typeOfAccountMenu();
        String accountType = scanner.nextLine();
        if (! (accountType.equals("1") || accountType.equals("2") ||
                accountType.equals("3") || accountType.equals(ADMIN_KEY)) ) {
            logInPresenter.lCreateAccountError();
            return false;
        }

        logInPresenter.lCreateAccountUsernamePrompt();
        String username = scanner.nextLine();
        if (userManager.existsUser(username)) {
            logInPresenter.lSayUsernameNotUnique();
            logInPresenter.lCreateAccountError();
            return false;
        }

        String password = TRIAL_PASSWORD;
        switch (accountType) {
            case "1":
                userType = UserManager.UserType.REGULAR;
                break;
            case "2":
                userType = UserManager.UserType.TEMPORARY;
                break;
            case "3":
                userType = UserManager.UserType.TRIAL;
                break;
            default:
                if (accountType.equals(ADMIN_KEY)) {
                    userType = UserManager.UserType.ADMIN;
                } else {
                    logInPresenter.lSayIncorrectKey();
                    return false;
                }
        }

        if (!userType.equals(UserManager.UserType.TRIAL)) {
            logInPresenter.lCreateAccountPasswordPrompt();
            password = scanner.nextLine();
        }

        logInPresenter.lCreateAccountEmailPrompt();
        String email = scanner.nextLine();

        if (userManager.createUser(username, password, email, userType)) {
            userID = userManager.getUserID(username);
            logInPresenter.lSaySuccessfulAccountCreation();
            return true;
        }
        logInPresenter.lCreateAccountError();
        return false;
    }

    /**
     * Prompts user for username and password
     * @return true if log in was successful, false otherwise
     */
    private boolean login() {
        logInPresenter.lLogInUsernamePrompt();
        String username = scanner.nextLine();
        if (!userManager.existsUser(username)) {
            logInPresenter.sayUserNameNotExist();
            return false;
        } else if (!userManager.isAvailableUser(username)) {
            logInPresenter.sayAccountSuspended();
            return false;
        }

        logInPresenter.lLogInPasswordPrompt();
        String password = scanner.nextLine();
        if (userManager.validateUsernameAndPassword(username, password)) {
            userID = userManager.getUserID(username);
            userType = userManager.getUserType(username);

            logInPresenter.lSayLoginSuccess();
            return true;
        }

        logInPresenter.lSayLoginError();
        return false;
    }

    /**
     * Prompts user for password reset
     * @return true if password was reset, false otherwise
     */
    private boolean resetPassword() {
        logInPresenter.lUsernameResetPrompt();
        String username = scanner.nextLine();
        try { // Still in progress
            String data = "email=" + userManager.getUserEmail(username) + "&guid=" + userManager.getUserGuid(username);
            URL url = new URL("https://thxt0k2q0c.execute-api.us-east-1.amazonaws.com/default?email=");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes("UTF-8"));
            con.getInputStream();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        logInPresenter.lResetAccountPrompt();
        scanner.nextLine();

        logInPresenter.lProvideNewPinPrompt();
        String pin = scanner.nextLine();

        if(pin.matches(userManager.getUserGuid(username))){
            logInPresenter.lCreateAccountPasswordPrompt();
            String password = scanner.nextLine();
            userManager.changePassword(username, password);
            logInPresenter.lSayPasswordChangeSuccess();
        }
        else{
            logInPresenter.lInvalidPinPrompt();
            return false;
        }
        return true;
    }
}
