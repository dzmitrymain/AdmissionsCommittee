package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.SubjectDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.validator.FacultyDataValidator;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Subject;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.FacultyService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.SubjectService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.FacultyServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

public class AddFacultyCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(AddFacultyCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page;
        HttpSession session = request.getSession();
        SubjectService subjectService = (SubjectService) ServiceFactory.getService(ServiceType.SUBJECT_TYPE);
        subjectService.takeConnection();

        if (actionType == ActionType.GET) {
            try {
                List<Subject> subjects = subjectService.getAllSubjects();
                subjectService.releaseConnection();
                request.setAttribute(Parameters.SUBJECTS, subjects);
                LOGGER.info("subjects were found and set as attribute");
                page = Pages.FORWARD_ADD_FACULTY;
            } catch (SubjectDaoException e) {
                page = Pages.FORWARD_ERROR_PAGE;
                session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                LOGGER.error(e.getMessage());
            }
        } else {
            String facultyName = request.getParameter(Parameters.FACULTY_NAME);
            String[] subjectStringId = request.getParameterValues(Parameters.SUBJECT_ID);
            String capacityString = request.getParameter(Parameters.CAPACITY);
            if (FacultyDataValidator.validateFacultyName(facultyName) && subjectStringId != null && FacultyDataValidator.validateCapacity(capacityString)) {
                int[] subjectId = new int[subjectStringId.length];
                FacultyService facultyService = (FacultyService) ServiceFactory.getService(ServiceType.FACULTY_SERVICE);
                facultyService.setConnection(subjectService.getConnection());
                try {
                    for (int i = 0; i < subjectStringId.length; i++) {
                        subjectId[i] = Integer.parseInt(subjectStringId[i]);
                    }
                    Faculty faculty = new Faculty();
                    faculty.setName(facultyName);
                    faculty.setCapacity(Integer.parseInt(capacityString));
                    facultyService.setAutoCommit(false);
                    int newFacultyId = facultyService.addFaculty(faculty);
                    subjectService.addRequiredSubjects(newFacultyId, subjectId);
                    facultyService.commit();
                    LOGGER.info("faculty has been added: " + faculty);
                    page = Pages.REDIRECT_VIEW_FACULTIES;
                } catch (FacultyServiceException | SubjectDaoException | NumberFormatException e) {
                    facultyService.rollback();
                    LOGGER.error(e.getMessage());
                    session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                    page = Pages.REDIRECT_ERROR_PAGE;
                } finally {
                    facultyService.setAutoCommit(true);
                    facultyService.releaseConnection();
                }
            } else {
                page = Pages.REDIRECT_ADD_FACULTY;
                session.setAttribute(Parameters.ERROR, Messages.INVALID_FACULTY_DATA);
                LOGGER.warn(Messages.INVALID_FACULTY_DATA);
            }
        }
        return page;
    }

}
