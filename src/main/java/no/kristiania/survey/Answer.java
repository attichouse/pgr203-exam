package no.kristiania.survey;

public class Answer {
    private long answer_id;
    private String answer_text;
    private long question_id;

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public long getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(long answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    @Override
    public String toString() {
        return "<p>" + answer_text + "</p>";
    }
}
