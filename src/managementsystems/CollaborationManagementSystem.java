package managementsystems;

import studytools.CollabManager;
import studytools.StudyToolDisplayer;
import studytools.StudyToolManager;
import textui.PresenterPrompts;
import textui.RegularPresenter;
import users.UserManager;

import java.util.Map;
import java.util.Scanner;

/**
 * Deals with viewing, adding and removing collaborators for a study tool of user's choice
 */
public class CollaborationManagementSystem implements ManagementRunnable {
    UserManager userManager;
    StudyToolManager studyToolManager;
    CollabManager collabManager;
    String thisUserID;
    RegularPresenter regularPresenter;
    StudyToolDisplayer studyToolDisplayer;

    public CollaborationManagementSystem(UserManager userManager, StudyToolManager studyToolManager,
                                         CollabManager collabManager, Map<PresenterPrompts, String> promptsMap, String thisUserID) {
        this.userManager = userManager;
        this.studyToolManager = studyToolManager;
        this.collabManager = collabManager;
        this.thisUserID = thisUserID;
        this.regularPresenter = new RegularPresenter(promptsMap);
        this.studyToolDisplayer = new StudyToolDisplayer(promptsMap);
    }

    public void run() {
        studyToolDisplayer.viewAllOwnedStudyTools(studyToolManager, thisUserID);
        String studyToolID = getValidStudyToolIDWithQuit();
        if (studyToolID.equalsIgnoreCase("q")) return;
        // Now study tool ID must be a valid study tool ID
        studyToolDisplayer.displayAllCollaborators(collabManager, userManager, studyToolID);
        regularPresenter.collabMenu();
        regularPresenter.chooseOptionWithQuitPrompter();
        Scanner scanner = new Scanner(System.in);
        String userChoice = scanner.nextLine();
        if (userChoice.equals("1")) addCollaborator(studyToolID);
        else if (userChoice.equals("2")) removeCollaborator(studyToolID);
    }

    private String getValidStudyToolIDWithQuit() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            regularPresenter.collaborateStudyToolIDWithQuitPrompter();
            String studyToolID = scanner.nextLine();
            if (studyToolManager.belongsToAuthor(studyToolID, thisUserID) ||
                    studyToolID.equalsIgnoreCase("q")) return studyToolID;
            else {regularPresenter.noStudyToolToEditSorrier();}
        }
    }

    private void addCollaborator(String studyToolID) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            regularPresenter.collaboratorNameWithQuitPrompter();
            String collabName = scanner.nextLine();
            if (!userManager.existsUser(collabName) || thisUserID.equals(userManager.getUserID(collabName))) {
                regularPresenter.userDoesNotExistSorrier();
            }
            else if (collabName.equalsIgnoreCase("q")) break;
            else {
                collabManager.addCollaborator(studyToolID, userManager.getUserID(collabName));
                regularPresenter.sayCollaboratorAdded();
                break;
            }
        }
    }

    private void removeCollaborator(String studyToolID){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            regularPresenter.collaboratorNameWithQuitPrompter();
            String collabName = scanner.nextLine();
            if (!userManager.existsUser(collabName) || thisUserID.equals(userManager.getUserID(collabName))) {
                regularPresenter.userDoesNotExistSorrier();
            }
            else if (collabName.equalsIgnoreCase("q")) break;
            else {
                collabManager.removeCollaborator(studyToolID, userManager.getUserID(collabName));
                regularPresenter.sayCollaboratorRemoved();
                break;
            }
        }
    }
}
