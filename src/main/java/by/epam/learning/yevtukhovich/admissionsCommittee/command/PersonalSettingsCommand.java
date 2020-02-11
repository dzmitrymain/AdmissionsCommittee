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
import java.util.Collections;
import java.util.List;

public class PersonalSettingsCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(PersonalSettingsCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page = Pages.FORWARD_ERROR_PAGE;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Parameters.USER);

        if (user != null) {
            page = Pages.FORWARD_PERSONAL_SETTINGS;
            if (user.getRole() == UserRole.APPLICANT) {
                ApplicantService applicantService= (ApplicantService) ServiceFactory.getService(ServiceType.APPLICANT_SERVICE);
                applicantService.takeConnection();
                try {
                   List<Applicant> applicants = applicantService.getApplicantsByUserId(user.getUserId());
                   if(!applicants.isEmpty()) {
                       Collections.reverse(applicants);
                       request.setAttribute(Parameters.APPLICANTS , applicants);
                       LOGGER.info("applications were found and set as attribute");
                   }
                } catch (ApplicantServiceException e) {
                    LOGGER.error(e.getMessage());
                    session.setAttribute(Parameters.ERROR,Messages.INTERNAL_ERROR);
                } finally {
                    applicantService.releaseConnection();
                }
            }
        } else {
            session.setAttribute(Parameters.ERROR, Messages.WRONG_USER_ROLE);
        }
        return page;
    }
}
