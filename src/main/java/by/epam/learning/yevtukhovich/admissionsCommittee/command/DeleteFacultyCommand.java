package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.FacultyDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.SubjectDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.ApplicantService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.FacultyService;
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
import java.util.List;

public class DeleteFacultyCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(DeleteFacultyCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page= Pages.REDIRECT_ERROR_PAGE;
        int facultyId=Integer.parseInt(request.getParameter(Parameters.ID));
        String facultyName=request.getParameter(Parameters.FACULTY_NAME);

        FacultyService facultyService= (FacultyService) ServiceFactory.getService(ServiceType.FACULTY_SERVICE);
        SubjectService subjectService= (SubjectService) ServiceFactory.getService(ServiceType.SUBJECT_TYPE);
        ApplicantService applicantService= (ApplicantService) ServiceFactory.getService(ServiceType.APPLICANT_SERVICE);
        facultyService.takeConnection();
        subjectService.setConnection(facultyService.getConnection());
        applicantService.setConnection(subjectService.getConnection());

        try {
            List<Applicant> applicants= applicantService.getApplicantsIdByFacultyId(facultyId);
            facultyService.setAutoCommit(false);

            if(!applicants.isEmpty()){
                for (Applicant applicant: applicants) {
                    subjectService.deleteGrades(applicant.getId());
                    applicantService.cancelApplication(applicant.getId());
                }
            }

            subjectService.deleteRequiredSubjectsByFacultyId(facultyId);
            facultyService.deleteFaculty(facultyId);
            facultyService.commit();
            LOGGER.info("faculty has been deleted: "+ facultyName);
            page=Pages.REDIRECT_VIEW_FACULTIES;
        } catch (ApplicantServiceException | SubjectDaoException | FacultyDaoException e) {
            facultyService.rollback();
            LOGGER.error(e.getMessage());
            request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        }finally {
            facultyService.setAutoCommit(true);
            facultyService.releaseConnection();
        }
        return page;
    }
}
