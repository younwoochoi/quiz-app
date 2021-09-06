package answercheckers;

import templates.TemplateManager;

/**
 * Creates answer checker based on TemplateType, default: ExactAnswerChecker
 */
public class AnswerCheckerFactory {
    public AnswerChecker createAnswerChecker(TemplateManager.TemplateType templateType) {
        if (templateType.equals(TemplateManager.TemplateType.SOR)) return new SortingAnswerChecker();
        else if (templateType.equals(TemplateManager.TemplateType.FC)) return new FlashCardAnswerChecker();
        else if (templateType.equals(TemplateManager.TemplateType.MCQ)) return new MultipleChoiceAnswerChecker();
        else return new ExactAnswerChecker();
    }
}
