package templates;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An instance of this class represents a template for a study tool.
 */
public abstract class Template implements Serializable {

    private final int id;
    private static int idCounter = 0;

    private String name;
    private boolean isTimed;
    private boolean timedPerQuestionNotQuiz; // true: per question, false: entire quiz
    private int timeLimit;
    private boolean isScored;

    /**
     * A name, whether the Template is timed, whether it is timed per Question or Quiz, a time limit, if it is
     * scored are required to create an instance of Template.
     * @param info A list of Strings necessary to initialize the Template in the format
     *             [name, isTimed, timedPerQuestionNotQuiz, timeLimit, isScored]
     */
    Template(List info) {
        this.id = idCounter;
        idCounter++;
        this.name = (String)info.get(0); // TODO: try - catch for errors!
        this.isTimed = Boolean.parseBoolean((String)info.get(1));
        this.timedPerQuestionNotQuiz = Boolean.parseBoolean((String)info.get(2));
        this.timeLimit = Integer.parseInt((String)info.get(3));
        this.isScored = Boolean.parseBoolean((String)info.get(4));
    }

    /**
     * This method updates the ID counter for Template identification when loading the program on start.
     * @param idCounter Static tracker to ensure no Templates have the same ID.
     */
    static void setIdCounter(int idCounter) {
        Template.idCounter = idCounter;
    }

    /**
     * Returns the id of the Template.
     * @return id
     */
    int getID() {
        return this.id;
    }

    /**
     * Returns the name of the Template.
     * @return name
     */
    String getName(){
        return this.name;
    }

    boolean getIsTimed(){
        return this.isTimed;
    }

    boolean getIsTimedPerQuestion(){
        return this.timedPerQuestionNotQuiz;
    }

    int getTimeLimit(){
        return this.timeLimit;
    }

    /**
     * This method updates the name of the Template.
     * @param name The name of this Template.
     */
    void setName(String name){
        this.name = name;
    }

    /**
     * This method updates whether the Template is timed or not.
     * @param isTimed Whether this Template is timed or not.
     */
    void setIsTimed(boolean isTimed) {
        this.isTimed = isTimed;
    }

    /**
     * This method updates whether the Template is timed per question or per quiz.
     * @param timedPerQuestionNotQuiz Whether this Template is timed per question or per quiz.
     */
    void setTimedPerQuestionNotQuiz(boolean timedPerQuestionNotQuiz) {
        this.timedPerQuestionNotQuiz = timedPerQuestionNotQuiz;
    }

    /**
     * This method updates the time limit of this Template.
     * @param timeLimit The time limit of this Template.
     */
    void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * This method updates whether the Template is scored or not.
     * @param isScored Whether this Template is scored or not.
     */
    void setIsScored(boolean isScored) {
        this.isScored = isScored;
    }


    /**
     * This method converts the Template to a string.
     * @return A string representation of this Template.
     */
    @Override
    public String toString() {
        return "Name: " + this.name + "\nTemplate Type: " + this.getClass().toString() + "\n\n";
    }

    /**
     * Returns the configuration of the Template of format (item : configuration).
     * @return the configuration of the Template of format (item : configuration).
     *      * eg. ("name": "default_template"), ("isTimed": "true"), ("timeLimit": "20")
     */
    Map<String, String> getConfiguration() {
        Map<String, String> retMap = new HashMap<>();
        retMap.put("name", name);
        retMap.put("id", String.valueOf(id));
        retMap.put("isTimed", String.valueOf(isTimed));
        retMap.put("timedPerQuestionNotQuiz", String.valueOf(timedPerQuestionNotQuiz));
        retMap.put("timeLimit", String.valueOf(timeLimit));
        retMap.put("isScored", String.valueOf(isScored));
        return retMap;
    }
}