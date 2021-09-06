package textui;

import templates.TemplateManager;

import java.util.Map;

/**
 * Displays regular user functionalities, i.e. viewing/creating/editing/studying study tools, other prompts/notices.
 */
public class RegularPresenter {
    Map<PresenterPrompts, String> promptsMap;

    public RegularPresenter(Map<PresenterPrompts, String> promptsMap) {
        this.promptsMap = promptsMap;
    }

    public void chooseOptionWithQuitPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.chooseOptionWithQuitPrompt));
    }

    public void chooseUserToAddFriendPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.chooseUserToAddFriendPrompt));
    }

    public void chooseStudyToolToEditPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.chooseStudyToolToEditPrompt));
    }

    public void chooseStudyToolToStudyPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.chooseStudyToolToStudyPrompt));
    }

    public void chooseQuestionNumberToEditPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.chooseQuestionNumberToEditPrompt));
    }

    public void newQuestionPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.newQuestionPrompt));
    }

    public void newQuestionWithQuitPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.newQuestionWithQuitPrompt));
    }

    public void newAnswerPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.newAnswerPrompt));
    }

    public void templateIDPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.templateIDPrompt));
    }

    public void saveOrNotPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.saveOrNotPrompt));
    }

    public void studyToolNamePrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.setStudyToolNamePrompt));
    }

    public void setAccessibilityPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.setAccessibilityPrompt));
    }

    public void displayMainMenuChoices() {
        System.out.println(promptsMap.get(PresenterPrompts.regularMainMenuChoicesDisplay) + "\n");
    }

    public void displayEditMenuChoices() {
        System.out.println(promptsMap.get(PresenterPrompts.studyToolEditMenuChoicesDisplay) + "\n");
    }

    public void pressEnterToContinue() {
        System.out.println(promptsMap.get(PresenterPrompts.pressEnterToContinue));
    }

    public void noStudyToolToEditSorrier() {
        System.out.println(promptsMap.get(PresenterPrompts.noStudyToolToEditSorrier));
    }

    public void noStudyToolToStudySorrier() {
        System.out.println(promptsMap.get(PresenterPrompts.noStudyToolToStudySorrier));
    }

    public void noTemplateToChooseSorrier() {
        System.out.println(promptsMap.get(PresenterPrompts.noTemplateToChooseSorrier));
    }

    public void quizSavedReporter() {
        System.out.println(promptsMap.get(PresenterPrompts.quizSaved));
    }

    public void friendAddFailed() {
        System.out.println(promptsMap.get(PresenterPrompts.friendAddFailed));
    }

    public void friendAddSucceeded() {
        System.out.println(promptsMap.get(PresenterPrompts.friendAddSucceeded));
    }

    public void studySessionEndedReporter(String score) {
        System.out.println(promptsMap.get(PresenterPrompts.scoreReporter) + score + ".");
        System.out.println(promptsMap.get(PresenterPrompts.studySessionEndedReporter));
    }

    public void getAnswerPrompter(TemplateManager.TemplateType templateType) {
        switch (templateType) {
            case FC:
                System.out.println(promptsMap.get(PresenterPrompts.getFCAnswerPrompt));
                break;
            case MCQ:
                System.out.println(promptsMap.get(PresenterPrompts.getMCAnswerPrompt));
                break;
            case SOR:
                System.out.println(promptsMap.get(PresenterPrompts.getSORAnswerPrompt));
                break;
            default:
                System.out.println(promptsMap.get(PresenterPrompts.getAnswerPrompt));
        }
        System.out.println(promptsMap.get(PresenterPrompts.getAnswerPrompt));
    }

    public void pressEnterToShowAnswer() {
        System.out.println(promptsMap.get(PresenterPrompts.pressEnterToShowAnswer));
    }

    public void correctnessReporter(Boolean correctness) {
        if (correctness) {
            System.out.println(promptsMap.get(PresenterPrompts.correctReporter));
        } else {
            System.out.println(promptsMap.get(PresenterPrompts.incorrectReporter));
        }
    }

    public void illegalAnswerReporter() {
        System.out.println(promptsMap.get(PresenterPrompts.illegalAnswerReporter));
    }

    public void collaborateStudyToolIDWithQuitPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.collaborateStudyToolIDPrompt));
    }

    public void collaboratorNameWithQuitPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.collaboratorNameWithQuitPrompt));
    }

    public void userDoesNotExistSorrier() {
        System.out.println(promptsMap.get(PresenterPrompts.logInUserNotExist));
    }

    public void sayCollaboratorAdded() {
        System.out.println(promptsMap.get(PresenterPrompts.sayCollaboratorAdded));
    }

    public void collabMenu() {
        System.out.println(promptsMap.get(PresenterPrompts.collabMenu));
    }

    public void sayCollaboratorRemoved() {
        System.out.println(promptsMap.get(PresenterPrompts.sayCollaboratorRemoved));
    }

    public void sayInvalidInput() {
        System.out.println(promptsMap.get(PresenterPrompts.invalidInputReporter));
    }

    // deleting study tools
    public void selectStudyToolToDeletePrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.chooseStudyToolToDeletePrompt));
    }
    public void sayNoStudyToolToDelete() {
        System.out.println(promptsMap.get(PresenterPrompts.noStudyToolToDeleteSorrier));
    }

    // undeleting study tools
    public void selectStudyToolToUndeletePrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.chooseStudyToolToUndeletePrompt));
    }
    public void sayNoStudyToolToUndelete() {
        System.out.println(promptsMap.get(PresenterPrompts.noStudyToolToUndeleteSorrier));
    }

    public void revertVersionWithQuitPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.revertVersionWithQuitPrompt));
    }

    public void sayStudyToolVersionReverted() {
        System.out.println(promptsMap.get(PresenterPrompts.sayStudyToolVersionReverted));
    }

    public void ranOutOfTimeReporter(){
        System.out.println(promptsMap.get(PresenterPrompts.ranOutOfTime));
    }

}

