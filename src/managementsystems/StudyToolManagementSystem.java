package managementsystems;

import answercheckers.AnswerChecker;
import answercheckers.AnswerCheckerFactory;
import studytools.*;
import templates.TemplateDisplayer;
import templates.TemplateManager;
import textui.PresenterPrompts;
import textui.RegularPresenter;
import users.UserManager;

import java.util.*;

/**
 * Has methods that are common to both AdminManagementSystem and RegularManagementSystem:
 * Viewing, editing and studying with study tools.
 * Version control system launcher
 */
public abstract class StudyToolManagementSystem {
    protected String thisUserId;
    protected UserManager userManager;
    protected TemplateManager templateManager;
    protected StudyToolManager studyToolManager;
    protected CollabManager collabManager;
    protected StudyToolDisplayer studyToolDisplayer;
    private TemplateDisplayer templateDisplayer;
    protected RegularPresenter regularPresenter;
    protected Map<PresenterPrompts, String> promptsMap;
    private Scanner scanner;

    public StudyToolManagementSystem(String thisUserId, UserManager userManager,
                                     TemplateManager templateManager, StudyToolManager studyToolManager,
                                     CollabManager collabManager, Map<PresenterPrompts, String> promptsMap) {
        this.thisUserId = thisUserId;
        this.userManager = userManager;
        this.templateManager = templateManager;
        this.studyToolManager = studyToolManager;
        this.collabManager = collabManager;
        this.studyToolDisplayer = new StudyToolDisplayer(promptsMap);
        this.templateDisplayer = new TemplateDisplayer(promptsMap);
        this.regularPresenter = new RegularPresenter(promptsMap);
        this.promptsMap = promptsMap;
        this.scanner = new Scanner(System.in);
    }

    protected void editStudyTool() {
        regularPresenter.chooseStudyToolToEditPrompter();
        String studyToolId = scanner.nextLine();

        if (!studyToolManager.studyToolExists(studyToolId)) {
            regularPresenter.noStudyToolToEditSorrier();
            return;
        }
        if (verifyRightToEdit(studyToolId)) {
            studyToolEditor(studyToolId);
            collabManager.addToHistory(studyToolManager.getStudyToolByID(studyToolId), thisUserId);
        } else {
            regularPresenter.noStudyToolToEditSorrier();
        }
    }

    private void studyToolEditor(String id) {
        String[][] qa = this.studyToolManager.fetchQuestionsAndAnswers(id);
        boolean editing = true;
        StudyToolDisplayer sd = new StudyToolDisplayer(promptsMap);
        while (editing) {
            sd.displayAllQuestionsAndAnswers(qa);
            regularPresenter.displayEditMenuChoices();
            regularPresenter.chooseOptionWithQuitPrompter();
            String choice = scanner.nextLine();

            switch (choice) {
                case "q":
                case "Q":
                    editing = false;
                    break;
                case "1":
                    qa = singleQuestionEditor(qa, id, true, false);
                    break;
                case "2":
                    qa = singleQuestionEditor(qa, id, false, true);
                    break;
                case "3":
                    String[] newQA = newQuestionAnswerPrompter("", "", studyToolManager.getAnswerChecker(id), true, true);
                    this.studyToolManager.addQuestionAndAnswer(id, newQA[0], newQA[1]);
                    qa = this.studyToolManager.fetchQuestionsAndAnswers(id);
                    break;
                default:
                    regularPresenter.sayInvalidInput();
            }
        }
    }

    protected void studyWithStudyTool() {
        /*
        Prompt to select a study tool, check if has access to it, then use Studier to study
         */
        regularPresenter.chooseStudyToolToStudyPrompter();
        String studyToolId = scanner.nextLine();
        if (studyToolId.equals("q")) {
            return;
        }
        if (verifyRightToView(studyToolId)) {
            study(studyToolId);
        } else {
            regularPresenter.noStudyToolToStudySorrier();
        }
    }

