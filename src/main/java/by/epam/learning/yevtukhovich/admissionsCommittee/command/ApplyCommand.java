package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.validator.GradesValidator;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.*;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.ApplicantState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.EnrollmentState;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.ApplicantService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.FacultyService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.SubjectService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.ApplicantServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.FacultyServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.SubjectServiceException;
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
import java.util.ArrayList;
import java.util.List;

public class ApplyCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ApplyCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_ERROR_PAGE;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Parameters.USER);
        String facultyId = request.getParameter(Parameters.ID);

        Enrollment enrollment = (Enrollment) request.getAttribute(Parameters.ENROLLMENT);

        if (enrollment != null && enrollment.getState() == EnrollmentState.OPENED) {
            if (facultyId != null && user != null && user.getRole() == UserRole.APPLICANT) {
                int facultyIntId = Integer.parseInt(facultyId);
                FacultyService facultyService = (FacultyService) ServiceFactory.getService(ServiceType.FACULTY_SERVICE);
                facultyService.takeConnection();

                SubjectService subjectService = (SubjectService) ServiceFactory.getService(ServiceType.SUBJECT_TYPE);
                subjectService.setConnection(facultyService.getConnection());

                try {
                    Faculty faculty = facultyService.getById(facultyIntId);
                    faculty.setRequiredSubjects(subjectService.retrieveRequiredSubjects(facultyIntId));

                    if (type == ActionType.POST) {

                        Applicant newApplicant = new Applicant();
                        newApplicant.setUserId(user.getUserId());
                        newApplicant.setFacultyId(faculty.getId());
                        newApplicant.setEnrollmentId(enrollment.getEnrollmentId());
                        newApplicant.setApplicantState(ApplicantState.APPLIED);

                        ApplicantService applicantService = (ApplicantService) ServiceFactory.getService(ServiceType.APPLICANT_SERVICE);
                        applicantService.setConnection(facultyService.getConnection());

                        applicantService.setAutoCommit(false);
                        if(applicantService.isAlreadyApplied(newApplicant)) {
                            int newId = applicantService.addApplicant(newApplicant);
                            List<Grade> applicantGrades = new ArrayList<>();
                            Grade grade;
                            for (Subject subject : faculty.getRequiredSubjects()) {
                                grade = new Grade();
                                grade.setGrade(request.getParameter(subject.getName()));
                                grade.setApplicantId(newId);
                                grade.setSubjectId(subject.getSubjectId());
                                applicantGrades.add(grade);
                            }
                            if (GradesValidator.validateGrades(applicantGrades)) {
                                subjectService.addGrades(applicantGrades);
                                applicantService.commit();
                                applicantService.setAutoCommit(true);
                                LOGGER.info("applicant has been added: " + newApplicant);
                                page = Pages.REDIRECT_PERSONAL_SETTINGS;
                            } else {
                                facultyService.rollback();
                                facultyService.setAutoCommit(true);
                                session.setAttribute(Parameters.ERROR, Messages.INVALID_GRADE);
                                page = UrlMatcher.matchUrl(request.getHeader(Parameters.REFERER));
                            }
                        }else{
                            LOGGER.warn("trying to apply while already applied: "+user);
                            facultyService.rollback();
                            facultyService.setAutoCommit(true);
                            session.setAttribute(Parameters.ERROR, Messages.ALREADY_APPLIED);
                            page = UrlMatcher.matchUrl(request.getHeader(Parameters.REFERER));
                        }
                    } else {
                        request.setAttribute(Parameters.FACULTY, faculty);
                        LOGGER.info("faculty set as attribute");
                        page = Pages.FORWARD_APPLY_PAGE;
                    }

                } catch (SubjectServiceException | ApplicantServiceException | FacultyServiceException e) {
                    facultyService.rollback();
                    facultyService.setAutoCommit(true);
                    LOGGER.error(e.getMessage());
                    session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                } finally {
                    facultyService.releaseConnection();
                }
            } else {
                LOGGER.debug(Messages.COMMAND_WITHOUT_PARAMETERS);
                session.setAttribute(Parameters.ERROR, Messages.COMMAND_WITHOUT_PARAMETERS);
            }
        } else {
            LOGGER.warn(Messages.WRONG_ENROLLMENT_STATE);
            session.setAttribute(Parameters.ERROR, Messages.WRONG_ENROLLMENT_STATE);
        }

        return page;
    }
}
