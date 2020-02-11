package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception;

public class CommitException extends MySqlTransactionException {
    public CommitException() {
    }

    public CommitException(String message) {
        super(message);
    }

    public CommitException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommitException(Throwable cause) {
        super(cause);
    }
}
