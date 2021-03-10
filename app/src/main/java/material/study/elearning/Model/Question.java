package material.study.elearning.Model;

public class Question {

    private String question,answer,questionNo;

    public Question() {
    }

    public Question(String question, String answer, String questionNo) {
        this.question = question;
        this.answer = answer;
        this.questionNo = questionNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }
}
