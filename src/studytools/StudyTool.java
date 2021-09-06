package studytools;

import answercheckers.AnswerChecker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A study tool, exact type is determined by the template referred to by the template ID that it stores.
 */
public class StudyTool implements Serializable {
    private String name;
    private ArrayList<String> prompts;
    private ArrayList<String> answers;
    private final String templateId;
    private final String authorId;
    private final String identifier;
    private final AnswerChecker answerChecker;
    private boolean currentlyFrozen;

    StudyTool(List<String> prompts, List<String> answers, Map info){
        this.identifier = info.get("id").toString();
        this.name = info.get("name").toString();
        this.prompts = new ArrayList<>(prompts);
        this.answers = new ArrayList<>(answers);
        this.authorId = info.get("authorId").toString();
        this.templateId = info.get("templateId").toString();
        this.answerChecker = (AnswerChecker)info.get("answerChecker");
        this.currentlyFrozen = false;
    }

    @Override
    public String toString() {
        return "StudyTool{" +
                "name='" + name + '\'' +
                ", identifier=" + identifier +
                ", authorId='" + authorId + '\'' +
                ", accessibility='" + '\'' +
                ", prompts=" + prompts +
                ", answers=" + answers +
                '}';
    }

    String getIdentifier(){
        return this.identifier;
    }

    String getName(){
        return this.name;
    }

    String getAuthorId() {return this.authorId; }

    String getTemplateId() {return this.templateId; }

    boolean isFrozen() {
        return this.currentlyFrozen;
    }
    void setFrozenStatus(boolean newFrozenStatus) {
        this.currentlyFrozen = newFrozenStatus;
    }

    /**
     * Edits a single question, if the answer is of illegal format, return false
     * @param index index of question, starting at 0
     * @return whether the question is successfully edited
     */
     boolean editQuestion(int index, String newQuestion, String newAnswer) {
        if (0 <= index && index < this.prompts.size() && answerChecker.isValidAnswerForm(newAnswer)){
            this.prompts.set(index, newQuestion);
            this.answers.set(index, newAnswer);
            return true;
        }
        else {return false;}
    }

    String[][] getQuestionsAndAnswers() {
        String[] questions = new String[this.prompts.size()];
        questions = this.prompts.toArray(questions);
        String[] answers = new String[this.answers.size()];
        answers = this.answers.toArray(answers);
        String[][] retArray = new String[questions.length][2];
        for (int k = 0; k < questions.length; k++) {
            retArray[k][0] = questions[k];
            retArray[k][1] = answers[k];
        }
        return retArray;
    }

    /**
     * @return a copy of this study tool
     */
    StudyTool duplicateStudyTool() {
        Map info = new HashMap();
        info.put("name", this.name);
        info.put("id", this.identifier);
        info.put("authorId", this.authorId);
        info.put("templateId", this.templateId);
        info.put("answerChecker", this.answerChecker);

        return new StudyTool(this.prompts, this.answers, info);
    }


    boolean isValidAnswerForm(String inputAnswer) {
        return this.answerChecker.isValidAnswerForm(inputAnswer);
    }

    AnswerChecker getAnswerChecker() {
        return answerChecker;
    }

    /**
     * Returns if correct answer according to answer checker
     * @param questionIndex question index, starting from 0
     */
    boolean isCorrectAnswer(int questionIndex, String inputAnswer) {
        return this.answerChecker.isCorrectAnswer(inputAnswer, this.answers.get(questionIndex));
    }


    void addQuestionAnswer(String q, String a) {
        this.prompts.add(q);
        this.answers.add(a);
    }
}
