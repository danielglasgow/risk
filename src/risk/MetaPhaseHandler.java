package risk;

import java.util.Set;

import com.google.common.collect.Sets;

public abstract class MetaPhaseHandler {

    private final BoardState boardState;
    private final InstructionPanel instructionPanel;
    private final Player player;
    private final HumanTurnPhases nextPhase;
    private final Set<HumanPhaseHandler> subPhaseHandlers = Sets.newHashSet();

    private HumanTurnPhases subPhase;

    public MetaPhaseHandler(BoardState boardState, InstructionPanel instructionPanel,
            Player player, HumanTurnPhases firstPhase, HumanTurnPhases nextPhase) {
        this.boardState = boardState;
        this.instructionPanel = instructionPanel;
        this.player = player;
        this.subPhase = firstPhase;
        this.nextPhase = nextPhase;
    }

    protected void setSubPhaseHanlders(Set<HumanPhaseHandler> subPhaseHandlers) {
        this.subPhaseHandlers.addAll(subPhaseHandlers);
    }

    public HumanTurnPhases runPhase() {
        while (true) {
            if (subPhase == HumanTurnPhases.END_SUB_PHASE) {
                break;
            }
            for (HumanPhaseHandler phaseHandler : subPhaseHandlers) {
                if (subPhase == phaseHandler.getPhaseType()) {
                    handlePhase(phaseHandler);
                }
            }
        }
        return nextPhase;
    }

    private void handlePhase(HumanPhaseHandler phaseHandler) {
        boardState.getBoard().getMouse().setPhaseHandler(phaseHandler);
        phaseHandler.displayInterface();
        subPhase = phaseHandler.await();
    }

}
