package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfessorDetailsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorDetailsAllPropertiesEquals(ProfessorDetails expected, ProfessorDetails actual) {
        assertProfessorDetailsAutoGeneratedPropertiesEquals(expected, actual);
        assertProfessorDetailsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorDetailsAllUpdatablePropertiesEquals(ProfessorDetails expected, ProfessorDetails actual) {
        assertProfessorDetailsUpdatableFieldsEquals(expected, actual);
        assertProfessorDetailsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorDetailsAutoGeneratedPropertiesEquals(ProfessorDetails expected, ProfessorDetails actual) {
        assertThat(expected)
            .as("Verify ProfessorDetails auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorDetailsUpdatableFieldsEquals(ProfessorDetails expected, ProfessorDetails actual) {
        assertThat(expected)
            .as("Verify ProfessorDetails relevant properties")
            .satisfies(e -> assertThat(e.getRank()).as("check rank").isEqualTo(actual.getRank()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertProfessorDetailsUpdatableRelationshipsEquals(ProfessorDetails expected, ProfessorDetails actual) {
        assertThat(expected)
            .as("Verify ProfessorDetails relationships")
            .satisfies(e -> assertThat(e.getSupervisedExams()).as("check supervisedExams").isEqualTo(actual.getSupervisedExams()))
            .satisfies(e -> assertThat(e.getReport()).as("check report").isEqualTo(actual.getReport()))
            .satisfies(e -> assertThat(e.getTimetable()).as("check timetable").isEqualTo(actual.getTimetable()));
    }
}