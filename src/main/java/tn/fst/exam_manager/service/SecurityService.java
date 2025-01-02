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
import tn.fst.exam_manager.service.dto.ClassroomDTO;
import tn.fst.exam_manager.service.dto.DepartmentDTO;

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

    public boolean isAuthorizedToWriteClassroom(ClassroomDTO classroomDTO) {
        UserAcademicInfo userAcademicInfo = getCurrentUserAcademicInfo();

        if (userAcademicInfo == null) {
            return false;
        }

        // verify that the data in the classroomDTO is within the same
        // institute/department as the user
        if (SecurityUtils.isCurrentUserDepartmentAdmin()) {
            if (!isInSameDepartment(classroomDTO.getDepartment().getId(), userAcademicInfo)) {
                return false;
            }
        }

        if (SecurityUtils.isCurrentUserInstituteAdmin()) {
            Long classroomDepartmentId = classroomDTO.getDepartment().getId();
            Long classroomInstituteId = departmentRepository
                .findById(classroomDepartmentId)
                .map(Department::getInstitute)
                .map(Institute::getId)
                .orElse(null);
            if (!isInSameInstitute(classroomInstituteId, userAcademicInfo)) {
                return false;
            }
        }

        if (classroomDTO.getId() == null) {
            return true;
        }

        Classroom classroom = classroomRepository
            .findById(classroomDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Classroom not found"));

        if (SecurityUtils.isCurrentUserInstituteAdmin()) {
            return isInSameInstitute(classroom.getDepartment().getInstitute().getId(), userAcademicInfo);
        }

        if (SecurityUtils.isCurrentUserDepartmentAdmin()) {
            return isInSameDepartment(classroom.getDepartment().getId(), userAcademicInfo);
        }

        return false;
    }

    public boolean isAuthorizedToDeleteClassroom(Long classroomId) {
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

    public boolean isAuthorizedToReadDepartment(Long departmentId) {
        UserAcademicInfo userAcademicInfo = getCurrentUserAcademicInfo();

        if (userAcademicInfo == null) {
            return false;
        }

        Department department = departmentRepository
            .findById(departmentId)
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        if (SecurityUtils.isCurrentUserInstituteAdmin() || SecurityUtils.isCurrentUserDepartmentAdmin()) {
            return isInSameInstitute(department.getInstitute().getId(), userAcademicInfo);
        }

        return false;
    }

    public boolean isAuthorizedToWriteDepartment(DepartmentDTO departmentDTO) {
        UserAcademicInfo userAcademicInfo = getCurrentUserAcademicInfo();

        if (userAcademicInfo == null) {
            return false;
        }

        if (SecurityUtils.isCurrentUserDepartmentAdmin()) {
            if (!isInSameDepartment(departmentDTO.getId(), userAcademicInfo)) {
                return false;
            }
        }

        if (SecurityUtils.isCurrentUserInstituteAdmin()) {
            Long departmentInstituteId = departmentRepository
                .findById(departmentDTO.getId())
                .map(Department::getInstitute)
                .map(Institute::getId)
                .orElse(null);
            if (!isInSameInstitute(departmentInstituteId, userAcademicInfo)) {
                return false;
            }
        }

        if (departmentDTO.getId() == null) {
            return true;
        }

        Department department = departmentRepository
            .findById(departmentDTO.getId())
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        if (SecurityUtils.isCurrentUserInstituteAdmin()) {
            return isInSameInstitute(department.getInstitute().getId(), userAcademicInfo);
        }

        if (SecurityUtils.isCurrentUserDepartmentAdmin()) {
            return isInSameDepartment(department.getId(), userAcademicInfo);
        }

        return false;
    }

    public boolean isAuthorizedToDeleteDepartment(Long departmentId) {
        UserAcademicInfo userAcademicInfo = getCurrentUserAcademicInfo();

        if (userAcademicInfo == null) {
            return false;
        }

        Department department = departmentRepository
            .findById(departmentId)
            .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        if (SecurityUtils.isCurrentUserInstituteAdmin()) {
            return isInSameInstitute(department.getInstitute().getId(), userAcademicInfo);
        }

        return false;
    }
}
