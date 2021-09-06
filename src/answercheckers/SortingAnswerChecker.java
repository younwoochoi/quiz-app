package answercheckers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SortingAnswerChecker implements AnswerChecker{

    /**
     * Valid answer form: (number, letter), (number, letter) ...
     */
    public boolean isValidAnswerForm(String inputAnswer) {
        return inputAnswer.matches("\\(\\d,\\s*[a-zA-Z]\\)(\\s*,\\s*\\(\\d,\\s*[a-zA-Z]\\))*");
    }

    public boolean isCorrectAnswer(String inputAnswer, String correctAnswer) {
        if (isValidAnswerForm(inputAnswer)) {
            Map<String, List<String>> A = splitSORAnswer(inputAnswer);
            Map<String, List<String>> B = splitSORAnswer(correctAnswer);
            for (String category : A.keySet()) {
                if (B.containsKey(category)) {
                    if (!ListEqual(A.get(category), B.get(category))) return false;
                }
                else return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean ListEqual(List<String> list1, List<String> list2) {
        return list1.containsAll(list2) && list2.containsAll(list1);
    }

    private Map<String, List<String>> splitSORAnswer(String answer) {
        /*
        Returns sorting answer of map form
        i.e.
        answer assumed to be of the legal SOR form as above
         */
        Map<String, List<String>> out = new HashMap<>();
        Pattern p = Pattern.compile("\\((\\d),\\s*([a-zA-Z])\\)");
        Matcher m = p.matcher(answer);
        while (m.find()) {
            String category = m.group(1);
            String item = m.group(2).toLowerCase();
            if (out.containsKey(category)) {
                out.get(category).add(item);
            }
            else {
                out.put(category, new ArrayList<>());
                out.get(category).add(item);
            }
        }
        return out;
    }
}
