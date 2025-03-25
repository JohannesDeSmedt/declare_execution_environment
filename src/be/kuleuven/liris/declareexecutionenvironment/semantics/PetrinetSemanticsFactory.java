package be.kuleuven.liris.declareexecutionenvironment.semantics;

import org.processmining.models.graphbased.directed.petrinet.ResetInhibitorNet;

public class PetrinetSemanticsFactory {

	private PetrinetSemanticsFactory() {

	}

	public static ResetInhibitorNetSemantics regularResetInhibitorNetSemantics(Class<? extends ResetInhibitorNet> net) {
		return new ResetInhibitorNetSemanticsImpl();
	}
	
}
