package templates;

import java.util.List;

public class TemplateFactory {

    /**
     * Use getTemplate to get a Template of templateType.
     * @param templateType The type of this Template.
     * @param info The information required to create this Template in the format
     *             [name, isTimed, timedPerQuestionNotQuiz, timeLimit, isScored, ...]
     *             depending on the template type.
     */
    public Template getTemplate(String templateType, List info) {

        switch (templateType) {
            case "FC":
                return new FlashCardTemplate(info);
            case "MCQ":
            case "EAQ":
                return new QuizTemplate(templateType, info);
            case "SOR":
                return new SortingTemplate(info);
            default:
                System.out.println("A " + templateType + " is an undefined template for this program.");
                return null;
        }
    }
}