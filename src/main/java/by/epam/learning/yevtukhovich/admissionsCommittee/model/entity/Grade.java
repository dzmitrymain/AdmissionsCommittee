package by.epam.learning.yevtukhovich.admissionsCommittee.model.entity;

public class Grade {

    private String grade;
    private int subjectId;
    private int applicantId;

    public Grade(String grade, int subjectId, int applicantId) {
        this.grade = grade;
        this.subjectId = subjectId;
        this.applicantId = applicantId;
    }

    public Grade(){

    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }
}