    private void study(String studyToolId) {
        // TODO: long method, split into helpers
        String[][] questionsAndAnswers = studyToolManager.fetchQuestionsAndAnswers(studyToolId);
        String templateId = studyToolManager.getStudyToolTemplateId(studyToolId);
        TemplateManager.TemplateType templateType = templateManager.getTemplateType(Integer.parseInt(templateId));
        List<Object> timeInfo = templateManager.getTimeInfo(templateId);
        boolean isTimed = (Boolean) timeInfo.get(0);
        boolean isTimedPerQuestion = (Boolean) timeInfo.get(1);
        long timeLimitInMS = 1000 * (Integer) timeInfo.get(2);
        long quizStartTime = System.currentTimeMillis();
        int total_score = questionsAndAnswers.length;
        int curr_score = 0;
        Queue<String[]> questionsToRepeat = new LinkedList<>();
        AnswerChecker answerChecker = studyToolManager.getAnswerChecker(studyToolId);
        for (String[] qa : questionsAndAnswers) {
            long questionStartTime = System.currentTimeMillis();
            studyToolDisplayer.displayAQuestion(qa);
            regularPresenter.getAnswerPrompter(templateType);
            String answer = scanner.nextLine();
            boolean correctness = answerChecker.isCorrectAnswer(answer, qa[1]);
            if (templateType.equals(TemplateManager.TemplateType.FC) && !correctness) {
                // TODO: make this depend on template's configuration
                questionsToRepeat.add(qa);
            }
            long questionTimeElapsed = System.currentTimeMillis() - questionStartTime;
            if (isTimed && isTimedPerQuestion && (questionTimeElapsed >= timeLimitInMS)) {
                regularPresenter.ranOutOfTimeReporter();
            } else {
                curr_score += (correctness ? 1 : 0);
                regularPresenter.correctnessReporter(correctness);
            }
            regularPresenter.pressEnterToShowAnswer();
            scanner.nextLine();
            studyToolDisplayer.displayAnAnswer(qa);
        }
        //FC only, for repeating wrong questions until all is memorized
        while (!questionsToRepeat.isEmpty()) {
            String[] qa = questionsToRepeat.poll();
            studyToolDisplayer.displayAQuestion(qa);
            regularPresenter.getAnswerPrompter(templateType);
            String answer = scanner.nextLine();
            boolean correctness = answerChecker.isCorrectAnswer(answer, qa[1]);
            if (!correctness) {
                questionsToRepeat.add(qa);
            }
            regularPresenter.correctnessReporter(correctness);
            regularPresenter.pressEnterToShowAnswer();
            studyToolDisplayer.displayAnAnswer(qa);
        }
        long quizTimeElapsed = System.currentTimeMillis() - quizStartTime;
        if (isTimed && !isTimedPerQuestion && (quizTimeElapsed >= timeLimitInMS)){
            regularPresenter.ranOutOfTimeReporter();
        }
        else if (templateManager.isTemplateScored(templateId)) {
            String score = curr_score + "/" + total_score;
            regularPresenter.studySessionEndedReporter(score);
        }
    }

