package by.epam.learning.yevtukhovich.admissionsCommittee.util.calculator;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Applicant;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Faculty;

import java.util.*;

public class EnrollmentCalculator {


    public static List<Integer> calculateEnrolledApplicantsId(Map<Faculty, TreeSet<Applicant>> applicants) {

        List<Integer> idList = new ArrayList<>();
        Set<Faculty> faculties = applicants.keySet();
        int count;

        for (Faculty faculty : faculties) {
            count = 0;

            Set<Applicant> currentFacultyApplicants = applicants.get(faculty);
            Iterator<Applicant> iterator = currentFacultyApplicants.iterator();

            while (iterator.hasNext()) {
                idList.add(iterator.next().getId());
                if (++count == faculty.getCapacity()) {
                    break;
                }
            }
        }
        return idList;
    }
}
