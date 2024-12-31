package tn.fst.exam_manager.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.fst.exam_manager.domain.Classroom;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.Institute;
import tn.fst.exam_manager.domain.UserAcademicInfo;
import tn.fst.exam_manager.repository.ClassroomRepository;
import tn.fst.exam_manager.repository.DepartmentRepository;
import tn.fst.exam_manager.repository.ExamRepository;
import tn.fst.exam_manager.repository.ExamSessionRepository;
import tn.fst.exam_manager.repository.InstituteRepository;
import tn.fst.exam_manager.repository.MajorRepository;
import tn.fst.exam_manager.repository.ProfessorDetailsRepository;
import tn.fst.exam_manager.repository.ReportRepository;
import tn.fst.exam_manager.repository.SessionTypeRepository;
import tn.fst.exam_manager.repository.StudentClassRepository;
import tn.fst.exam_manager.repository.TeachingSessionRepository;
import tn.fst.exam_manager.repository.TimetableRepository;
import tn.fst.exam_manager.repository.UserAcademicInfoRepository;
import tn.fst.exam_manager.security.SecurityUtils;

@Service
@AllArgsConstructor
public class SecurityService {

    private final UserAcademicInfoRepository userAcademicInfoRepository;
    private final DepartmentRepository departmentRepository;
    private final ClassroomRepository classroomRepository;
    private final ExamRepository examRepository;
    private final ExamSessionRepository examSessionRepository;
    private final InstituteRepository instituteRepository;
    private final MajorRepository majorRepository;
    private final ProfessorDetailsRepository professorDetailsRepository;
    private final ReportRepository reportRepository;
    private final SessionTypeRepository sessionTypeRepository;
    private final StudentClassRepository studentClassRepository;
    private final TeachingSessionRepository teachingSessionRepository;
    private final TimetableRepository timetableRepository;

    public boolean isInSameInstitute(Long instituteId, UserAcademicInfo userAcademicInfo) {
        if (instituteId == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        if (userAcademicInfo == null) {
            throw new IllegalArgumentException("The given userAcademicInfo must not be null");
        }

        return userAcademicInfo.getInstitute().getId().equals(instituteId);
    }

    public boolean isInSameDepartment(Long departmentId, UserAcademicInfo userAcademicInfo) {
        if (departmentId == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        if (userAcademicInfo == null) {
            throw new IllegalArgumentException("The given userAcademicInfo must not be null");
        }

        return userAcademicInfo.getDepartment().getId().equals(departmentId);
    }

    public UserAcademicInfo getCurrentUserAcademicInfo() {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<UserAcademicInfo> userAcademicInfo = userAcademicInfoRepository.findByUserLogin(login);
        return userAcademicInfo.orElse(null);
    }

    public Long getCurrentUserDepartmentId() {
        return Optional.ofNullable(getCurrentUserAcademicInfo()).map(UserAcademicInfo::getDepartment).map(Department::getId).orElse(null);
    }

    public Long getCurrentUserInstituteId() {
        return Optional.ofNullable(getCurrentUserAcademicInfo()).map(UserAcademicInfo::getInstitute).map(Institute::getId).orElse(null);
    }

    public boolean isAuthorizedToReadClassroom(Long classroomId) {
        UserAcademicInfo userAcademicInfo = getCurrentUserAcademicInfo();

        if (userAcademicInfo == null) {
            return false;
        }

        Classroom classroom = classroomRepository
            .findById(classroomId)
            .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

        if (SecurityUtils.isCurrentUserInstituteAdmin() || SecurityUtils.isCurrentUserDepartmentAdmin()) {
            return isInSameInstitute(classroom.getDepartment().getInstitute().getId(), userAcademicInfo);
        }

        return false;
    }

    public boolean isAuthorizedToWriteClassroom(Long classroomId) {
        UserAcademicInfo userAcademicInfo = getCurrentUserAcademicInfo();

        if (userAcademicInfo == null) {
            return false;
        }

        Classroom classroom = classroomRepository
            .findById(classroomId)
            .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

        if (SecurityUtils.isCurrentUserInstituteAdmin()) {
            return isInSameInstitute(classroom.getDepartment().getInstitute().getId(), userAcademicInfo);
        }

        if (SecurityUtils.isCurrentUserDepartmentAdmin()) {
            return isInSameDepartment(classroom.getDepartment().getId(), userAcademicInfo);
        }

        return false;
    }
}
