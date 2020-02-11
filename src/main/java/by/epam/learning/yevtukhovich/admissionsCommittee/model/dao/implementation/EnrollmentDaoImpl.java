package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.AbstractDao;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.EnrollmentDao;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.EnrollmentDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.EnrollmentState;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Fields;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDaoImpl extends AbstractDao implements EnrollmentDao {

    private static final String FIND_LATEST_ENROLLMENT = "SELECT enrollment.id,enrollment_type,start_date, end_date from enrollment JOIN enrollment_state ON enrollment.enrollment_state_id=enrollment_state.id where enrollment.id = (select max(id) from enrollment)";
    private static final String UPDATE_LAST_ENROLLMENT = "UPDATE enrollment Set enrollment_state_id='2', end_date=? ORDER BY id DESC LIMIT 1";
    private static final String INSERT_ENROLLMENT = "INSERT INTO enrollment(enrollment_state_id, start_date) VALUES ('1', ?)";
    private static final String SELECT_CLOSED_ENROLLMENTS = "SELECT id,start_date,end_date FROM admissions_committee.enrollment WHERE end_date is not null";


    public List<Enrollment> getAllClosedEnrollments() throws EnrollmentDaoException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_CLOSED_ENROLLMENTS);

            Enrollment enrollment;
            List<Enrollment> enrollments = new ArrayList<>();
            while (resultSet.next()) {
                enrollment = new Enrollment();
                enrollment.setEnrollmentId(resultSet.getInt(Fields.ID));
                enrollment.setStartDate(resultSet.getTimestamp(Fields.START_DATE));
                enrollment.setEndDate(resultSet.getTimestamp(Fields.END_DATE));

                enrollments.add(enrollment);
            }
            return enrollments;
        } catch (SQLException e) {
            throw new EnrollmentDaoException("could not get al closed enrollments: " + e.getMessage());
        }
    }

    public boolean openNewEnrollment(Timestamp timestamp) throws EnrollmentDaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ENROLLMENT);
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.execute();

            return true;
        } catch (SQLException e) {
            throw new EnrollmentDaoException("could not open new enrollment: " + e.getMessage());
        }
    }

    public boolean closeCurrentEnrollment(Timestamp timestamp) throws EnrollmentDaoException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LAST_ENROLLMENT);
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new EnrollmentDaoException("could not close current enrollment: " + e.getMessage());
        }
    }

    public Enrollment getLatestEnrollment() throws EnrollmentDaoException {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_LATEST_ENROLLMENT);
            Enrollment enrollment = null;
            while (resultSet.next()) {
                enrollment = new Enrollment();
                enrollment.setEnrollmentId(resultSet.getInt(Fields.ID));
                enrollment.setState(EnrollmentState.valueOf(resultSet.getString(Fields.ENROLLMENT_TYPE).toUpperCase()));
                enrollment.setStartDate(resultSet.getTimestamp(Fields.START_DATE));
                enrollment.setEndDate(resultSet.getTimestamp(Fields.END_DATE));
            }
            return enrollment;
        } catch (SQLException e) {
            throw new EnrollmentDaoException("could not get latest enrollment: " + e.getMessage());
        }
    }

}
