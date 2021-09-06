package templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Template for a quiz, which can be either exact answer or multiple choice
 */
public class QuizTemplate extends Template {
    // variables
    private final boolean showCorrectAnswerAfterQuestion;
    private final boolean showCorrectAnswersAfterQuiz;

    // constructor(s)
    QuizTemplate(String templateType, List info) {
        super(info);
        if (templateType.equals("MCQ")) {
            this.showCorrectAnswerAfterQuestion = false;
            this.showCorrectAnswersAfterQuiz = true;
        } else { // template is EAQ
            this.showCorrectAnswerAfterQuestion = true;
            this.showCorrectAnswersAfterQuiz = false;
        }
    }

    // methods
    boolean isMCQ() { // will change if more quiz templates added
        return this.showCorrectAnswersAfterQuiz;
    }

    @Override
    Map<String, String> getConfiguration() {
        /*
        Returns the configuration of the template of format (item: configuration)
        e.g. ("name": "default_template"), ("timed": "true"), ("time": "200")
         */
        Map<String, String> retMap = new HashMap<>(super.getConfiguration());
        if (this.isMCQ()) {
            retMap.put("type", "MCQ");
        } else {
            retMap.put("type", "EAQ");
        }
        retMap.put("showCorrectAnswerAfterQuestion", String.valueOf(this.showCorrectAnswerAfterQuestion));
        retMap.put("showCorrectAnswersAfterQuiz", String.valueOf(this.showCorrectAnswersAfterQuiz));
        return retMap;
    }
}
