package tn.fst.exam_manager.domain.enumeration;

/**
 * The Rank enumeration.
 * Each rank has a corresponding number of hours for supervising exams.
 */
public enum Rank {
    MAITRE_ASSISTANT(10),
    ASSISTANT(8),
    VACATAIRE(5),
    MAITRE_CONF(12),
    PROFESSEUR(15),
    EXPERT(20),
    PES(18),
    CONTRACTUEL(6);

    private final int supervisionHours;

    Rank(int supervisionHours) {
        this.supervisionHours = supervisionHours;
    }

    /**
     * Gets the number of hours required for supervising exams.
     *
     * @return the number of supervision hours
     */
    public int getSupervisionHours() {
        return supervisionHours;
    }
}
