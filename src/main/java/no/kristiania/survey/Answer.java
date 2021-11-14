package no.kristiania.survey;

public class Answer {
    private long answer_id;
    private String answer_text;
    private long question_id;

    public long getQuestionId() {
        return question_id;
    }

    public void setQuestionId(long question_id) {
        this.question_id = question_id;
    }

    public long getAnswerId() {
        return answer_id;
    }

    public void setAnswerId(long answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswerText() {
        return answer_text;
    }

    public void setAnswerText(String answer_text) {
        this.answer_text = answer_text;
    }

    @Override
    public String toString() {
        return "<p>" + answer_text + "</p>";
    }
}
