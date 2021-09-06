package filereadwriter;

import managementsystems.UserHandlingManagementSystem;
import studytools.CollabManager;
import studytools.StudyTool;
import studytools.StudyToolHistory;
import studytools.StudyToolManager;
import templates.Template;
import templates.TemplateManager;
import users.User;
import users.UserManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Deals with loading/saving .ser files
 */
public class Serializer implements IGateway, PropertyChangeListener {

    String directory = "";

    // save paths
    String userFilePath = directory + "savefiles/Users.ser";
    String userDateFilePath = directory + "savefiles/UserDates.ser";
    String userFriendFilePath = directory + "savefiles/UserFriends.ser";
    String templateFilePath = directory + "savefiles/Templates.ser";
    String studyToolFilePath = directory + "savefiles/StudyTools.ser";
    String historyFilePath = directory + "savefiles/History.ser";
    String invitationFilePath = directory + "savefiles/Invitation.ser";
    String userHandlingFilePath = directory + "savefiles/UserHandling.ser";
    String deletedStudyToolsFilePath = directory + "savefiles/DeletedStudyTools.ser";

    // default paths
    String defaultUserFilePath = directory + "savefiles/defaultUsers.ser";
    String defaultTemplateFilePath = directory + "savefiles/defaultTemplates.ser";
    String defaultStudyToolFilePath = directory + "savefiles/defaultStudyTools.ser";
    String defaultUserDateFilePath = directory + "savefiles/defaultUserDates.ser";
    String defaultUserFriendFilePath = directory + "savefiles/defaultUserFriends.ser";
    String defaultHistoryFilePath = directory + "savefiles/defaultHistory.ser";
    String defaultInvitationFilePath = directory + "savefiles/defaultInvitation.ser";
    String defaultUserHandlingFilePath = directory + "savefiles/defaultUserHandling.ser";
    String defaultDeletedStudyToolsFilePath = directory + "savefiles/DefaultDeletedStudyTools.ser";

    UserManager userManager;
    TemplateManager templateManager;
    StudyToolManager studyToolManager;
    CollabManager collabManager;
    UserHandlingManagementSystem userHandlingManagementSystem;

    public Serializer() {}

    public Serializer(UserManager userManager, TemplateManager templateManager, StudyToolManager studyToolManager,
                      CollabManager collabManager, UserHandlingManagementSystem userHandlingManagementSystem) {
        this.userManager = userManager;
        this.templateManager = templateManager;
        this.studyToolManager = studyToolManager;
        this.collabManager = collabManager;
        this.userHandlingManagementSystem = userHandlingManagementSystem;
    }

    @Override
    public void serializeObject(Serializable object, String filename) throws IOException {
        OutputStream file = new FileOutputStream(filename);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput out = new ObjectOutputStream(buffer);

        out.writeObject(object);

        out.close();
    }

