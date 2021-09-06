package managementsystems;

import filereadwriter.TextReadWriter;
import studytools.CollabManager;
import studytools.StudyToolManager;
import templates.TemplateDisplayer;
import templates.TemplateManager;
import textui.AdminPresenter;
import textui.PresenterPrompts;
import users.UserManager;

import java.util.Map;
import java.util.Scanner;

/**
 * An instance of this class represents an admin management system to control interactions of admin users
 * with the program.
 *
 * Controls admin functionalities.
 */
public class AdminManagementSystem extends StudyToolManagementSystem implements ManagementRunnable {

    private AdminPresenter adminPresenter;
    private TemplateDisplayer templateDisplayer;
    private TextReadWriter textReadWriter;
    private String LANGUAGE;
    private String ADMIN_KEY;
    private Scanner scanner;

    private TemplateManagementSystem templateManagementSystem;
    private UserHandlingManagementSystem userHandlingManagementSystem;

    /**
     * Create an instance of AdminManagementSystem.
     * @param userID The current user's ID
     * @param userManager The user manager of the program, already instantiated.
     * @param templateManager The template manager of the program, already instantiated.
     * @param studyToolManager The study tool manager of the program, already instantiated.
     * @param collabManager The collaboration manager of the program, already instantiated.
     * @param promptsMap The prompts map for the program.
     */
    public AdminManagementSystem(String userID, UserManager userManager, TemplateManager templateManager,
                                 StudyToolManager studyToolManager, CollabManager collabManager,
                                 Map<PresenterPrompts, String> promptsMap, String ADMIN_KEY,
                                 String LANGUAGE, UserHandlingManagementSystem userHandlingManagementSystem) {
        super(userID, userManager, templateManager, studyToolManager, collabManager, promptsMap);
        this.adminPresenter = new AdminPresenter(promptsMap);
        this.templateDisplayer = new TemplateDisplayer(promptsMap);
        this.textReadWriter = new TextReadWriter();
        this.scanner = new Scanner(System.in);
        this.LANGUAGE = LANGUAGE;
        this.ADMIN_KEY = ADMIN_KEY;

        this.templateManagementSystem = new TemplateManagementSystem(templateDisplayer, adminPresenter,
                scanner, templateManager);
        this.userHandlingManagementSystem = userHandlingManagementSystem;
    }

    /**
     * Runs the main menu of the AdminManagementSystem allowing admin users to select what they
     * would like to do out of the following options:
     *      1, View/Edit/Create templates.
     *      2, View all study tools to study.
     *      3, Change accessibility of a study tool.
     *      4, Delete a study tool.
     *      5, Create a study tool.
     *      6, Edit your study tools.
     *      7, Handle users (suspend, freeze, ...).
     *      8, Edit the welcome or exit message.
     *      9, Edit the admin key.
     *      10, Quit.
     */
    public void run() {
        boolean running = true;
        while (running) {
            adminPresenter.mainMenu();
            String selection = scanner.nextLine();

            switch (selection) {
                case "1":  // view/edit/create templates
                    templateManagementSystem.run();
                    break;
                case "2":  // view all study tools to study
                    studyToolDisplayer.viewAllStudyTools(studyToolManager, userManager);
                    studyWithStudyTool();
                    break;
                case "3":  // change accessibility of a study tool
                    studyToolDisplayer.viewAllStudyTools(studyToolManager, userManager);
                    changeAccessibilityOfStudyTool();
                    break;
                case "4":  // delete a study tool
                    studyToolDisplayer.viewAllStudyTools(studyToolManager, userManager);
                    deleteStudyTool();
                    break;
                case "5":  // create a study tool
                    createStudyTool();
                    break;
                case "6":  // edit a study tool
                    studyToolDisplayer.viewAllStudyTools(studyToolManager, userManager);
                    editStudyTool();
                    break;
                case "7":  // handle users
                    userHandlingManagementSystem.run();
                    break;
                case "8":  // edit the welcome or exit message
                    editWelcomeOrExitMessage();
                    break;
                case "9": // edit the admin key
                    editAdminKey();
                    break;
                case "10":  // quit
                    running = false;
                    break;
                default:  // invalid input
                    adminPresenter.sayInvalidInput();
            }
        }
    }

    /**
     * Allows user to edit the welcome/exit message of the program of the following options:
     *      1, Welcome message.
     *      2, Exit message.
     *      3, Quit.
     */
    private void editWelcomeOrExitMessage() {
        adminPresenter.welcomerOrExiterPrompt();
        String selection = scanner.nextLine();

        if (selection.equals("1")) {
            adminPresenter.currentWelcomePrompt();
            String newMessage = scanner.nextLine();
            if (!newMessage.equalsIgnoreCase("q")) {
                textReadWriter.editWelcomer(newMessage, this.LANGUAGE);
                promptsMap.put(PresenterPrompts.welcomer, newMessage);
                adminPresenter.sayEdited();
            }
        } else if (selection.equals("2")) {
            adminPresenter.currentExiterPrompt();
            String newMessage = scanner.nextLine();
            if (!newMessage.equalsIgnoreCase("q")) {
                textReadWriter.editExiter(newMessage, this.LANGUAGE);
                promptsMap.put(PresenterPrompts.logInExiter, newMessage);
                adminPresenter.sayEdited();
            }
        } else if (!selection.equals("3")) {
            adminPresenter.sayInvalidInput();
        }
    }

    /**
     * Allows user to edit the admin key.
     */
    private void editAdminKey() {
        adminPresenter.editAdminKeyPrompt(ADMIN_KEY);
        String selection = scanner.nextLine();
        if (!selection.equals("q")) {
            adminPresenter.newAdminKeyPrompt();
            String newKey = scanner.nextLine();

            if (validAdminKey(newKey)) {
                textReadWriter.editAdminKey(newKey);
                adminPresenter.sayEdited();
            } else {
                adminPresenter.sayInvalidInput();
            }
        }
    }
    /**
     * Returns whether the potential admin key is valid or not.
     * @param potentialKey The potential new admin key.
     * @return true if potentialKey is a valid new admin key.
     */
    private boolean validAdminKey(String potentialKey) {
        return !potentialKey.equals("1") && !potentialKey.equals("2") &&
                !potentialKey.equals("3") && !potentialKey.equals("q");
    }

    @Override
    protected boolean verifyRightToView(String studyToolId) {
        return studyToolManager.studyToolExists(studyToolId);
    }
    @Override
    protected boolean verifyRightToEdit(String studyToolId) {
        return studyToolManager.studyToolExists(studyToolId);
    }
    @Override
    //admins don't currently have the ability to undelete
    protected boolean verifyRightToUndelete(String studyToolId) {
        return false;
    }
}