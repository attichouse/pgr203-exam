package no.kristiania.survey;

public class Question {

    private long questionId;
    private String questionDescription;
    private long questionIdFk;
    private String questionAlternatives;

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

    public String getQuestionAlternatives() {
        return questionAlternatives;
    }

    public void setQuestionAlternatives(String questionAlternatives) {
        this.questionAlternatives = questionAlternatives;
    }
    @Override
    public String toString() {
        String s = "<p>" + questionDescription +"</p>";
        s += "<label>Svaralternativer: <select name=\"alternativ\" id=\"alternativer\">";
        if (questionAlternatives != null) {
            String[] sa = questionAlternatives.split(";");
            int value = 1;
            for (String so: sa) {
                s += "<option value=" + (value++) + ">" + so + "</option>";
            }
        }
        s += "</select></label>";
        return s;
    }
}
