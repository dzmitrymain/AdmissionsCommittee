package by.epam.learning.yevtukhovich.admissionsCommittee.model.entity;

public class Subject {

    private int subjectId;
    private String name;
    private int grade;

    public Subject(int subjectId) {
        this.subjectId = subjectId;
    }

    public Subject(){

    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return name;
    }
}
