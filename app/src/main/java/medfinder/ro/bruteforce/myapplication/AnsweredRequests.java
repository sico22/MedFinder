package medfinder.ro.bruteforce.myapplication;

public class AnsweredRequests {
    String centerName, centerDate, centerHour, centerKey, answerForRequest, answeredService;

    public AnsweredRequests(){

    }

    public AnsweredRequests(String centerName, String centerKey, String centerDate, String centerHour,
                            String answeredService, String answerForRequest){
        this.centerName = centerName;
        this.centerKey = centerKey;
        this.centerDate = centerDate;
        this.centerHour = centerHour;
        this.answerForRequest = answerForRequest;
        this.answeredService = answeredService;
    }

    public String getCenterName(){
        return centerName;
    }
    public String getCenterDate(){
        return centerDate;
    }
    public String getCenterHour(){
        return centerHour;
    }
    public String getCenterKey(){
        return centerKey;
    }
    public String getAnswerForRequest(){return answerForRequest;}
    public String getAnsweredService(){return answeredService;}
}
