package by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role;

public enum UserRole {

    ADMIN(1),
    APPLICANT(2);

    private int ordinalNumber;

    UserRole(int ordinalNumber){
        this.ordinalNumber=ordinalNumber;
    }

    public int getOrdinalNumber(){
        return ordinalNumber;
    }
}
