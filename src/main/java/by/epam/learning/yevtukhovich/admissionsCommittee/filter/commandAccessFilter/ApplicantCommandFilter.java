package by.epam.learning.yevtukhovich.admissionsCommittee.filter.commandAccessFilter;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;

import javax.servlet.*;

public class ApplicantCommandFilter extends CommandAccessFilter implements Filter {

    {
        exclusiveCommands ="applicantCommands";
        userRole=UserRole.APPLICANT;
        logMessage="non-applicant user tried to perform: ";
    }
}
