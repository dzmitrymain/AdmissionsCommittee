package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.validator.GradesValidator;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.ApplicantState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.EnrollmentState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.ApplicantService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.FacultyService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.ApplicantServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.FacultyServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.matcher.UrlMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ApplyCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ApplyCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = type == ActionType.POST ? Pages.REDIRECT_ERROR_PAGE : Pages.FORWARD_ERROR_PAGE;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Parameters.USER);
        String facultyIdString = request.getParameter(Parameters.ID);
        Enrollment enrollment = (Enrollment) request.getAttribute(Parameters.ENROLLMENT);

        if (enrollment != null && enrollment.getState() == EnrollmentState.OPENED) {
            if (facultyIdString != null && user != null && user.getRole() == UserRole.APPLICANT) {
                int facultyId = Integer.parseInt(facultyIdString);

                try {
                    if (type == ActionType.POST) {
                        String[] subjectGradesStrings = request.getParameterValues(Parameters.GRADE);
                        if (GradesValidator.validateGrades(subjectGradesStrings)) {
                            Applicant newApplicant = new Applicant();
                            newApplicant.setUserId(user.getUserId());
                            newApplicant.setFacultyId(facultyId);
                            newApplicant.setEnrollmentId(enrollment.getEnrollmentId());
                            newApplicant.setApplicantState(ApplicantState.APPLIED);
                            ApplicantService applicantService = (ApplicantService) ServiceFactory.getService(ServiceType.APPLICANT_SERVICE);

                            if (applicantService.addApplicant(newApplicant, subjectGradesStrings)) {
                                LOGGER.info("applicant has been added: " + newApplicant);
                                page = Pages.REDIRECT_PERSONAL_SETTINGS;
                            } else {
                                LOGGER.warn("trying to apply while already applied: " + user);
                                session.setAttribute(Parameters.ERROR, Messages.ALREADY_APPLIED);
                                page = UrlMatcher.matchUrl(request.getHeader(Parameters.REFERER));
                            }
                        } else {
                            session.setAttribute(Parameters.ERROR, Messages.INVALID_GRADE);
                            page = UrlMatcher.matchUrl(request.getHeader(Parameters.REFERER));
                        }
                    } else {
                        FacultyService facultyService = (FacultyService) ServiceFactory.getService(ServiceType.FACULTY_SERVICE);
                        Faculty faculty = facultyService.getById(facultyId);
                        request.setAttribute(Parameters.FACULTY, faculty);
                        LOGGER.info("faculty set as attribute");
                        page = Pages.FORWARD_APPLY_PAGE;
                    }

                } catch (ApplicantServiceException | FacultyServiceException e) {
                    LOGGER.error(e.getMessage());
                    session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                }
            } else {
                LOGGER.warn("wrong input parameters");
                session.setAttribute(Parameters.ERROR, Messages.NO_ACCESS);
            }
        } else {
            LOGGER.warn(Messages.WRONG_ENROLLMENT_STATE);
            session.setAttribute(Parameters.ERROR, Messages.WRONG_ENROLLMENT_STATE);
        }

        return page;
    }
}
