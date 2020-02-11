package by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception;

public class EnrollmentServiceException extends ServiceException {

    public EnrollmentServiceException() {
    }

    public EnrollmentServiceException(String message) {
        super(message);
    }

    public EnrollmentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnrollmentServiceException(Throwable cause) {
        super(cause);
    }
}
