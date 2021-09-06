package templates;

import filereadwriter.IGateway;
import filereadwriter.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An instance of this class represents a template manager to handle templates for a study tool.
 */
public class TemplateManager implements Serializable {

    private HashMap<Integer, Template> idMapper;
    private IGateway iGateway = new Serializer();
    private TemplateFactory templateFactory;

    public enum TemplateType {
        FC {
            public String toString() {
                return "FC";
            }
        },
        MCQ {
            public String toString() {
                return "MCQ";
            }
        },
        EAQ {
            public String toString() {
                return "EAQ";
            }
        },
        SOR {
            public String toString(){
                return "SOR";
            }
        }
    }

    // constructor(s)
    /**
     * Create an instance of TemplateManager.
     */
    public TemplateManager() {
        this.idMapper = new HashMap<>();
        this.templateFactory = new TemplateFactory();
    }

    public void loadFromMaps(HashMap<Integer, Template> idMapper) {
        this.idMapper = idMapper;
        updateTemplateCount();
    }

    // methods
    /**
     * Returns a string representation of the TemplateManager.
     * @return A string representation of the TemplateManager.
     */
    @Override
    public String toString() {
        return "TemplateManager{" +
                "idMapper=" + idMapper + '}';
    }
    /**
     * Pass important information about the TemplateManager instance to the iGateway.
     * @param filePath
     */
    public void saveToSer(String filePath) throws IOException {
        this.iGateway.serializeObject(this.idMapper, filePath);
    }
    /**
     * Returns whether the TemplateManager is empty or not.
     * @return If the TemplateManager has no templates.
     */
    public boolean isEmpty() {
        return idMapper.isEmpty();
    }
    /**
     * Returns whether id is a valid ID of a Template in TemplateManager.
     * @param id An integer that may or may not be the ID of a Template.
     * @return If id is an id of an existing Template in the TemplateManager.
     */

