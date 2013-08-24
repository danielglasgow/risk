package risk;

/**
 * The phases a human player completes during different MainPhases.
 */
public enum SubPhase {
    PLACE_ARMIES, // Place Armies SubPhase
    SELECT_DEFENDING_TERRITORY, SELECT_ATTACKING_TERRITORY, BATTLE, WON_TERRITORY, // Attack
                                                                                   // SubPhases
    FORTIFY_SELECTION, FORTIFY, // Fortify SubPhases
    EDIT, END_SUB_PHASE,
}
