package answercheckers;

public class FlashCardAnswerChecker implements AnswerChecker{

    public boolean isValidAnswerForm(String inputAnswer) {
        /*
        All non-empty answer is valid
         */
        return inputAnswer.length() >= 1;
    }

    public boolean isCorrectAnswer(String inputAnswer, String correctAnswer) {
        return inputAnswer.equalsIgnoreCase("y");
    }
}
