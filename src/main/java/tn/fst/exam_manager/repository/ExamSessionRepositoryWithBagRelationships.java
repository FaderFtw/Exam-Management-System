package tn.fst.exam_manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import tn.fst.exam_manager.domain.ExamSession;

public interface ExamSessionRepositoryWithBagRelationships {
    Optional<ExamSession> fetchBagRelationships(Optional<ExamSession> examSession);

    List<ExamSession> fetchBagRelationships(List<ExamSession> examSessions);

    Page<ExamSession> fetchBagRelationships(Page<ExamSession> examSessions);
}
