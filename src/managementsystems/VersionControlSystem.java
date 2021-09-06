package managementsystems;

import studytools.CollabManager;
import studytools.StudyTool;
import studytools.StudyToolDisplayer;
import studytools.StudyToolManager;
import textui.RegularPresenter;
import users.UserManager;

import java.util.Scanner;

/**
 * Deals with viewing and reverting back to past versions of a study tool
 */
public class VersionControlSystem implements ManagementRunnable {
    private StudyToolManager studyToolManager;
    private CollabManager collabManager;
    private UserManager userManager;
    private StudyToolDisplayer studyToolDisplayer;
    private RegularPresenter regularPresenter;
    private String thisUserId;
    private Scanner scanner;

    public VersionControlSystem(StudyToolManager studyToolManager, CollabManager collabManager, UserManager userManager,
                                StudyToolDisplayer studyToolDisplayer, RegularPresenter regularPresenter, String thisUserId,
                                Scanner scanner) {
        this.studyToolManager = studyToolManager;
        this.collabManager = collabManager;
        this.userManager = userManager;
        this.studyToolDisplayer = studyToolDisplayer;
        this.regularPresenter = regularPresenter;
        this.thisUserId = thisUserId;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        studyToolDisplayer.viewAllOwnedStudyTools(studyToolManager, thisUserId);
        studyToolDisplayer.diaplayAllInvitedStudyTools(studyToolManager, collabManager, userManager, thisUserId);
        regularPresenter.chooseStudyToolToEditPrompter();
        String studyToolId = scanner.nextLine();
        if (!(studyToolManager.belongsToAuthor(studyToolId, thisUserId) || collabManager.isCollaborator(studyToolId, thisUserId))) {
            regularPresenter.noStudyToolToEditSorrier();
            return;
        }
        // Now the user is allowed to edit this study tool
        studyToolDisplayer.displayEditHistory(collabManager, userManager, studyToolId);
        while (true) {
            regularPresenter.revertVersionWithQuitPrompter();
            String version = scanner.nextLine();
            if (collabManager.isValidVersionIndex(studyToolId, version)) {
                replaceWithPreviousVersion(studyToolId, Integer.parseInt(version));
                break;
            }
            else if (version.equalsIgnoreCase("q")) return;
            else regularPresenter.sayInvalidInput();
        }
    }

    private void replaceWithPreviousVersion(String studyToolId, int mostRecentIndex) {
        StudyTool oldStudyTool = studyToolManager.getStudyToolByID(studyToolId);
        StudyTool newStudyTool = collabManager.fetchFromHistory(studyToolId, mostRecentIndex);
        studyToolManager.replaceStudyTool(oldStudyTool, newStudyTool);
        collabManager.addToHistory(newStudyTool, thisUserId);
        regularPresenter.sayStudyToolVersionReverted();
    }
}
