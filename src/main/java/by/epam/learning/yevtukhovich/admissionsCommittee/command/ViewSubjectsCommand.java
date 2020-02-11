package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.SubjectDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Subject;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.SubjectService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ViewSubjectsCommand implements Command  {
    private static final Logger LOGGER = LogManager.getLogger(ViewSubjectsCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {

        String page= Pages.FORWARD_ERROR_PAGE;

        SubjectService subjectService= (SubjectService) ServiceFactory.getService(ServiceType.SUBJECT_TYPE);
     subjectService.takeConnection();

     try{
         List<Subject> subjects= subjectService.getAllSubjects();
         if(subjects!=null){
             LOGGER.info("subjects were found and set as attribute");
             request.setAttribute(Parameters.SUBJECTS,subjects);
         }else{
             LOGGER.warn("subjects weren't found");
         }
         page=Pages.FORWARD_VIEW_SUBJECTS;
     } catch (SubjectDaoException e) {
         LOGGER.error(e.getMessage());
         request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
     }finally {
         subjectService.releaseConnection();
     }
        return page;
    }
}
