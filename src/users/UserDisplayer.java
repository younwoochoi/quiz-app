package users;

import textui.PresenterPrompts;

import java.util.List;
import java.util.Map;

/**
 * Displays anything to do with users
 */
public class UserDisplayer {
    private Map<PresenterPrompts, String> promptsMap;

    public UserDisplayer(Map<PresenterPrompts, String> promptsMap) {
        this.promptsMap = promptsMap;
    }

    public void displayUser(Map<String, String> user) {
        System.out.println("{");
        for (String config : user.keySet()) {
            System.out.println(promptsMap.get(PresenterPrompts.valueOf(config)) + user.get(config));
        }
        System.out.println("}");
    }

    public void displayAllUsers(UserManager userManager, int numDaysFrozen) {
        System.out.println(promptsMap.get(PresenterPrompts.userDisplayerUsers));
        List<Map<String, String>> users = userManager.getUsers(numDaysFrozen);
        for (Map<String, String> user : users) {
            displayUser(user);
        }
        System.out.println();
    }
}
