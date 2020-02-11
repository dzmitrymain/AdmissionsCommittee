package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.EnrollmentState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.EnrollmentService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.EnrollmentServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

public class OpenEnrollmentCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(OpenEnrollmentCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page = Pages.REDIRECT_ERROR_PAGE;
        HttpSession session = request.getSession();

        Enrollment enrollment = (Enrollment) request.getAttribute(Parameters.ENROLLMENT);

        if (enrollment==null || enrollment.getState() == EnrollmentState.CLOSED) {

            EnrollmentService enrollmentService = (EnrollmentService) ServiceFactory.getService(ServiceType.ENROLLMENT_SERVICE);
            enrollmentService.takeConnection();

            try {
                enrollmentService.openNewEnrollment(new Timestamp(System.currentTimeMillis()));
                LOGGER.info("new enrollment was opened");
                page=Pages.REDIRECT_MAIN;
            } catch (EnrollmentServiceException e) {
                LOGGER.error(e.getMessage());
                session.setAttribute(Parameters.ERROR,Messages.INTERNAL_ERROR);
            } finally {
                enrollmentService.releaseConnection();
            }
        } else {
            LOGGER.debug(Messages.WRONG_ENROLLMENT_STATE);
            session.setAttribute(Parameters.ERROR, Messages.WRONG_ENROLLMENT_STATE);
        }
        return page;
    }
}
