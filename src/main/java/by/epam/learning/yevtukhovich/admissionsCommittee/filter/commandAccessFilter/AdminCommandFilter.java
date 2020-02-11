package by.epam.learning.yevtukhovich.admissionsCommittee.filter.commandAccessFilter;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;

import javax.servlet.*;

public class AdminCommandFilter extends CommandAccessFilter implements Filter {

    {
        exclusiveCommands ="adminCommands";
        userRole=UserRole.ADMIN;
        logMessage="non-admin user tried to perform: ";
    }
}
