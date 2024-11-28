package tn.fst.exam_manager.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TeachingSessionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachingSessionAllPropertiesEquals(TeachingSession expected, TeachingSession actual) {
        assertTeachingSessionAutoGeneratedPropertiesEquals(expected, actual);
        assertTeachingSessionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachingSessionAllUpdatablePropertiesEquals(TeachingSession expected, TeachingSession actual) {
        assertTeachingSessionUpdatableFieldsEquals(expected, actual);
        assertTeachingSessionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachingSessionAutoGeneratedPropertiesEquals(TeachingSession expected, TeachingSession actual) {
        assertThat(expected)
            .as("Verify TeachingSession auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachingSessionUpdatableFieldsEquals(TeachingSession expected, TeachingSession actual) {
        assertThat(expected)
            .as("Verify TeachingSession relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getStartHour()).as("check startHour").isEqualTo(actual.getStartHour()))
            .satisfies(e -> assertThat(e.getEndHour()).as("check endHour").isEqualTo(actual.getEndHour()))
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachingSessionUpdatableRelationshipsEquals(TeachingSession expected, TeachingSession actual) {
        // empty method
    }
}
