package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.validator;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Grade;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GradesValidator {

    private static final Logger LOGGER = LogManager.getLogger(GradesValidator.class.getName());

    public static boolean validateGrades(String[] grades) {
        for (String grade : grades) {
            try {
                int gradeInt = Integer.parseInt(grade);
                if (!(gradeInt >= 5 && gradeInt <= 100)) {
                    LOGGER.debug(Messages.INVALID_GRADE+" Grade: "+gradeInt);
                    return false;
                }
            } catch (NumberFormatException e) {
                LOGGER.debug(e.getMessage()+" Grade: "+grade);
                return false;
            }
        }
        return true;
    }
}
