package by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception;

public class FacultyServiceException extends ServiceException{

    public FacultyServiceException() {
    }

    public FacultyServiceException(String message) {
        super(message);
    }

    public FacultyServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacultyServiceException(Throwable cause) {
        super(cause);
    }
}
