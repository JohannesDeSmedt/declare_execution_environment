package be.kuleuven.liris.declareexecutionenvironment.semantics;

import org.processmining.models.graphbased.directed.petrinet.InhibitorNet;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.ResetInhibitorNet;
import org.processmining.models.graphbased.directed.petrinet.ResetNet;
import org.processmining.models.semantics.petrinet.InhibitorNetSemantics;
import org.processmining.models.semantics.petrinet.PetrinetSemantics;
import org.processmining.models.semantics.petrinet.ResetNetSemantics;
import org.processmining.models.semantics.petrinet.impl.ElementaryInhibitorNetSemanticsImpl;
import org.processmining.models.semantics.petrinet.impl.ElementaryPetrinetSemanticsImpl;
import org.processmining.models.semantics.petrinet.impl.ElementaryResetInhibitorNetSemanticsImpl;
import org.processmining.models.semantics.petrinet.impl.ElementaryResetNetSemanticsImpl;
import org.processmining.models.semantics.petrinet.impl.InhibitorNetSemanticsImpl;
import org.processmining.models.semantics.petrinet.impl.PetrinetSemanticsImpl;
import org.processmining.models.semantics.petrinet.impl.ResetInhibitorNetSemanticsImpl;
import org.processmining.models.semantics.petrinet.impl.ResetNetSemanticsImpl;

public class PetrinetSemanticsFactory {

	private PetrinetSemanticsFactory() {

	}

	public static PetrinetSemantics regularPetrinetSemantics(Class<? extends Petrinet> net) {
		return new PetrinetSemanticsImpl();
	}

	public static PetrinetSemantics elementaryPetrinetSemantics(Class<? extends Petrinet> net) {
		return new ElementaryPetrinetSemanticsImpl();
	}

	public static ResetNetSemantics regularResetNetSemantics(Class<? extends ResetNet> net) {
		return new ResetNetSemanticsImpl();
	}

	public static ResetNetSemantics elementaryResetNetSemantics(Class<? extends ResetNet> net) {
		return new ElementaryResetNetSemanticsImpl();
	}

	public static InhibitorNetSemantics regularInhibitorNetSemantics(Class<? extends InhibitorNet> net) {
		return new InhibitorNetSemanticsImpl();
	}

	public static InhibitorNetSemantics elementaryInhibitorNetSemantics(Class<? extends InhibitorNet> net) {
		return new ElementaryInhibitorNetSemanticsImpl();
	}

	public static ResetInhibitorNetSemantics regularResetInhibitorNetSemantics(Class<? extends ResetInhibitorNet> net) {
		return new ResetInhibitorNetSemanticsImpl();
	}

	public static ResetInhibitorNetSemantics elementaryResetInhibitorNetSemantics(Class<? extends ResetInhibitorNet> net) {
		return new ElementaryResetInhibitorNetSemanticsImpl();
	}
	
	public static DeclareSemanticsImpl riNetDeclareSemantics(Class<? extends ResetInhibitorNet> net) {
		return new DeclareSemanticsImpl();
	}
	
}
