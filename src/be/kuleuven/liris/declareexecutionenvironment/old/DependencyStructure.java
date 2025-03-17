package be.kuleuven.liris.declareexecutionenvironment.old;

/** Author: Johannes De Smedt
 *  E-mail: johannes.desmedt@kuleuven.be
 *  October 2015
 */


import java.util.Collection;
import java.util.HashSet;


import org.processmining.framework.util.Pair;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;


public class DependencyStructure {

	private Pair<Transition,Place> p;
	private HashSet<Place> bwP;
	private HashSet<Transition> Tr;
	private HashSet<DependencyStructure> dp;
	private HashSet<Pair<Place,Transition>> ptt;
	private String type;
	private Place unPlace;
	private Transition nsTrans;
	private Integer numConst;
	private boolean permDisabled;
	private String caption;
	
	public String toString(){
		String text = "";
		text = text + "\nFor t "+ p.getFirst().getLabel() +" and p "+p.getSecond().getLabel()+" of type "+type;
		text = text + "\nPlaces "+bwP.toString();
		text = text + "\nDepth "+dp.size();
		for(DependencyStructure t: dp){
			text = text + "\nGoing down";
			text = text + t.toString();
		}				
		return text;		
	}
	

	public DependencyStructure(Pair<Transition,Place> pIn, String typeIn, Place un, Transition t2){
		p = pIn;
		bwP = new HashSet<Place>();	
		Tr = new HashSet<Transition>();
		dp = new HashSet<DependencyStructure>();
		ptt = new HashSet<Pair<Place,Transition>>();
		type = typeIn;
		numConst = 1;
		permDisabled = false;
		if(type=="un")
			unPlace = un;
		if(type=="ns")
			nsTrans = t2;
	}	
		
	public DependencyStructure clone(DependencyStructure in){
		DependencyStructure newDP = new DependencyStructure(in.p,in.type,in.unPlace,nsTrans);
		newDP.bwP.addAll(in.bwP);
		newDP.ptt.addAll(in.ptt);
		newDP.Tr.addAll(in.getTr());
		for(DependencyStructure d: dp){
			newDP.dp.add(d.clone(d));
		}
		return newDP;
	}
	
	public void setDis(boolean perm){
		permDisabled = perm;
	}
	
	public void addT(Transition t){
		Tr.add(t);
	}
	
	public HashSet<Transition> getTr(){
		return Tr;
	}
	
	public void setT(Transition t){
		this.p = new Pair<Transition,Place>(t,this.p.getSecond());
	}
	
	public Pair<Transition,Place> getPair(){
		return this.p;
	}
	
	public boolean getDis(){
		return permDisabled;
	}
	public String getType(){
		return type;
	}
	
	public Integer numConstr(){
		return numConst;
	}
	
	public void setType(String typeIn){
		type = typeIn;
	}
	
	public boolean isUn(){
		if(type=="un")
			return true;
		else
			return false;
	}
	
	public boolean isNS(){
		if(type=="ns")
			return true;
		else
			return false;
	}
	
	public boolean isEx(){
		if(type=="ex")
			return true;
		else
			return false;
	}
	
	public void setCaption(String in){
		caption=in;
	}
	
	public String getCaption(){
		return caption;
	}
	
	public void addPlaceBw(Place p, Transition t){
		numConst++;
		bwP.add(p);
		ptt.add(new Pair<Place,Transition>(p,t));
	}
	
	public void addPlacesBw(HashSet<Place> hsP){
		bwP.addAll(hsP);
	}

	public void addDepStruc(DependencyStructure dpIn){
		numConst += dpIn.numConst;
		dp.add(dpIn);
	}
	
	public Transition getTforP(Place p){
		for(Pair<Place,Transition> pair: ptt)
			if(pair.getFirst().equals(p))
				return pair.getSecond();
		return null;
	}
	
	public HashSet<DependencyStructure> getDP(){
		return dp;
	}
	
	public Collection<Place> getPlaces(){
		return bwP;
	}	
	
	public Transition getT(){
		return p.getFirst();
	}
	
	public Transition getTs(){
		return nsTrans;
	}
	
	public void setUP(Place inP){
		unPlace = inP;
	}
	
	public HashSet<Pair<Place,Transition>> getPtt(){
		return ptt;
	}
	
	public Place getUP(){
		return unPlace;
	}
	
	public Place getP(){
		return p.getSecond();
	}
	
}
