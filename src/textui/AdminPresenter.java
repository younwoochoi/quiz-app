package textui;

import java.util.Map;

/**
 * Displays admin user related functionalities. i.e. managing templates, users etc.
 */
public class AdminPresenter {
    Map<PresenterPrompts, String> promptsMap;

    public AdminPresenter(Map<PresenterPrompts, String> promptsMap) {
        this.promptsMap = promptsMap;
    }

    public void mainMenu() {
        System.out.println(promptsMap.get(PresenterPrompts.adminMainMenuDisplay));
        System.out.println();
        System.out.println(promptsMap.get(PresenterPrompts.chooseOptionPrompt));
    }

    // generic prompts
    public void sayInvalidInput() {
        System.out.println(promptsMap.get(PresenterPrompts.invalidInputReporter));
    }
    public void sayEdited() {
        System.out.println(promptsMap.get(PresenterPrompts.templateEditedReporter));
    }

    public void templateMenu() {
        System.out.println(promptsMap.get(PresenterPrompts.templateMenu));
        System.out.println();
        System.out.println(promptsMap.get(PresenterPrompts.chooseOptionPrompt));
    }
    // editing/creating templates
    public void selectTemplatePrompt() {
        System.out.print(promptsMap.get(PresenterPrompts.chooseTemplatePrompt));
    }
    public void editTemplatePrompt(String templateType) {
        if (templateType.equals("FC")) {
            System.out.println(promptsMap.get(PresenterPrompts.editTemplateMenuFC));
        } else if (templateType.equals("MCQ") || templateType.equals("EAQ")) {
            System.out.println(promptsMap.get(PresenterPrompts.editTemplateMenuMCQ));
        } else if (templateType.equals("SOR")) {
            System.out.println(promptsMap.get(PresenterPrompts.editTemplateMenuSOR));
        }
        System.out.println(promptsMap.get(PresenterPrompts.chooseOptionPrompt));
    }
    public void editNamePrompt() {
        System.out.print(promptsMap.get(PresenterPrompts.editNameGenericPrompt));
    }
    public void sayTemplateDeleted() {
        System.out.println(promptsMap.get(PresenterPrompts.templateDeletedReporter));
    }
    // template specific
    public void editWordsReappearPrompt() {
        System.out.print(promptsMap.get(PresenterPrompts.templateEditorunfamiliarFCReappear));
    }
    public void editIsScoredPrompt() {
        System.out.print(promptsMap.get(PresenterPrompts.templateEditortemplateScored));
    }
    public void editNumCategories() {
        System.out.print(promptsMap.get(PresenterPrompts.templateEditorTemplateSORNumCategories));
    }
    // editing time
    public void editTimePrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.templateEditorTemplateTimedMenu));
        System.out.println();
        System.out.println(promptsMap.get(PresenterPrompts.chooseOptionPrompt));
    }
    public void editIsTimedPrompt() {
        System.out.print(promptsMap.get(PresenterPrompts.templateEditorTemplateTimed));
    }
    public void editTimedPerQuestionNotQuizPrompt() {
        System.out.print(promptsMap.get(PresenterPrompts.templateEditorTemplateTimedPerQuestion));
    }
    public void editTimeLimitPrompt() {
        System.out.print(promptsMap.get(PresenterPrompts.templateEditorTemplateTimeLimit));
    }
    //creating templates
    public void createTemplatePrompt(){
        System.out.println(promptsMap.get(PresenterPrompts.createTemplateMenu));
        System.out.println(promptsMap.get(PresenterPrompts.chooseOptionPrompt));
    }
    public void setNamePrompt() {
        System.out.print(promptsMap.get(PresenterPrompts.templateCreatorname));
    }
    public void sayFCCreated(int tid) {
        System.out.println(promptsMap.get(PresenterPrompts.templateCreatorFCCreatedReporter) + tid + "\n");
    }
    public void sayEAQCreated(int tid) {
        System.out.println(promptsMap.get(PresenterPrompts.templateCreatorEAQCreatedReporter) + tid + "\n");
    }
    public void sayMCQCreated(int tid) {
        System.out.println(promptsMap.get(PresenterPrompts.templateCreatorMCQCreatedReporter) + tid + "\n");
    }
    public void saySORCreated(int tid){
        System.out.println(promptsMap.get(PresenterPrompts.templateCreatorSORCreatedReporter) + tid + "\n");
    }

    // welcome message
    public void welcomerOrExiterPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.welcomerOrExiterPrompt));
    }
    public void currentWelcomePrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.introduceWelcomer));
        System.out.println(promptsMap.get(PresenterPrompts.welcomer));
        System.out.println(promptsMap.get(PresenterPrompts.chooseNewWelcomer));
    }
    public void currentExiterPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.introduceExiter));
        System.out.println(promptsMap.get(PresenterPrompts.logInExiter));
        System.out.println(promptsMap.get(PresenterPrompts.chooseNewExiter));
    }

    // edit admin key
    public void editAdminKeyPrompt(String currAdminKey) {
        System.out.println(promptsMap.get(PresenterPrompts.introduceAdminKey));
        System.out.println(currAdminKey);
        System.out.println(promptsMap.get(PresenterPrompts.editAdminKeyPrompt));
    }
    public void newAdminKeyPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.newAdminKeyPrompt));
    }

    public void userHandlingMenu() {
        System.out.println(promptsMap.get(PresenterPrompts.userHandlingMenu));
    }
    // suspending users
    public void selectUserToSuspend() {
        System.out.println(promptsMap.get(PresenterPrompts.chooseUserToSuspend));
    }
    public void selectNumberOfDaysToSuspend(String name) {
        System.out.println(promptsMap.get(PresenterPrompts.chooseNumberOfDaysToSuspend) + name + "?: ");
    }
    public void suspendAnotherUserPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.suspendAnotherUserPrompt));
    }
    public void saySuspensionSuccess() {
        System.out.println(promptsMap.get(PresenterPrompts.saySuspensionSuccess));
    }

    // freezing accounts
    public void freezeAccountsPrompt(boolean frozen, int numDays) {
        if (frozen) {
            System.out.print(promptsMap.get(PresenterPrompts.currentlyFrozen) + numDays + " ");
            System.out.println(promptsMap.get(PresenterPrompts.days));
        } else {
            System.out.println(promptsMap.get(PresenterPrompts.currentlyNotFrozen));
        }
        System.out.println(promptsMap.get(PresenterPrompts.changeFreezeStatusPrompt));
    }
    public void freezeAccountsForHowManyDaysPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.freezeAccountsForHowManyDaysPrompt));
    }
    public void sayProgramFrozen(int numDaysFrozen) {
        System.out.print(promptsMap.get(PresenterPrompts.sayProgramFrozenFor));
        System.out.print(numDaysFrozen + " ");
        System.out.println(promptsMap.get(PresenterPrompts.days));
        System.out.println();
    }
}