    public boolean validId (String id) {
        for (Integer x : idMapper.keySet()) {
            if (String.valueOf(x.intValue()).equals(id)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Returns the template type of the Template with the given id.
     * @param id The Template ID.
     * @return The TemplateType of the Template.
     */

    public TemplateManager.TemplateType getTemplateType(int id) {
        Map<String, String> t = this.getTemplate(id);

        if (t.get("type").equals("FC")) {
            return TemplateManager.TemplateType.FC;
        } else if (t.get("type").equals("EAQ")) {
            return TemplateManager.TemplateType.EAQ;
        } else if (t.get("type").equals("SOR")){
            return TemplateManager.TemplateType.SOR;
        }
        else {
            return TemplateManager.TemplateType.MCQ;
        }
    }

    /**
     * A name, whether the Template is timed, whether it is timed per Question or Quiz, a time limit, if it is
     * scored and if words reappear are required to create an instance of FlashCardTemplate.
     * @param templateType The type of the Template.
     * @param templateInfo Information for the specific Template to be created in the format
     *                     [name,isTimed,timedPerQuestionNotQuiz,timeLimit,isScored] for MCQ, EAQ,
     *                     [name,isTimed,timedPerQuestionNotQuiz,timeLimit,isScored,wordsReappear] for FC,
     *                     [name,isTimed,timedPerQuestionNotQuiz,timeLimit,isScored,numCategories] for SOR.
     * @return id of the created Template.
     */
    public int createTemplate(TemplateManager.TemplateType templateType, List templateInfo) {
        Template template = this.templateFactory.getTemplate(templateType.toString(), templateInfo);
        idMapper.put(template.getID(), template);
        return template.getID();
    }
    /**
     * Deletes the Template of id from the TemplateManager.
     * @param id The Template ID.
     */
    public void deleteTemplate(int id){
        idMapper.remove(id);
    }

    /**
     * Changes the name of the Template with id to name.
     * @param id The Template ID.
     * @param name The new Template name.
     */
    public void editTemplateName(int id, String name){
        idMapper.get(id).setName(name);
    }
    /**
     * Changes whether the Template, with id, is scored to isScored.
     * @param id The Template ID.
     * @param isScored If the Template is scored.
     */
    public void editIsScored(int id, boolean isScored) {
        this.idMapper.get(id).setIsScored(isScored);
    }
    /**
     * Changes whether the Template, with id, is timed to isTimed.
     * @param id The Template ID.
     * @param isTimed If the Template is timed.
     */
    public void editIsTimed(int id, boolean isTimed) {
        this.idMapper.get(id).setIsTimed(isTimed);
    }
    /**
     * Changes whether the Template, with id, is timed per question, not quiz to timedPerQuestionNotQuiz.
     * @param id The Template ID.
     * @param timedPerQuestionNotQuiz If the Template is timed per question, not quiz.
     */
    public void editTimedPerQuestionNotQuiz(int id, boolean timedPerQuestionNotQuiz) {
        this.idMapper.get(id).setTimedPerQuestionNotQuiz(timedPerQuestionNotQuiz);
    }
    /**
     * Changes the time limit of the Template with id to timeLimit.
     * @param id The Template ID.
     * @param timeLimit The new time limit for the Template.
     */
    public void editTimeLimit(int id, int timeLimit) {
        this.idMapper.get(id).setTimeLimit(timeLimit);
    }
    /**
     * Changes whether the Template, with id, has unfamiliar words reappear as flash cards to wordsReappear.
     * @param id The Template ID.
     * @param wordsReappear If unfamiliar words reappear.
     */
    public void editWordsReappear(int id, boolean wordsReappear) {
        ((FlashCardTemplate)this.idMapper.get(id)).setWordsReappear(wordsReappear);
    }
    /**
     * Changes whether the Template, with id, has unfamiliar words reappear as flash cards to wordsReappear.
     * @param id The Template ID.
     * @param numCategories The number of categories words are sorted into.
     */
    public void editNumCategories(int id, int numCategories) {
        ((SortingTemplate)this.idMapper.get(id)).setNumCategories(numCategories);
    }

    /**
     * Returns all templates of the form ArrayList<{configs}.
     * @return An ArrayList of all Template configurations.
     */
    public List<Map<String, String>> getTemplates() {
        List<Map<String, String>> templates = new ArrayList<>();
        for (Template t : this.idMapper.values()) {
            templates.add(t.getConfiguration());
        }
        return templates;
    }
    /**
     * Returns the template of tID of the form {configs}.
     * @param tID The Template ID.
     * @return An ArrayList of the Template configuration.
     */

    public Map<String, String> getTemplate(int tID) {
        for (Template t : this.idMapper.values()) {
            if (t.getID() == tID) {
                return t.getConfiguration();
            }
        }
        return new HashMap<>();
    }


    /**
     * Returns whether the Template is scored or not.
     * @param tID The Template ID.
     * @return Whether the Template is scored or not.
     */ public boolean isTemplateScored(String tID) {
        return getTemplate(Integer.parseInt(tID)).get("isScored").equals("true");
    }

    /**
     * Returns isTimed, isTimedPerQuestion, and timeLimit
     */
    public List<Object> getTimeInfo (String tID) {
        Template template = idMapper.get(Integer.parseInt(tID));
        boolean isTimed = template.getIsTimed();
        boolean isTimedPerQuestion = template.getIsTimedPerQuestion();
        int timeLimit = template.getTimeLimit();
        List<Object> timeInfo = new ArrayList<>();
        timeInfo.add(isTimed);
        timeInfo.add(isTimedPerQuestion);
        timeInfo.add(timeLimit);
        return timeInfo;
    }

    /**
     * Updates the template count for when loading the program from start to ensure all Templates
     * are being identified correctly.
     */

    private void updateTemplateCount() {
        int highestCount = 0;
        for (int id : idMapper.keySet()) {
            if (id > highestCount) highestCount = id;
        }
        Template.setIdCounter(highestCount + 1);
    }

}
