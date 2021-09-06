package studytools;

import answercheckers.AnswerChecker;
import answercheckers.AnswerCheckerFactory;
import filereadwriter.IGateway;
import filereadwriter.Serializer;
import templates.TemplateManager;
import users.UserManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Stores study tools and allows finding study tools by name, by author, and by publicity.
 * It also allows the creation and edition of study tools.
 */
public class StudyToolManager implements Serializable {

    /* Attributes:
        allStudyTools: maps each study tool to accessibility
        idMapper: maps each id to a study tool
     */

    private HashMap<StudyTool, Accessibility> allStudyTools;
    private HashMap<StudyTool, Accessibility> deletedStudyToolsAccessibility;
    private Map<String, StudyTool> idMapper;
    private IGateway iGateway = new Serializer();

    public enum Accessibility implements Serializable{
        PUBLIC,
        PRIVATE,
        FRIEND_ONLY,
        DELETED
    }

    public StudyToolManager() {
        this.allStudyTools = new HashMap<>();
        this.idMapper = new HashMap<>();
        this.deletedStudyToolsAccessibility = new HashMap<>();
    }

    public void loadFromMaps(HashMap<StudyTool, Accessibility> allStudyTools, HashMap<StudyTool, Accessibility> deletedStudyToolsAccessibility) {
        this.allStudyTools = allStudyTools;
        this.deletedStudyToolsAccessibility = deletedStudyToolsAccessibility;
        inferIdMapperFromStudyTools();
    }

    @Override
    public String toString() {
        return "StudyToolManager{" +
                "allStudyTools=" + allStudyTools +
                ", idMapper=" + idMapper +
                '}';
    }

    /**
     * Creates study tool and stores it in the current study tool manager
     * @return the study tool just created
     */
    public StudyTool createStudyTool(List<String> prompts, List<String> answers, Map info) {
        info.put("id", String.valueOf(this.allStudyTools.size()+1));

        StudyTool studyTool = new StudyTool(prompts, answers, info);
        addStudyTool(studyTool, info.get("accessibility").toString());
        return studyTool;
    }

    public void changeAccessibility(String id, String desiredAccessibility) {
        StudyTool studyTool = getStudyToolByID(id);
        if (desiredAccessibility.equalsIgnoreCase("public")) allStudyTools.put(studyTool, Accessibility.PUBLIC);
        else if (desiredAccessibility.equalsIgnoreCase("friend")) allStudyTools.put(studyTool, Accessibility.FRIEND_ONLY);
        else allStudyTools.put(studyTool, Accessibility.PRIVATE);
    }

    public void deleteStudyTool(String id) {
        StudyTool studyTool = getStudyToolByID(id);
        deletedStudyToolsAccessibility.put(studyTool, allStudyTools.get(studyTool));
        allStudyTools.put(studyTool, Accessibility.DELETED);
    }

    public void revertDeletedStudyTool(String id) {
        StudyTool studyTool = getStudyToolByID(id);
        allStudyTools.put(studyTool, deletedStudyToolsAccessibility.get(studyTool));
        deletedStudyToolsAccessibility.remove(studyTool);
    }

    public boolean isStudyToolDeleted(String id){
        StudyTool studyTool = getStudyToolByID(id);
        return deletedStudyToolsAccessibility.containsKey(studyTool);
    }

    public void replaceStudyTool(StudyTool oldStudyTool, StudyTool newStudyTool) {
        if (this.allStudyTools.containsKey(oldStudyTool)) {
            this.allStudyTools.put(newStudyTool, this.allStudyTools.get(oldStudyTool));
            this.allStudyTools.remove(oldStudyTool);
            this.idMapper.put(oldStudyTool.getIdentifier(), newStudyTool);
            if (deletedStudyToolsAccessibility.containsKey(oldStudyTool)) {
                this.deletedStudyToolsAccessibility.put(newStudyTool, this.deletedStudyToolsAccessibility.get(oldStudyTool));
                this.deletedStudyToolsAccessibility.remove(oldStudyTool);
            }
        }
        else throw new IllegalArgumentException("No such study tool exist");
    }

    /**
     * Return questions and answers of the form Array({question, answer})
     */
    public String[][] fetchQuestionsAndAnswers(String id) {
        StudyTool s = getStudyToolByID(id);
        return s.getQuestionsAndAnswers();
    }


    /**
     * Edits a single question
     * @param Id study tool's id
     * @param index the index of question, counting from 0
     * @return if successfully edited or not
     */
     public boolean editSingleQuestion(String Id, int index, String newPrompt, String newAnswer) {
        StudyTool s = getStudyToolByID(Id);
        return s.editQuestion(index, newPrompt, newAnswer);
    }

