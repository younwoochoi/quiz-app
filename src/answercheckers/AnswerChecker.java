package answercheckers;

import java.io.Serializable;

public interface AnswerChecker extends Serializable {

    public boolean isValidAnswerForm(String inputAnswer);
    public boolean isCorrectAnswer(String inputAnswer, String correctAnswer);
}
