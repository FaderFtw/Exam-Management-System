package tn.fst.exam_manager.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.fst.exam_manager.domain.Department;
import tn.fst.exam_manager.domain.User;
import tn.fst.exam_manager.domain.UserAcademicInfo;
import tn.fst.exam_manager.repository.DepartmentRepository;
import tn.fst.exam_manager.repository.UserAcademicInfoRepository;
import tn.fst.exam_manager.security.SecurityUtils;

@Service
public class SecurityService {

    @Autowired
    private UserAcademicInfoRepository userAcademicInfoRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public boolean isInSameInstitute(Long instituteId) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<UserAcademicInfo> userAcademicInfo = userAcademicInfoRepository.findByUserLogin(login);

        return userAcademicInfo
            .map(info -> {
                return info.getInstitute().getId().equals(instituteId);
            })
            .orElse(false);
    }

    public boolean isInSameInstituteFromDepartment(Long departmentId) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<UserAcademicInfo> userAcademicInfo = userAcademicInfoRepository.findByUserLogin(login);
        if (departmentId == null) {
            throw new IllegalArgumentException("The given id must not be null");
        }
        Optional<Department> department = departmentRepository.findById(departmentId);

        if (department.isEmpty()) {
            return false;
        }

        Long instituteId = department.get().getInstitute().getId();

        return userAcademicInfo
            .map(info -> {
                return info.getInstitute().getId().equals(instituteId);
            })
            .orElse(false);
    }

    public boolean isInSameDepartment(Long departmentId) {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<UserAcademicInfo> userAcademicInfo = userAcademicInfoRepository.findByUserLogin(login);
        return userAcademicInfo
            .map(info -> {
                return info.getDepartment().getId().equals(departmentId);
            })
            .orElse(false);
    }
}
