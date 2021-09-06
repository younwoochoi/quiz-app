package answercheckers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultipleChoiceAnswerChecker implements AnswerChecker{

    /**
     * Valid: letter, letter, letter...
     */
    public boolean isValidAnswerForm(String inputAnswer) {
        return inputAnswer.matches("^[a-zA-Z](, [a-zA-Z])*$");
    }

    public boolean isCorrectAnswer(String inputAnswer, String correctAnswer) {
        if (isValidAnswerForm(inputAnswer)) {
            List<String> A = splitMCAnswer(inputAnswer);
            List<String> B = splitMCAnswer(correctAnswer);
            return A.containsAll(B) && B.containsAll(A);
        } else {
            return false;
        }
    }

    private List<String> splitMCAnswer(String answer) {
        /*
        Returns mc answer of array form
        answer assumed to be of the legal MC form: a, b, d
         */
        List<String> out = new ArrayList<>();
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(answer);
        while (m.find()) {
            out.add(m.group(1).toLowerCase());
        }
        return out;
    }
}