package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.EnrollmentService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.EnrollmentServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ViewEnrollmentsCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ViewEnrollmentsCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        EnrollmentService enrollmentService = (EnrollmentService) ServiceFactory.getService(ServiceType.ENROLLMENT_SERVICE);
        enrollmentService.takeConnection();

        try {
            List<Enrollment> enrollments = enrollmentService.getAllClosedEnrollments();
            if(!enrollments.isEmpty()){
                LOGGER.info("enrollments were found and set as attribute");
                request.setAttribute(Parameters.ENROLLMENTS, enrollments);
            }else{
                LOGGER.warn("enrollments weren't found");
            }
        } catch (EnrollmentServiceException e) {
            e.printStackTrace();
        } finally {
            enrollmentService.releaseConnection();
        }
        return Pages.FORWARD_VIEW_ENROLLMENTS;
    }
}
