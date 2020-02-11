package by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state;

public enum ApplicantState {

    APPLIED(1),
    ENROLLED(2),
    NOT_ENROLLED(3);

    private int ordinalNumber;

    ApplicantState(int ordinalNumber ){
        this.ordinalNumber=ordinalNumber;
    }

    public int getOrdinalNumber(){
        return ordinalNumber;
    }

}
