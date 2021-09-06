package managementsystems;

import studytools.CollabManager;
import studytools.StudyToolManager;
import templates.TemplateManager;
import textui.PresenterPrompts;
import users.UserManager;

import java.util.Map;
import java.util.Scanner;

/**
 * Deals with all regular user functionalities such as creating, editing and playing quiz.
 * Also collaboration and version control features.
 */
public class RegularManagementSystem extends StudyToolManagementSystem implements ManagementRunnable {

    Scanner scanner;

    public RegularManagementSystem(String thisUserId, UserManager userManager,
                                   TemplateManager templateManager, StudyToolManager studyToolManager,
                                   CollabManager collabManager, Map<PresenterPrompts, String> promptsMap,
                                   Scanner scanner) {
        super(thisUserId, userManager, templateManager, studyToolManager, collabManager, promptsMap);
        this.scanner = scanner;
    }

    public void run(){
        /*
        Allow users to see a list of public study tools, a list of their own study tools
        Allow users to edit study their own tools
        Allow users to create study tools
        Allow users to change accessibility of their own study tools
        Allow users to play any of the public study tools or their own study tools
        Upon quitting, return if save or not, depending on user's choice
         */

       boolean running = true;

        while (running){
            regularPresenter.pressEnterToContinue();
            scanner.nextLine();
            regularPresenter.displayMainMenuChoices();

            /* Menu choices:
                1, View all study tools available to you.
                2, View all study tools created by you and those you are invited to edit.
                3, Edit a study tool.
                4, Study with a public study tool or one of your own study tools.
                5, Create study tool.
                6, Change the accessibility of a study tool that you have created.
                7, View edit history of a study tool / Revert to a previous version
                8, Manage collaboration on your study tools.
                9, Add a friend.
                10, Delete one of your study tools.
                11, Undelete a study tool.
             */

            regularPresenter.chooseOptionWithQuitPrompter();
            String choice = scanner.nextLine();

            switch (choice) {
                case "q": case "Q":
                    running = false;
                    break;
                case "1":
                    studyToolDisplayer.viewAllVisibleStudyTools(studyToolManager, userManager, thisUserId);
                    break;
                case "2":
                    studyToolDisplayer.viewAllOwnedStudyTools(studyToolManager, thisUserId);
                    studyToolDisplayer.diaplayAllInvitedStudyTools(studyToolManager, collabManager, userManager, thisUserId);
                    break;
                case "3":
                    editStudyTool();
                    break;
                case "4":
                    studyWithStudyTool();
                    break;
                case "5":
                    createStudyTool();
                    break;
                case "6":
                    changeAccessibilityOfStudyTool();
                    break;
                case "7":
                    launchVersionControlSystem();
                    break;
                case "8":
                    launchCollaborationControl();
                    break;
                case "9":
                    addFriend();
                    break;
                case "10":  // delete a study tool
                    studyToolDisplayer.viewAllOwnedStudyTools(studyToolManager, thisUserId);
                    deleteStudyTool();
                    break;
                case "11":  // undelete a study tool
                    studyToolDisplayer.viewAllOwnedDeletedStudyTools(studyToolManager, thisUserId);
                    undeleteStudyTool();
                    break;
                default:
                    regularPresenter.sayInvalidInput();
                    break;
            }
        }
    }

    private void addFriend() {
        regularPresenter.chooseUserToAddFriendPrompter();
        String friendName = scanner.nextLine();
        boolean friendAdded = userManager.addFriend(userManager.getUserID(thisUserId), friendName);
        if (!friendAdded) {
            regularPresenter.friendAddFailed();
        }
        else {
            regularPresenter.friendAddSucceeded();
        }
    }

    private void launchCollaborationControl() {
        ManagementRunnable collaborationManagementSystem = new CollaborationManagementSystem(userManager,
                studyToolManager, collabManager, promptsMap, thisUserId);
        collaborationManagementSystem.run();
    }

    @Override
    protected boolean verifyRightToView(String studyToolId) {
        return (studyToolManager.verifyVisibility(studyToolId, thisUserId) ||
                collabManager.isCollaborator(studyToolId, thisUserId)) &&
                !studyToolManager.isStudyToolDeleted(studyToolId);
    }

    @Override
    protected boolean verifyRightToEdit(String studyToolId) {
        return (studyToolManager.belongsToAuthor(studyToolId, thisUserId) ||
                collabManager.isCollaborator(studyToolId, thisUserId)) &&
                !studyToolManager.isStudyToolDeleted(studyToolId);
    }

    @Override
    protected boolean verifyRightToUndelete(String studyToolId) {
        return studyToolManager.belongsToAuthor(studyToolId, thisUserId)  &&
                studyToolManager.isStudyToolDeleted(studyToolId);
    }
}
