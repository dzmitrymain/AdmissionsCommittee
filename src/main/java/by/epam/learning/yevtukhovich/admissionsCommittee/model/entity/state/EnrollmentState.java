package by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state;

public enum EnrollmentState {

    OPENED(1),
    CLOSED(2);

    private int ordinalNumber;

    EnrollmentState(int ordinalNumber){
        this.ordinalNumber=ordinalNumber;
    }
    public int getOrdinalNumber(){
        return ordinalNumber;
    }
}
