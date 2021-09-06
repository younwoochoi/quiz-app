package managementsystems;

import filereadwriter.IGateway;
import filereadwriter.Serializer;
import studytools.StudyToolManager;
import textui.AdminPresenter;
import textui.PresenterPrompts;
import users.UserDisplayer;
import users.UserManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * An instance of this class represents a user handling management system which contain functionalities
 * pertaining to handling users. i.e. view users, suspension, freeze...
 */
public class UserHandlingManagementSystem implements ManagementRunnable, PropertyChangeListener, Serializable {

    private boolean frozen;
    private int numDaysFrozen;
    private AdminPresenter adminPresenter;
    private UserDisplayer userDisplayer;
    private UserManager userManager;
    private StudyToolManager studyToolManager;
    private Scanner scanner;
    private IGateway iGateway;
    private String userID;

    /**
     * Create an instance of UserHandlingManagementSystem.
     * @param promptsMap
     * @param userManager
     * @param studyToolManager
     * @param scanner
     * @param userID
     */
    public UserHandlingManagementSystem(Map<PresenterPrompts, String> promptsMap,
                                        UserManager userManager, StudyToolManager studyToolManager,
                                        Scanner scanner, String userID) {
        this.frozen = false;
        this.numDaysFrozen = 7;
        this.adminPresenter = new AdminPresenter(promptsMap);
        this.userDisplayer = new UserDisplayer(promptsMap);
        this.scanner = scanner;
        this.userManager = userManager;
        this.studyToolManager = studyToolManager;
        this.iGateway = new Serializer();
        this.userID = userID;
    }

    /**
     * Runs the main menu of the UserHandlingManagementSystem allowing users to select what they
     * would like to do to handle users of the following options:
     *      1, View all users.
     *      2, Create an admin account.
     *      3, Suspend a user.
     *      4, Freeze accounts.
     *      5, Quit.
     */
    public void run() {
        boolean running = true;
        while (running) {
            adminPresenter.userHandlingMenu();
            String selection = scanner.nextLine();
            switch (selection) {
                case "1":  // view all users
                    userDisplayer.displayAllUsers(userManager, numDaysFrozen);
                    break;
                case "2":  // suspend a user
                    suspendUserMenu();
                    break;
                case "3":  // freeze accounts
                    freezeAccounts();
                    break;
                case "4":
                    running = false;
                    break;
                default:  // invalid input
                    adminPresenter.sayInvalidInput();
            }
        }
    }

    public void loadFromMaps(ArrayList<Object> userHandlingData) {
        this.frozen = (boolean)userHandlingData.get(0);
        this.numDaysFrozen = (int)userHandlingData.get(1);
    }

    public void saveToSer(String userHandlingFilePath) throws IOException {
        ArrayList<Object> userHandlingData = new ArrayList<>();
        userHandlingData.add(frozen);
        userHandlingData.add(numDaysFrozen);
        iGateway.serializeObject(userHandlingData, userHandlingFilePath);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("load")) {
            updateFrozenUserStudyTools();
        } else if (evt.getPropertyName().equals("logIn")) {
            userManager.recordLogIn((String)evt.getNewValue());
            updateFrozenUserStudyTools();
        }
    }

    /**
     * Allows user to suspend a user for a chosen number of days, meaning the user cannot log in
     * for these days. Their creations may still be accessible.
     */
    private void suspendUserMenu() {
        boolean suspending = true;
        while(suspending) {
            String name = "";
            while(true) {
                adminPresenter.selectUserToSuspend();
                String input = scanner.nextLine();
                if (input.equals("q") || input.equals(userID)) {
                    suspending = false;
                    break;
                } else if (userManager.existsUser(input)) {
                    name = input;
                    break;
                }
            }
            if (!suspending) {
                break;
            }

            int days;
            while(true) {
                adminPresenter.selectNumberOfDaysToSuspend(name);
                String input = scanner.nextLine();
                try {
                    if (0 < Integer.parseInt(input)) {
                        days = Integer.parseInt(input);
                        break;
                    }
                } catch (NumberFormatException ignored) {}
            }
            userManager.suspendUser(name, days);
            adminPresenter.saySuspensionSuccess();

            adminPresenter.suspendAnotherUserPrompt();
            if (scanner.nextLine().equals("n")) {
                suspending = false;
            }
        }
    }

    /**
     * Allows users to set the program to freeze accounts of anyone who has not logged in since
     * x number of days ago, where x is a number the admin chooses and "freeze" means no one can
     * interact with their creations until they log back in to unfreeze their account.
     */
    private void freezeAccounts() {
        adminPresenter.freezeAccountsPrompt(frozen, numDaysFrozen);
        String selection = scanner.nextLine();
        if (selection.equalsIgnoreCase("y")) {
            if (!frozen) {
                adminPresenter.freezeAccountsForHowManyDaysPrompt();
                try {
                    this.numDaysFrozen = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    adminPresenter.sayInvalidInput();
                    return;
                }
            }
            this.frozen = !frozen;
            adminPresenter.sayProgramFrozen(numDaysFrozen);
            updateFrozenUserStudyTools();
        } else if (!selection.equalsIgnoreCase("n")) {
            adminPresenter.sayInvalidInput();
        }
    }
    /**
     * Updates the status of frozen study tools for accessibility by users depending on the frozen status
     * of the program and the date checking the last recorded log in for each user and changing
     * study tool access accordingly.
     */
    private void updateFrozenUserStudyTools() {
        for (String userID : userManager.frozenUsers(numDaysFrozen)) {
            studyToolManager.freezeStudyToolsForUser(userID);
        }
        for (String userID : userManager.unfrozenUsers(numDaysFrozen)) {
            studyToolManager.unfreezeStudyToolsForUser(userID);
        }
    }
}
