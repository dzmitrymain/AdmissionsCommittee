package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Subject;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.ApplicantService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.SubjectService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.ApplicantServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.SubjectServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ViewApplicationCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ViewApplicationCommand.class.getName());


    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page = Pages.FORWARD_ERROR_PAGE;


        int applicantId = Integer.parseInt(request.getParameter(Parameters.ID));

        ApplicantService applicantService = (ApplicantService) ServiceFactory.getService(ServiceType.APPLICANT_SERVICE);
        SubjectService subjectService = (SubjectService) ServiceFactory.getService(ServiceType.SUBJECT_TYPE);
        applicantService.takeConnection();
        subjectService.setConnection(applicantService.getConnection());

        try {
            Applicant applicant = applicantService.getApplicantById(applicantId);
            if (applicant != null) {
                List<Subject> subjects = subjectService.getSubjectGrades(applicantId);
                request.setAttribute(Parameters.APPLICANT, applicant);
                request.setAttribute(Parameters.SUBJECTS, subjects);
                LOGGER.info("application has been found and set as attributes: 'applicant' 'subjects'");

            } else {
                LOGGER.warn("application has not been found");
            }
            page = Pages.FORWARD_VIEW_APPLICATION;
        } catch (ApplicantServiceException | SubjectServiceException e) {
            LOGGER.error(e.getMessage());
            request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        } finally {
            applicantService.releaseConnection();
        }
        return page;
    }
}
