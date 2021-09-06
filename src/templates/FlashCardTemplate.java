package templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlashCardTemplate extends Template {
    // variables
    private boolean wordsReappear;

    // constructor(s)
    /**
     * A name, whether the Template is timed, whether it is timed per Question or Quiz, a time limit, if it is
     * scored, if words reappear are required to create an instance of Template.
     * @param info A list of Strings necessary to initialize the Template in the format
     *             [name, isTimed, timedPerQuestionNotQuiz, timeLimit, isScored, wordsReappear]
     */
    FlashCardTemplate(List info) {
        super(info);
        this.wordsReappear = Boolean.parseBoolean((String)info.get(5));
    }

    // methods
    void setWordsReappear(boolean wordsReappear) {
        this.wordsReappear = wordsReappear;
    }

    @Override
    Map<String, String> getConfiguration() {
        /*
        Returns the configuration of the template of format (item: configuration)
        e.g. ("name": "default_template"), ("timed": "true"), ("time": "200")
         */
        Map<String, String> retMap = new HashMap<>(super.getConfiguration());
        retMap.put("type", "FC");
        retMap.put("wordsReappear", String.valueOf(this.wordsReappear));
        return retMap;
    }
}
