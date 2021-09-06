package templates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Template for sorting study tool: putting each item in a category.
 */
public class SortingTemplate extends Template {

    // variable(s)
    private int numCategories;

    // constructor(s)
    /**
     * A name, whether the Template is timed, whether it is timed per Question or Quiz, a time limit, if it is
     * scored, number of categories are required to create an instance of Template.
     * @param info A list of Strings necessary to initialize the Template in the format
     *             [name, isTimed, timedPerQuestionNotQuiz, timeLimit, isScored, numCategories]
     */
    SortingTemplate(List info) {
        super(info);
        this.numCategories = Integer.parseInt((String)info.get(5));
    }

    // methods
    void setNumCategories(int numCategories){this.numCategories = numCategories;}

    @Override
    Map<String, String> getConfiguration() {
        /*
        Returns the configuration of the template of format (item: configuration)
        e.g. ("name": "default_template"), ("timed": "true"), ("time": "200")
         */
        Map<String, String> retMap = new HashMap<>(super.getConfiguration());
        retMap.put("type", "SOR");
        retMap.put("numCategories", String.valueOf(this.numCategories));
        return retMap;
    }
}
