package answercheckers;

public class ExactAnswerChecker implements AnswerChecker{
    @Override
    public boolean isValidAnswerForm(String inputAnswer) {
        /*
        Every nonempty answer is valid in exact answer
         */
        return inputAnswer.length() >= 1;
    }

    public boolean isCorrectAnswer(String inputAnswer, String correctAnswer) {
        return inputAnswer.equalsIgnoreCase(correctAnswer);
    }
}
