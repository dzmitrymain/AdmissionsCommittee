package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.FacultyService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.FacultyServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.jvm.hotspot.debugger.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ViewFacultiesCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ViewFacultyCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page=Pages.FORWARD_ERROR_PAGE;

        FacultyService facultyService = (FacultyService) ServiceFactory.getService(ServiceType.FACULTY_SERVICE);
        facultyService.takeConnection();

        try {
            List<Faculty> faculties = facultyService.retrieveAllFaculties();
            if(!faculties.isEmpty()) {
                LOGGER.info("faculties were found and set as attribute");
                request.setAttribute(Parameters.FACULTIES, faculties);
            }else{
                LOGGER.warn("faculties weren't found");
            }
            page= Pages.FORWARD_VIEW_FACULTIES;
        } catch (FacultyServiceException e) {
            LOGGER.error(e.getMessage());
            request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        } finally {
            facultyService.releaseConnection();
        }
        return page;
    }
}
