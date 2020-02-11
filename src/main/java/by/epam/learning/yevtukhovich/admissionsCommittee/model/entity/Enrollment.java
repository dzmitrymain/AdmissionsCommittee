package by.epam.learning.yevtukhovich.admissionsCommittee.model.entity;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.EnrollmentState;

import java.sql.Timestamp;

public class Enrollment {

    private int enrollmentId;
    private EnrollmentState state;
    private Timestamp startDate;
    private Timestamp endDate;

    public Enrollment(int enrollmentId, EnrollmentState state, Timestamp startDate, Timestamp endDate) {
        this.enrollmentId = enrollmentId;
        this.state = state;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Enrollment(){

    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public EnrollmentState getState() {
        return state;
    }

    public void setState(EnrollmentState state) {
        this.state = state;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