    protected void createStudyTool() {
        if (!templateManager.isEmpty()) {
            List<String> questions = new ArrayList<>();
            List<String> answers = new ArrayList<>();
            templateDisplayer.displayAllTemplates(templateManager);
            String templateID;
            do {        // Ask for template ID
                regularPresenter.templateIDPrompter();
                templateID = scanner.nextLine();
            } while (!templateManager.validId(templateID));

            AnswerCheckerFactory answerCheckerFactory = new AnswerCheckerFactory();
            AnswerChecker answerChecker = answerCheckerFactory.createAnswerChecker(
                    templateManager.getTemplateType(Integer.parseInt(templateID)));
            while (true) {      // Keep prompting for question and answer until q
                regularPresenter.newQuestionWithQuitPrompter();
                String q = scanner.nextLine();
                if (q.equalsIgnoreCase("q")) {
                    break;
                }
                q = q.replace("\\n", "\n");
                String a = promptForAnswer(answerChecker);
                questions.add(q);
                answers.add(a);
            }
            // Save study tool
            regularPresenter.saveOrNotPrompter();
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("y")) {
                Map info = retMap(templateID, answerChecker);

                StudyTool createdStudyTool = studyToolManager.createStudyTool(questions, answers, info);
                collabManager.addToHistory(createdStudyTool, thisUserId);

                regularPresenter.quizSavedReporter();
            }
        } else {
            regularPresenter.noTemplateToChooseSorrier();
        }
    }

    /**
     * Prompts and creates a Map representing a study tool.
     * @return The information received from prompts in a Map with keys:
     *      {name, accessibility, templateId, authorId, answerChecker}
     */
    private Map retMap(String templateId, AnswerChecker answerChecker) {
        Map retMap = new HashMap<String, String>();

        regularPresenter.studyToolNamePrompter();
        retMap.put("name", scanner.nextLine());
        regularPresenter.setAccessibilityPrompter();
        retMap.put("accessibility", scanner.nextLine());
        retMap.put("templateId", templateId);
        retMap.put("authorId", thisUserId);
        retMap.put("answerChecker", answerChecker);

        return retMap;
    }

    protected void changeAccessibilityOfStudyTool() {
        regularPresenter.chooseStudyToolToEditPrompter();
        String studyToolId = scanner.nextLine();
        if (verifyRightToEdit(studyToolId)) {
            regularPresenter.setAccessibilityPrompter();
            String newAccessibility = scanner.nextLine();
            studyToolManager.changeAccessibility(studyToolId, newAccessibility);
        } else {
            regularPresenter.noStudyToolToEditSorrier();
        }
    }

    protected abstract boolean verifyRightToView(String studyToolId);

    protected abstract boolean verifyRightToEdit(String studyToolId);

    protected abstract boolean verifyRightToUndelete(String studyToolId);

    protected void launchVersionControlSystem() {
        ManagementRunnable versionControlSystem = new VersionControlSystem(studyToolManager, collabManager,
                userManager, studyToolDisplayer, regularPresenter, thisUserId, scanner);
        versionControlSystem.run();
    }

    // Helper methods

    /**
     * Will keep prompting for answer until the user enters an acceptable answer form, according to answerChecker
     * @return the new answer
     */
    private String promptForAnswer(AnswerChecker answerChecker) {
        String answer;
        while (true) {
            regularPresenter.newAnswerPrompter();
            answer = scanner.nextLine();
            if (answerChecker.isValidAnswerForm(answer)) break;
            else regularPresenter.illegalAnswerReporter();
        }
        return answer;
    }

    // deleting study tool method
    protected void deleteStudyTool() {
        regularPresenter.selectStudyToolToDeletePrompt();
        String studyToolID = scanner.nextLine();

        if (verifyRightToEdit(studyToolID)) {
            studyToolManager.deleteStudyTool(studyToolID);
        } else {
            regularPresenter.sayNoStudyToolToDelete();
        }
    }

    // undeleting study tool method
    protected void undeleteStudyTool() {
        regularPresenter.selectStudyToolToUndeletePrompt();
        String studyToolID = scanner.nextLine();

        if (verifyRightToUndelete(studyToolID)) {
            studyToolManager.revertDeletedStudyTool(studyToolID);
        } else {
            regularPresenter.sayNoStudyToolToUndelete();
        }
    }

    private String[][] singleQuestionEditor(String[][] qa, String studyToolID, boolean editingQuestion, boolean editingAnswer) {
        String numQuestion;
        do {
            regularPresenter.chooseQuestionNumberToEditPrompter();
            numQuestion = scanner.nextLine();
        } while (!numQuestion.matches("\\d+"));
        int questionNum = Integer.parseInt(numQuestion);
        int index = questionNum - 1;
        if (0 <= index && index < qa.length) {
            String[] newQA = newQuestionAnswerPrompter(qa[index][0], qa[index][1], studyToolManager.getAnswerChecker(studyToolID), editingQuestion, editingAnswer);
            if (!this.studyToolManager.editSingleQuestion(studyToolID, index, newQA[0], newQA[1])) {
                // Attempt to edit question, false -> Answer is of the incorrect form
                regularPresenter.sayInvalidInput();
            }
        }
        return this.studyToolManager.fetchQuestionsAndAnswers(studyToolID);
    }

    /**
     * @param promptForQuestion whether prompt for question
     * @param promptForAnswer whether prompt for answer
     * @return (newQuestion, newAnswer), if new ones are not needed, the old ones are used
     */
    private String[] newQuestionAnswerPrompter(String oldQuestion, String oldAnswer, answercheckers.AnswerChecker answerChecker, boolean promptForQuestion, boolean promptForAnswer) {
        String newQuestion = oldQuestion;
        String newAnswer = oldAnswer;
        if (promptForQuestion) {
            regularPresenter.newQuestionPrompter();
            newQuestion = scanner.nextLine();
            newQuestion = newQuestion.replace("\\n", "\n");
        }
        if (promptForAnswer) {
            newAnswer = this.promptForAnswer(answerChecker);
        }
        return new String[]{newQuestion, newAnswer};
    }
}
