package studytools;

import textui.PresenterPrompts;
import users.UserManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Deals with displaying study tools as text on screen
 */
public class StudyToolDisplayer {
    private Map<PresenterPrompts, String> promptsMap;

    public StudyToolDisplayer(Map<PresenterPrompts, String> promptsMap) {
        this.promptsMap = promptsMap;
    }

    public void displayVisibleStudyTool(String name, String id, String authorName) {
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayName) + name + "; " + promptsMap.get(PresenterPrompts.studyToolDisplayID)
                + id + "; " + promptsMap.get(PresenterPrompts.studyToolDisplayAuthor) + authorName);
    }

    public void displayAnyStudyTool(String name, String id, String authorName, String accessibility) {
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayName) + name + "; " + promptsMap.get(PresenterPrompts.studyToolDisplayID)
                + id + "; " + promptsMap.get(PresenterPrompts.studyToolDisplayAuthor) + authorName + "; " +
                promptsMap.get(PresenterPrompts.studyToolDisplayAccessibility) + accessibility);
    }

    public void displayAnyStudyTool(String name, String id, String accessibility) {
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayName) + name + "; " + promptsMap.get(PresenterPrompts.studyToolDisplayID)
                + id + "; " + promptsMap.get(PresenterPrompts.studyToolDisplayAccessibility) + accessibility);
    }


    public void displayAllQuestionsAndAnswers(String[][] qa) {
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayCurrQA));

        for (int k = 0; k < qa.length; k++) {
            System.out.println(k+1 + ": " + qa[k][0]);
            System.out.println("    " + promptsMap.get(PresenterPrompts.studyToolDisplayAnswer) + qa[k][1]);
        }
    }

    public void displayAQuestion(String[] qa) {
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayQuestion));
        System.out.println(qa[0]);
    }

    public void displayAnAnswer(String[] qa) {
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayAnswerIs));
        System.out.println(qa[1]);
    }

    public void viewAllVisibleStudyTools(StudyToolManager studyToolManager, UserManager userManager, String userId) {
        List<Map<String, String>> visibleStudyTools = studyToolManager.getVisibleStudyTools(userId, userManager);
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayVisibleStudyTools));
        for (Map<String, String> studyToolInfo : visibleStudyTools) {
            displayVisibleStudyTool(studyToolInfo.get("name"), studyToolInfo.get("Id"), userManager.findUserNameByID(studyToolInfo.get("authorId")));
        }
    }

    public void viewAllOwnedStudyTools(StudyToolManager studyToolManager, String thisUserId) {
        List<Map<String, String>> ost = studyToolManager.getStudyToolsByAuthorId(thisUserId);
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayOwnedStudyTools));
        for (Map<String, String> s : ost) {
            displayAnyStudyTool(s.get("name"), s.get("Id"), s.get("accessibility"));
        }
    }

    public void viewAllOwnedDeletedStudyTools(StudyToolManager studyToolManager, String thisUserId) {
        List<Map<String, String>> ost = studyToolManager.getDeletedStudyToolsByAuthorId(thisUserId);
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayOwnedDeletedStudyTools));
        for (Map<String, String> s : ost) {
            displayAnyStudyTool(s.get("name"), s.get("Id"), s.get("accessibility"));
        }
    }

    public void viewAllStudyTools(StudyToolManager studyToolManager, UserManager userManager) {
        List<Map<String, String>> studyTools = studyToolManager.getAllStudyTools();
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayAllStudyTools));
        for (Map<String, String> studyTool : studyTools) {
            displayAnyStudyTool(studyTool.get("name"), studyTool.get("Id"),
                    userManager.findUserNameByID(studyTool.get("authorId")), studyTool.get("accessibility"));
        }
    }
    public void displayAnyStudyToolByID(String id, StudyToolManager studyToolManager) {
        Map<String, String> studyTool = studyToolManager.describeStudyToolbyId(id);
        displayAnyStudyTool(studyTool.get("name"), id, studyTool.get("authorName"), studyTool.get("accessibility"));
    }

    public void diaplayAllInvitedStudyTools(StudyToolManager studyToolManager, CollabManager collabManager, UserManager userManager, String thisUserId) {
        List<String> invited = collabManager.getAllInvitedStudyTools(thisUserId);
        System.out.println(promptsMap.get(PresenterPrompts.studyToolDisplayInvitedStudyTools));
        for (String id : invited) {
            Map<String, String> s = studyToolManager.describeStudyToolbyId(id);
            displayAnyStudyTool(s.get("name"), s.get("Id"), userManager.findUserNameByID(s.get("authorId")), s.get("accessibility"));
        }
    }

    public void displayAllCollaborators(CollabManager collabManager, UserManager userManager, String studyToolID) {
        List<String> invited = collabManager.getCollaboratorsID(studyToolID);
        if (invited == null || invited.isEmpty()) {
            System.out.println(promptsMap.get(PresenterPrompts.noCollaboratorsExist));
        }
        else {
            System.out.println(promptsMap.get(PresenterPrompts.displayAllCollaborators));
            for (String userID : invited) {
                System.out.print(userManager.findUserNameByID(userID) + "; ");
            }
            System.out.println();
        }
    }

    /**
     * Display edit history from latest to earliest
     */
    public void displayEditHistory(CollabManager collabManager, UserManager userManager, String studyToolId) {
        StudyToolHistory studyToolHistory = collabManager.getEntireHistory(studyToolId);
        Map<LocalDateTime, StudyTool> historyMap = studyToolHistory.getHistoryMapper();
        Map<LocalDateTime, String> editorMap = studyToolHistory.getEditorMapper();
        List<LocalDateTime> times = studyToolHistory.getLatestToEarliestDateTimes();
        System.out.println(promptsMap.get(PresenterPrompts.editHistory));
        for (int k = 0; k < times.size(); k++) {
            LocalDateTime thisTime = times.get(k);
            StudyTool thisStudyTool = historyMap.get(thisTime);
            String editorName = userManager.findUserNameByID(editorMap.get(thisTime));
            System.out.println((k+1) + ", " + "{");
            System.out.println(promptsMap.get(PresenterPrompts.editTime) + ": " + formatDateTime(thisTime));
            System.out.println(promptsMap.get(PresenterPrompts.editorName) + ": " + editorName);
            displayAllQuestionsAndAnswers(thisStudyTool.getQuestionsAndAnswers());
            System.out.println("}\n");
        }
    }

    private String formatDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
    }
}