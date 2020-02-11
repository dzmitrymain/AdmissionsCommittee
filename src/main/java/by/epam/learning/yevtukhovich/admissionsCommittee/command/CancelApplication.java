package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.SubjectDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.EnrollmentState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.ApplicantService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.SubjectService;
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

public class CancelApplication implements Command {

    private static final Logger LOGGER = LogManager.getLogger(CancelApplication.class.getName());


    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page = Pages.REDIRECT_ERROR_PAGE;
        HttpSession session = request.getSession();
        User user= (User) session.getAttribute(Parameters.USER);
        String applicantId = request.getParameter(Parameters.APPLICANT_ID);
        int id = Integer.parseInt(applicantId);

        Enrollment enrollment = (Enrollment) request.getAttribute(Parameters.ENROLLMENT);


        if (enrollment.getState() == EnrollmentState.OPENED) {
            ApplicantService applicantService = (ApplicantService) ServiceFactory.getService(ServiceType.APPLICANT_SERVICE);
            SubjectService subjectService = (SubjectService) ServiceFactory.getService(ServiceType.SUBJECT_TYPE);
            applicantService.takeConnection();
            subjectService.setConnection(applicantService.getConnection());

            try {
                Applicant applicant = applicantService.getApplicantById(id);
                if(applicant.getUserId()==user.getUserId()) {
                    applicantService.setAutoCommit(false);
                    if (subjectService.deleteGrades(id)) {
                        applicantService.cancelApplication(id);
                        applicantService.commit();
                        LOGGER.info("application has been deleted");
                        page = Pages.REDIRECT_PERSONAL_SETTINGS;
                    }
                }else{
                    LOGGER.warn("wrong user: "+user);
                    page=Pages.REDIRECT_ERROR_PAGE;
                    session.setAttribute(Parameters.ERROR,Messages.WRONG_USER);
                }
            } catch (SubjectDaoException | ApplicantServiceException e) {
                applicantService.rollback();
                LOGGER.error(e.getMessage());
                session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
            } finally {
                applicantService.setAutoCommit(true);
                applicantService.releaseConnection();
            }
        } else {
            LOGGER.warn(Messages.WRONG_ENROLLMENT_STATE);
            session.setAttribute(Parameters.ERROR, Messages.WRONG_ENROLLMENT_STATE);
        }
        return page;
    }
}
