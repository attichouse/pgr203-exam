package no.kristiania.survey;

public class Survey {

    private long surveyId;
    private String surveyName;


    public long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(long surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String survey) {
        this.surveyName = survey;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "surveyId=" + surveyId +
                ", surveyName='" + surveyName + '\'' +
                '}';
    }
}
