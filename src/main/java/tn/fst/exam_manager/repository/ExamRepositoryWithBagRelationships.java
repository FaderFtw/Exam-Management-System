package tn.fst.exam_manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import tn.fst.exam_manager.domain.Exam;

public interface ExamRepositoryWithBagRelationships {
    Optional<Exam> fetchBagRelationships(Optional<Exam> exam);

    List<Exam> fetchBagRelationships(List<Exam> exams);

    Page<Exam> fetchBagRelationships(Page<Exam> exams);
}
