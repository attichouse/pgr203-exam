package no.kristiania.survey;

public class Question {

    private long questionId;
    private String questionDescription;
    private long questionIdFk;

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public long getQuestionIdFk() {
        return questionIdFk;
    }

    public void setQuestionIdFk(long questionIdFk) {
        this.questionIdFk = questionIdFk;
    }
}