    @Override
    public Object loadFromSerializedObject(String filePath) throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream(filePath);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        Object o = input.readObject();
        input.close();
        return o;
    }

    public void saveUsersOnly(UserManager userManager) {
        try {
            userManager.saveToSer(userFilePath, userDateFilePath, userFriendFilePath);
            System.out.println("Save successful");
        } catch (IOException e) {
            System.out.println("Save failed. ");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("load")) {
            loadAllManagers();
        }
        else if (evt.getPropertyName().equalsIgnoreCase("save")) {
            saveEntireProgram();
        }
    }


    /**
     * Loads all managers from the file path, if one of the managers fail to load, load all from default
     */
    private void loadAllManagers() {
        try {
            loadUserManager();
            loadTemplateManager();
            loadStudyToolManager();
            loadCollabManager();
            loadUserHandlingManagementSystem();
        } catch (IOException | ClassNotFoundException e) {
            try {
                loadDefaultUserManager();
                loadDefaultTemplateManager();
                loadDefaultStudyToolManager();
                loadDefaultCollabManager();
                loadDefaultUserHandlingManagementSystem();
            } catch (IOException | ClassNotFoundException ignored) {
            }
        }
    }

    private void saveEntireProgram() {
        try {
            userManager.saveToSer(userFilePath, userDateFilePath, userFriendFilePath);
            templateManager.saveToSer(templateFilePath);
            studyToolManager.saveToSer(studyToolFilePath, deletedStudyToolsFilePath);
            collabManager.saveToSer(historyFilePath, invitationFilePath);
            userHandlingManagementSystem.saveToSer(userHandlingFilePath);
            System.out.println("Save successful. ");
        } catch (IOException e) {
            System.out.println("Save failed. ");
        }
    }

    // Manager loaders and default manager loaders
    private void loadUserManager() throws IOException, ClassNotFoundException {
        HashMap<User, UserManager.UserType> userDictionary = (HashMap<User, UserManager.UserType>) loadFromSerializedObject(userFilePath);
        HashMap<String, LocalDate[]> dateMapper = (HashMap<String, LocalDate[]>) loadFromSerializedObject(userDateFilePath);
        HashMap<String, HashSet<String>> friendMapper = (HashMap<String, HashSet<String>>) loadFromSerializedObject(userFriendFilePath);
        this.userManager.loadFromMaps(userDictionary, dateMapper, friendMapper);
    }

    private void loadDefaultUserManager() throws IOException, ClassNotFoundException {
        HashMap<User, UserManager.UserType> userDictionary = (HashMap<User, UserManager.UserType>) loadFromSerializedObject(defaultUserFilePath);
        HashMap<String, LocalDate[]> dateMapper = (HashMap<String, LocalDate[]>) loadFromSerializedObject(defaultUserDateFilePath);
        HashMap<String, HashSet<String>> friendMapper = (HashMap<String, HashSet<String>>) loadFromSerializedObject(defaultUserFriendFilePath);
        this.userManager.loadFromMaps(userDictionary, dateMapper, friendMapper);
    }

    private void loadTemplateManager() throws IOException, ClassNotFoundException {
        HashMap<Integer, Template> idMapper = (HashMap<Integer, Template>) loadFromSerializedObject(templateFilePath);
        this.templateManager.loadFromMaps(idMapper);
    }

    private void loadDefaultTemplateManager() throws IOException, ClassNotFoundException {
        HashMap<Integer, Template> idMapper = (HashMap<Integer, Template>) loadFromSerializedObject(defaultTemplateFilePath);
        this.templateManager.loadFromMaps(idMapper);
    }

    private void loadStudyToolManager() throws IOException, ClassNotFoundException {
        HashMap<StudyTool, StudyToolManager.Accessibility> allStudyTools = (HashMap<StudyTool, StudyToolManager.Accessibility>) loadFromSerializedObject(studyToolFilePath);
        HashMap<StudyTool, StudyToolManager.Accessibility> deletedStudyToolsAccessibility = (HashMap<StudyTool, StudyToolManager.Accessibility>) loadFromSerializedObject(deletedStudyToolsFilePath);
        this.studyToolManager.loadFromMaps(allStudyTools, deletedStudyToolsAccessibility);
    }

    private void loadDefaultStudyToolManager() throws IOException, ClassNotFoundException {
        HashMap<StudyTool, StudyToolManager.Accessibility> allStudyTools = (HashMap<StudyTool, StudyToolManager.Accessibility>) loadFromSerializedObject(defaultStudyToolFilePath);
        HashMap<StudyTool, StudyToolManager.Accessibility> deletedStudyToolsAccessibility = (HashMap<StudyTool, StudyToolManager.Accessibility>) loadFromSerializedObject(defaultDeletedStudyToolsFilePath);
        this.studyToolManager.loadFromMaps(allStudyTools, deletedStudyToolsAccessibility);
    }

    private void loadCollabManager() throws IOException, ClassNotFoundException {
        HashMap<String, StudyToolHistory> historyHashMap = (HashMap<String, StudyToolHistory>) loadFromSerializedObject(historyFilePath);
        HashMap<String, ArrayList<String>> invitationHashMap = (HashMap<String, ArrayList<String>>) loadFromSerializedObject(invitationFilePath);
        this.collabManager.loadFromMaps(historyHashMap, invitationHashMap);
    }

    private void loadDefaultCollabManager() throws IOException, ClassNotFoundException {
        HashMap<String, StudyToolHistory> historyHashMap = (HashMap<String, StudyToolHistory>) loadFromSerializedObject(defaultHistoryFilePath);
        HashMap<String, ArrayList<String>> invitationHashMap = (HashMap<String, ArrayList<String>>) loadFromSerializedObject(defaultInvitationFilePath);
        this.collabManager.loadFromMaps(historyHashMap, invitationHashMap);
    }

    private void loadUserHandlingManagementSystem() throws IOException, ClassNotFoundException {
        ArrayList<Object> userHandlingData = (ArrayList<Object>) loadFromSerializedObject(userHandlingFilePath);
        this.userHandlingManagementSystem.loadFromMaps(userHandlingData);
    }

    private void loadDefaultUserHandlingManagementSystem() throws IOException, ClassNotFoundException {
        ArrayList<Object> userHandlingData = (ArrayList<Object>) loadFromSerializedObject(defaultUserHandlingFilePath);
        this.userHandlingManagementSystem.loadFromMaps(userHandlingData);
    }
}
