package by.epam.learning.yevtukhovich.admissionsCommittee.model.entity;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.ApplicantState;

public class Applicant extends User implements Comparable<Applicant> {

    private int id;
    private int facultyId;
    private String facultyName;
    private int enrollmentId;
    private ApplicantState applicantState;
    private int totalRating;


    public Applicant(int userId, UserRole role, String login, int password, String firstName, String lastName, String patronymic, int id, int userId1, int facultyId, int enrollmentId, ApplicantState applicantState, int totalRating) {
        super(userId, role, login, password, firstName, lastName, patronymic);
        this.id = id;
        this.facultyId = facultyId;
        this.enrollmentId = enrollmentId;
        this.applicantState = applicantState;
        this.totalRating = totalRating;
    }

    public Applicant(UserRole role, String login, int password, String firstName, String lastName, String patronymic, int id, int userId, int facultyId, int enrollmentId, ApplicantState applicantState, int totalRating) {
        super(role, login, password, firstName, lastName, patronymic);
        this.id = id;
        this.facultyId = facultyId;
        this.enrollmentId = enrollmentId;
        this.applicantState = applicantState;
        this.totalRating = totalRating;
    }

    public Applicant(int id, int userId, int facultyId, int enrollmentId, ApplicantState applicantState, int totalRating) {
        this.id = id;
        this.facultyId = facultyId;
        this.enrollmentId = enrollmentId;
        this.applicantState = applicantState;
        this.totalRating = totalRating;
    }

    public Applicant() {

    }


    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public ApplicantState getApplicantState() {
        return applicantState;
    }

    public void setApplicantState(ApplicantState applicantState) {
        this.applicantState = applicantState;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public int compareTo(Applicant applicant) {
        return totalRating > applicant.totalRating ? -1 : totalRating == applicant.totalRating ? 0 : 1;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id=" + id +
                ", facultyId=" + facultyId +
                ", facultyName='" + facultyName + '\'' +
                ", enrollmentId=" + enrollmentId +
                ", applicantState=" + applicantState +
                ", totalRating=" + totalRating +
                '}' + super.toString();
    }
}
