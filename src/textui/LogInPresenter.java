package textui;

import users.UserManager;

import java.util.Map;

/**
 * Displays logging in and quitting functionalities.
 */
public class LogInPresenter {
    Map<PresenterPrompts, String> promptsMap;

    public LogInPresenter(Map<PresenterPrompts, String> promptsMap) {
        this.promptsMap = promptsMap;
    }

    public void welcomer() {
        System.out.println(promptsMap.get(PresenterPrompts.welcomer));
    }

    public void lLogInUsernamePrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInUserNamePrompt));
    }
    public void lLogInPasswordPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInPasswordPrompt));
    }

    public void lCreateAccountOrLoginPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInMenu));
        System.out.println(promptsMap.get(PresenterPrompts.chooseOptionPrompt));
    }

    public void lAdminKeyPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInAdminKeyPrompt));
    }
    public void lSayIncorrectKey() {
        System.out.println(promptsMap.get(PresenterPrompts.logInIncorrectAdminKey));
    }
    public void lCreateAccountError() {
        System.out.println(promptsMap.get(PresenterPrompts.logInCreateAccountError));
    }
    public void lCreateAccountUsernamePrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInCreateUserNamePrompt));
    }
    public void lCreateAccountPasswordPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInCreatePasswordPrompt));
    }
    public void lCreateAccountEmailPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInCreateEmailPrompt));
    }
    public void lUsernameResetPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInUsernameResetPrompt));
    }
    public void lResetAccountPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInResetAccountPrompt));
    }
    public void lProvideNewPinPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInProvideNewPinPrompt));
    }
    public void lInvalidPinPrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInInvalidPinPrompt));
    }



    public void lSayUsernameNotUnique() {
        System.out.println(promptsMap.get(PresenterPrompts.logInSayUsernameNotUnique));
    }

    public void lSaySuccessfulAccountCreation() {
        System.out.println(promptsMap.get(PresenterPrompts.logInSaySuccessfulAccountCreation));
    }

    public void lSayInvalidInput() {
        System.out.println(promptsMap.get(PresenterPrompts.invalidInputReporter));
    }

    public void lTrialNamePrompt() {
        System.out.println(promptsMap.get(PresenterPrompts.logInTrialNamePrompt));
    }

    public void lSayTrialStarted(String username) {
        System.out.println(promptsMap.get(PresenterPrompts.logInSayTrialStarted) + username + "!\n");
    }

    public void lSayLoginSuccess() {
        System.out.println(promptsMap.get(PresenterPrompts.logInSayLoginSuccess));
    }
    public void lSayPasswordChangeSuccess() {
        System.out.println(promptsMap.get(PresenterPrompts.logInSayPasswordChangeSuccess));
    }


    public void lSayLoginError() {
        System.out.println(promptsMap.get(PresenterPrompts.logInSayLoginError));
    }

    public void displayUserInfo(String userName, String userID, UserManager.UserType userType) {
        System.out.println(promptsMap.get(PresenterPrompts.logInSayUserName) + userName + ". " +
                promptsMap.get(PresenterPrompts.logInSayUserID) + userID +
                ". " + promptsMap.get(PresenterPrompts.logInSayUserType) + userType.name() + ".\n");
    }

    public void saveOrNotPrompter() {
        System.out.println(promptsMap.get(PresenterPrompts.logInSaveOrNotPrompt));
    }

    public void exiter() {
        System.out.println(promptsMap.get(PresenterPrompts.logInExiter));
    }
//    public void exiter(String userName) {
//        System.out.println(promptsMap.get(PresenterPrompts.logInExiter) + userName + ". ");
//    }

    public void typeOfAccountMenu() {
        System.out.println(promptsMap.get(PresenterPrompts.logInTypeOfAccountMenu));
    }

    public void sayUserNameNotExist() {
        System.out.println(promptsMap.get(PresenterPrompts.logInUserNotExist));
    }
    public void sayAccountSuspended() {
        System.out.println(promptsMap.get(PresenterPrompts.logInAccountSuspended));
    }
}
