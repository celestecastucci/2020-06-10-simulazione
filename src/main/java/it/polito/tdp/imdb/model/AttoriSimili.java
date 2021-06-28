package it.polito.tdp.imdb.model;

public class AttoriSimili implements Comparable<AttoriSimili> {

	private Actor a;
	private Double peso;
	public AttoriSimili(Actor a, Double peso) {
		super();
		this.a = a;
		this.peso = peso;
	}
	public Actor getA() {
		return a;
	}
	public void setA(Actor a) {
		this.a = a;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(AttoriSimili o) {
		// TODO Auto-generated method stub
		return this.getA().getLastName().compareTo(o.getA().getLastName());
	}
	@Override
	public String toString() {
		return a.getLastName()+" || "+peso;
	}
	
	
	
	
	
	
}
