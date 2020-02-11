package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.EnrollmentDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.EnrollmentState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.ApplicantService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.EnrollmentService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.ApplicantServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.calculator.EnrollmentCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class CloseEnrollmentCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(CloseEnrollmentCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page = Pages.REDIRECT_ERROR_PAGE;
        HttpSession session = request.getSession();
        Enrollment enrollment = (Enrollment) request.getAttribute(Parameters.ENROLLMENT);

        if (enrollment!=null && enrollment.getState() == EnrollmentState.OPENED) {

            EnrollmentService enrollmentService = (EnrollmentService) ServiceFactory.getService(ServiceType.ENROLLMENT_SERVICE);
            enrollmentService.takeConnection();

            ApplicantService applicantService = (ApplicantService) ServiceFactory.getService(ServiceType.APPLICANT_SERVICE);
            applicantService.setConnection(enrollmentService.getConnection());

            applicantService.setAutoCommit(false);

            try {
                enrollmentService.closeCurrentEnrollment(new Timestamp(System.currentTimeMillis()));
                Map<Faculty, TreeSet<Applicant>> latestApplicants = applicantService.getLatestEnrollmentApplicants();
                if(!latestApplicants.isEmpty()) {
                    List<Integer> enrolledApplicantsIdList = EnrollmentCalculator.calculateEnrolledApplicantsId(latestApplicants);
                    applicantService.enrollApplicants(enrolledApplicantsIdList);
                }
                applicantService.commit();
                LOGGER.info("enrollment was closed with: "+latestApplicants.size()+" applicants");
                page=Pages.REDIRECT_MAIN;
            } catch (ApplicantServiceException | EnrollmentDaoException e) {
                LOGGER.error("could not close enrollment: "+e.getMessage());
                applicantService.rollback();
                request.getSession().setAttribute(Parameters.ERROR,Messages.INTERNAL_ERROR);
            }finally {
                applicantService.setAutoCommit(true);
                applicantService.releaseConnection();
            }
        }else{
            LOGGER.debug(Messages.WRONG_ENROLLMENT_STATE);
            session.setAttribute(Parameters.ERROR, Messages.WRONG_ENROLLMENT_STATE);
        }
        return page;
    }
}
