package main.java.gralog.bellmanford.entity;

public class ParameterVerificationEntity {

    private boolean verificationResults;
    private String errorMessage;

    public boolean getVerificationResults() {
        return verificationResults;
    }

    public void setVerificationResults(boolean verificationResults) {
        this.verificationResults = verificationResults;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
