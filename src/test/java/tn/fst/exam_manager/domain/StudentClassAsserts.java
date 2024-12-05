package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentClassAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassAllPropertiesEquals(StudentClass expected, StudentClass actual) {
        assertStudentClassAutoGeneratedPropertiesEquals(expected, actual);
        assertStudentClassAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassAllUpdatablePropertiesEquals(StudentClass expected, StudentClass actual) {
        assertStudentClassUpdatableFieldsEquals(expected, actual);
        assertStudentClassUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassAutoGeneratedPropertiesEquals(StudentClass expected, StudentClass actual) {
        assertThat(expected)
            .as("Verify StudentClass auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassUpdatableFieldsEquals(StudentClass expected, StudentClass actual) {
        assertThat(expected)
            .as("Verify StudentClass relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getStudentCount()).as("check studentCount").isEqualTo(actual.getStudentCount()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassUpdatableRelationshipsEquals(StudentClass expected, StudentClass actual) {
        assertThat(expected)
            .as("Verify StudentClass relationships")
            .satisfies(e -> assertThat(e.getExam()).as("check exam").isEqualTo(actual.getExam()))
            .satisfies(e -> assertThat(e.getTeachingSession()).as("check teachingSession").isEqualTo(actual.getTeachingSession()));
    }
}