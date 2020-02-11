package by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception;

public class SubjectServiceException extends ServiceException {

    public SubjectServiceException() {
    }

    public SubjectServiceException(String message) {
        super(message);
    }

    public SubjectServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubjectServiceException(Throwable cause) {
        super(cause);
    }
}
