package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.implementation;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.AbstractDao;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.ApplicantDao;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception.ApplicantDaoException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.state.ApplicantState;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Fields;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ApplicantDaoImpl extends AbstractDao implements ApplicantDao {

    private static final String INSERT_APPLICANT = "INSERT INTO applicant (user_id, faculty_id, applicant_state_id, enrollment_id) VALUES (?, ?, ?, ?)";
    private static final String FIND_APPLICANTS_BY_USER_ID = "SELECT applicant.id as applicant_id, enrollment.id ,faculty.name,applicant_state.state_type FROM admissions_committee.applicant JOIN admissions_committee.applicant_state ON applicant.applicant_state_id=applicant_state.id JOIN admissions_committee.enrollment ON applicant.enrollment_id=enrollment.id JOIN admissions_committee.faculty ON applicant.faculty_id=faculty.id WHERE user_id=?";
    private static final String FIND_LATEST_APPLICANTS = "SELECT applicant.id as applicant_id, faculty.id as faculty_id, faculty.name, faculty.capacity," +
            " (SELECT sum(grade) from admissions_committee.subject_grades WHERE applicant_id=applicant.id) as total_rating FROM admissions_committee.applicant " +
            "JOIN admissions_committee.user ON applicant.user_id=user.id JOIN admissions_committee.faculty ON applicant.faculty_id=faculty.id " +
            "JOIN admissions_committee.enrollment ON applicant.enrollment_id=enrollment.id where enrollment.id = (select max(id) from enrollment)";
    private static final String UPDATE_ENROLLED_APPLICANT_STATE = "UPDATE applicant SET applicant_state_id=2 WHERE id=?";
    private static final String UPDATE_NOT_ENROLLED_APPLICANTS_STATE = "UPDATE applicant SET applicant_state_id=3 WHERE applicant_state_id=1";
    private static final String FIND_APPLICANTS_BY_ENROLLMENT = "SELECT applicant.id as applicant_id,user.last_name, user.first_name,user.patronymic, faculty.name," +
            "(SELECT sum(grade) from admissions_committee.subject_grades WHERE applicant_id=applicant.id) as total_rating, applicant_state.state_type FROM admissions_committee.applicant" +
            " JOIN admissions_committee.user ON applicant.user_id=user.id JOIN admissions_committee.faculty ON applicant.faculty_id=faculty.id" +
            " JOIN admissions_committee.enrollment ON applicant.enrollment_id=enrollment.id JOIN applicant_state ON applicant_state.id=applicant.applicant_state_id where enrollment.id = ";
    private static final String SELECT_LATEST_ENROLLMENT_ID_SUBQUERY = "(SELECT MAX(id) FROM enrollment) AND end_date is NULL";
    private static final char PREPARED_CHAR = '?';
    private static final String DELETE_APPLICANT_BY_ID="DELETE FROM admissions_committee.applicant WHERE id=?";
    private static final String FIND_APPLICANTS_ID_BY_FACULTY_ID="SELECT applicant.id, faculty.name FROM admissions_committee.applicant JOIN admissions_committee.faculty ON applicant.faculty_id=faculty.id WHERE applicant.faculty_id=?";
    private static final String FIND_APPLICANT_BY_ID="SELECT applicant.enrollment_id, user.id, user.last_name, user.first_name, user.patronymic, faculty.name, applicant_state.state_type FROM admissions_committee.applicant JOIN admissions_committee.user ON applicant.user_id=user.id JOIN admissions_committee.faculty ON applicant.faculty_id=faculty.id JOIN admissions_committee.applicant_state ON applicant.applicant_state_id=applicant_state.id WHERE applicant.id=?";

    public TreeSet<Applicant> getApplicantsByEnrollment(int enrollmentId) throws ApplicantDaoException {
        StringBuilder sqlQuery = new StringBuilder(FIND_APPLICANTS_BY_ENROLLMENT);
        try {
            ResultSet resultSet;
            if (enrollmentId == 0) {
                sqlQuery.append(SELECT_LATEST_ENROLLMENT_ID_SUBQUERY);
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sqlQuery.toString());
            } else {
                sqlQuery.append(PREPARED_CHAR);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
                preparedStatement.setInt(1, enrollmentId);
                resultSet = preparedStatement.executeQuery();
            }
            TreeSet<Applicant> applicants = new TreeSet<>();
            Applicant applicant;
            while (resultSet.next()) {
                applicant = new by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant();
                applicant.setId(resultSet.getInt(Fields.APPLICANT_ID));
                applicant.setLastName(resultSet.getString(Fields.LAST_NAME));
                applicant.setFirstName(resultSet.getString(Fields.FIRST_NAME));
                applicant.setPatronymic(resultSet.getString(Fields.PATRONYMIC));
                applicant.setFacultyName(resultSet.getString(Fields.NAME));
                applicant.setTotalRating(resultSet.getInt(Fields.TOTAL_RATING));
                applicant.setApplicantState(ApplicantState.valueOf(resultSet.getString(Fields.STATE_TYPE).toUpperCase()));
                //applicant.setUserId(resultSet.getInt(Fields.ID));

                applicants.add(applicant);
            }
            return applicants;
        } catch (SQLException e) {
            throw new ApplicantDaoException("could not get applicants by enrollment: " + e.getMessage());
        }
    }

    public boolean updateEnrolledApplicantsState(List<Integer> idList) throws ApplicantDaoException {
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(UPDATE_ENROLLED_APPLICANT_STATE);

            for (int id : idList) {
                preparedStatement.setInt(1, id);
                preparedStatement.execute();
            }
            return true;

        } catch (SQLException e) {
            throw new ApplicantDaoException("could not update enrolled applicants state: " + e.getMessage());
        }
    }

    public boolean updateNotEnrolledApplicantsState() throws ApplicantDaoException {
        try {
            Statement statement = connection.createStatement();
            statement.execute(UPDATE_NOT_ENROLLED_APPLICANTS_STATE);
            return true;
        } catch (SQLException e) {
            throw new ApplicantDaoException("could not update not enrolled applicants state: " + e.getMessage());
        }
    }

    public Map<Faculty, TreeSet<Applicant>> getCurrentEnrollmentApplicants() throws ApplicantDaoException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_LATEST_APPLICANTS);

            Map<Faculty, TreeSet<by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant>> applicants = new HashMap<>();
            Faculty faculty;
            by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant applicant;
            while (resultSet.next()) {
                faculty = new Faculty();
                faculty.setId(resultSet.getInt(Fields.FACULTY_ID));
                faculty.setCapacity(resultSet.getInt(Fields.CAPACITY));

                applicant = new by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant();
                applicant.setId(resultSet.getInt(Fields.APPLICANT_ID));
                applicant.setTotalRating(resultSet.getInt(Fields.TOTAL_RATING));

                if (applicants.containsKey(faculty)) {
                    applicants.get(faculty).add(applicant);
                } else {
                    applicants.put(faculty, new TreeSet<>());
                    applicants.get(faculty).add(applicant);
                }
            }
            return applicants;

        } catch (SQLException e) {
            throw new ApplicantDaoException("could not get current enrollment applicants: " + e.getMessage());
        }
    }


    public int insert(Applicant applicant) throws ApplicantDaoException {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_APPLICANT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, applicant.getUserId());
            preparedStatement.setInt(2, applicant.getFacultyId());
            preparedStatement.setInt(3, applicant.getApplicantState().getOrdinalNumber());
            preparedStatement.setInt(4, applicant.getEnrollmentId());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            int id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }

            return id;
        } catch (SQLException e) {
            throw new ApplicantDaoException("could not insert applicant: " + e.getMessage());
        }
    }

    public List<Applicant> getApplicantsByUserId(int userId) throws ApplicantDaoException {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_APPLICANTS_BY_USER_ID);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Applicant> applicants =new ArrayList<>();
            Applicant applicant;
            while(resultSet.next()){
                applicant =new Applicant();
                applicant.setUserId(userId);
                applicant.setId(resultSet.getInt(Fields.APPLICANT_ID));
                applicant.setEnrollmentId(resultSet.getInt(Fields.ID));
                applicant.setFacultyName(resultSet.getString(Fields.NAME));
                applicant.setApplicantState(ApplicantState.valueOf(resultSet.getString(Fields.STATE_TYPE).toUpperCase()));
                applicants.add(applicant);
            }
            return applicants;
        } catch (SQLException e) {
            throw new ApplicantDaoException("could not get applicants by user id: "+e.getMessage());
        }
    }

    public Applicant getApplicantById(int applicantId) throws ApplicantDaoException {

        try{
            PreparedStatement preparedStatement=connection.prepareStatement(FIND_APPLICANT_BY_ID);
            preparedStatement.setInt(1,applicantId);
            ResultSet resultSet=preparedStatement.executeQuery();
            Applicant applicant=null;
            if(resultSet.next()){
                applicant=new Applicant();
                applicant.setUserId(resultSet.getInt(Fields.ID));
                applicant.setId(applicantId);
                applicant.setEnrollmentId(resultSet.getInt(Fields.ENROLLMENT_ID));
                applicant.setLastName(resultSet.getString(Fields.LAST_NAME));
                applicant.setFirstName(resultSet.getString(Fields.FIRST_NAME));
                applicant.setPatronymic(resultSet.getString(Fields.PATRONYMIC));
                applicant.setFacultyName(resultSet.getString(Fields.NAME));
                applicant.setApplicantState(ApplicantState.valueOf(resultSet.getString(Fields.STATE_TYPE).toUpperCase()));
            }
            return applicant;
        } catch (SQLException e) {
            throw new ApplicantDaoException("could not get applicant by id: "+e.getMessage());
        }
    }

    public boolean deleteApplicantById(int applicantId) throws ApplicantDaoException {

        try{
            PreparedStatement preparedStatement=connection.prepareStatement(DELETE_APPLICANT_BY_ID);
            preparedStatement.setInt(1,applicantId);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new ApplicantDaoException("could not delete applicant by id:" +e.getMessage());
        }
    }

    public List<Applicant> getApplicantsIdByFacultyId(int facultyId) throws ApplicantDaoException {

        try{
            PreparedStatement preparedStatement=connection.prepareStatement(FIND_APPLICANTS_ID_BY_FACULTY_ID);
            preparedStatement.setInt(1,facultyId);
            ResultSet resultSet=preparedStatement.executeQuery();
            List<Applicant> applicants=new ArrayList<>();
            Applicant applicant;
            while (resultSet.next()){
                applicant=new Applicant();
                applicant.setId(resultSet.getInt(Fields.ID));
                applicant.setFacultyName(resultSet.getString(Fields.NAME));
                applicants.add(applicant);
            }
            return applicants;
        } catch (SQLException e) {
            throw new ApplicantDaoException("could not get applicants id by faculty id: "+e.getMessage());
        }


    }
}