    /**
     * Verifies if a study tool is visible to a user
     * visible = ((user is owner) OR (is public study tool)) AND (is not frozen)
     */
    public boolean verifyVisibility(String id, String userId){
        try {
            return (belongsToAuthor(id,userId) ||
                    allStudyTools.get(getStudyToolByID(id)).equals(Accessibility.PUBLIC)) &&
                    !getStudyToolByID(id).isFrozen(); // TODO: check
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void saveToSer(String studyToolFilePath, String deletedStudyToolFilePath) throws IOException {
        this.iGateway.serializeObject(this.allStudyTools, studyToolFilePath);
        this.iGateway.serializeObject(this.deletedStudyToolsAccessibility, deletedStudyToolFilePath);
    }

    /**
     * @return visible study tools of the form: List(Map)
     * visible = ((user is owner) OR (is public study tool)) AND (is not frozen)
     * each map labels: {name, id, accessibility, author's id}
     */
    public List<Map<String, String>> getVisibleStudyTools(String userId, UserManager userManager) {
        List<Map<String, String>> retArray = new ArrayList<>();
        for (StudyTool studyTool : allStudyTools.keySet()) {
            String username = userManager.findUserNameByID(userId);
            String authorName = userManager.findUserNameByID(studyTool.getAuthorId());
            boolean isFriendVisible = allStudyTools.get(studyTool).equals(Accessibility.FRIEND_ONLY) &&
                    userManager.isFriend(authorName, username);
            if ((allStudyTools.get(studyTool).equals(Accessibility.PUBLIC) || isFriendVisible) && !studyTool.isFrozen()) {
                retArray.add(describeStudyToolbyId(String.valueOf(studyTool.getIdentifier())));
            }
        }
        return retArray;
    }

    public List<Map<String, String>> getAllStudyTools() {
        List<Map<String, String>> retArray = new ArrayList<>();
        for (StudyTool s : allStudyTools.keySet()) {
            retArray.add(describeStudyToolbyId(String.valueOf(s.getIdentifier())));
        }
        return retArray;
    }

    public List<Map<String, String>> getStudyToolsByAuthorId(String authorId) {
        /*
        Returns all undeleted study tools made by this author of the form List({name, id, accessibility, author's id})
         */
        List<Map<String, String>> retArray = new ArrayList<>();
        for (StudyTool s : allStudyTools.keySet()) {
            if (s.getAuthorId().equals(authorId) && !allStudyTools.get(s).equals(Accessibility.DELETED))
                retArray.add(describeStudyToolbyId(String.valueOf(s.getIdentifier())));
        }
        return retArray;
    }

    public List<Map<String, String>> getDeletedStudyToolsByAuthorId(String authorId) {
        /*
        Returns all deleted study tools made by this author of the form List({name, id, accessibility, author's id})
         */
        List<Map<String, String>> retArray = new ArrayList<>();
        for (StudyTool s : allStudyTools.keySet()) {
            if (s.getAuthorId().equals(authorId) && allStudyTools.get(s).equals(Accessibility.DELETED))
                retArray.add(describeStudyToolbyId(String.valueOf(s.getIdentifier())));
        }
        return retArray;
    }

    public String getStudyToolTemplateId(String studyToolId){
        return getStudyToolByID(studyToolId).getTemplateId();
    }

    /**
    Describe a certain study tool of the form {tag: item}, for instance {"name": "StudyTool1"}
    Tags: "name", "Id", "accessibility", "authorId"
    **/
    public Map<String, String> describeStudyToolbyId(String Id) {
        StudyTool s = getStudyToolByID(Id);
        Map<String, String> retMap= new HashMap<>();
        retMap.put("name", s.getName());
        retMap.put("Id", String.valueOf(s.getIdentifier()));
        retMap.put("accessibility", allStudyTools.get(s).name());
        retMap.put("authorId", s.getAuthorId());
        return retMap;
    }

    public boolean belongsToAuthor(String id, String authorId) {
        return this.idMapper.containsKey(id) && this.idMapper.get(id).getAuthorId().equals(authorId);
    }

    public boolean studyToolExists(String studyToolId) {
        return this.idMapper.containsKey(studyToolId);
    }

    public void addQuestionAndAnswer(String id, String q, String a) {
        getStudyToolByID(id).addQuestionAnswer(q, a);
    }

    public void freezeStudyToolsForUser(String userID) {
        setFrozenStatusStudyToolsForUser(userID, true);
    }
    public void unfreezeStudyToolsForUser(String userID) {
        setFrozenStatusStudyToolsForUser(userID, false);
    }

    public StudyTool getStudyToolByID(String id) throws IllegalArgumentException{
         if (this.studyToolExists(id)) return this.idMapper.get(id);
         else throw new IllegalArgumentException("Study tool does not exist");
    }

    public AnswerChecker getAnswerChecker(String id) {
         return this.getStudyToolByID(id).getAnswerChecker();
    }

    //===============helper methods=========================
    private List<StudyTool> getStudyToolsByAuthorIdHelper(String authorId) {
        List<StudyTool> retArray = new ArrayList<>();
        for (StudyTool studyTool : allStudyTools.keySet()) {
            if (studyTool.getAuthorId().equals(authorId))
                retArray.add(studyTool);
        }
        return retArray;
    }
    private void setFrozenStatusStudyToolsForUser(String userID, boolean newStatus) {
        List<StudyTool> studyTools = getStudyToolsByAuthorIdHelper(userID);
        for (StudyTool studyTool : studyTools) {
            studyTool.setFrozenStatus(newStatus);
        }
    }

    private void inferIdMapperFromStudyTools() {
        for (StudyTool studyTool : this.allStudyTools.keySet()) {
            this.idMapper.put(String.valueOf(studyTool.getIdentifier()), studyTool);
        }
    }

    private void addStudyTool(StudyTool studyTool, String accessibility){
        this.idMapper.put(String.valueOf(studyTool.getIdentifier()), studyTool);
        if (accessibility.equals("public")) allStudyTools.put(studyTool, Accessibility.PUBLIC);
        else if (accessibility.equals("friend")) allStudyTools.put(studyTool, Accessibility.FRIEND_ONLY);
        else allStudyTools.put(studyTool, Accessibility.PRIVATE);
    }
}
