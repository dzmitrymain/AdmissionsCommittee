package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.ApplicantService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.ApplicantServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

public class ViewApplicantsCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ViewApplicantsCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page = Pages.FORWARD_ERROR_PAGE;
        HttpSession session = request.getSession();
        String enrollmentId = request.getParameter(Parameters.ID);
        User user = (User) session.getAttribute(Parameters.USER);

        ApplicantService applicantService = (ApplicantService) ServiceFactory.getService(ServiceType.APPLICANT_SERVICE);
        try {
            Set<Applicant> applicants;
            if (enrollmentId == null && user != null && user.getRole() == UserRole.ADMIN) {
                applicants = applicantService.getCurrentApplicants();
            } else if (enrollmentId != null) {
                applicants = applicantService.getApplicantsByEnrollment(Integer.parseInt(enrollmentId));
            } else {
                session.setAttribute(Parameters.ERROR, Messages.WRONG_USER_ROLE);
                LOGGER.debug(Messages.WRONG_USER_ROLE);
                return page;
            }
            request.setAttribute(Parameters.APPLICANTS, applicants);
            page = Pages.FORWARD_VIEW_APPLICANTS;
            if (!applicants.isEmpty()) {
                LOGGER.info("applicants were found and set as attribute");
            } else {
                LOGGER.warn("applicants weren't found");
            }
        } catch (ApplicantServiceException e) {
            LOGGER.error(e.getMessage());
        }
        return page;
    }
}
