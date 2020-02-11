package by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception;

public class ApplicantServiceException extends ServiceException {

    public ApplicantServiceException() {
    }

    public ApplicantServiceException(String message) {
        super(message);
    }

    public ApplicantServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicantServiceException(Throwable cause) {
        super(cause);
    }
}
