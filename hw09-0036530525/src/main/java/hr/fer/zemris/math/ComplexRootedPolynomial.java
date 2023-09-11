package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComplexRootedPolynomial {

	private Complex constant;
	private List<Complex> roots;
	
	public ComplexRootedPolynomial(Complex constant, Complex ...roots) {
		this.constant = constant;
		this.roots = Arrays.asList(roots);
	}
	
	public Complex apply(Complex z) {
		
		List<Complex> factors = new ArrayList<>();
		
		for(Complex root : roots) {
			factors.add(z.add(root.negate()));
		}
		
		Complex result = constant;
		
		for(Complex factor : factors) {
			result = result.multiply(factor);
		}
		
		return result;
	}
	
	public ComplexPolynomial toComplexPolynom() {
		
		Complex factors[] = new Complex[roots.size() + 1];
		factors[0] = constant;
		
		int size = 1;
		
		for(Complex root : roots) {
			
			for(int i = factors.length - 1; i > 0; i--) {
				factors[i] = factors[i - 1];
			}
			
			size++;
			factors[0] = factors[0].multiply(root.negate());
			
			for(int i = 1; i < size - 1; i++) {
				factors[i] = factors[i].add(factors[i+1].multiply(root.negate()));
			}
			
		}
		
		Complex[] factorsReversed = factors;
		return new ComplexPolynomial(factorsReversed);
		
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append("(" + constant + ") *");
		
		for(Complex root : roots) {
			sb.append("(z-(" + root.toString() + "))");
		}
		
		return sb.toString();
	
	}
	
	public int indexOfClosestRootFor(Complex z, double treshold) {
		
		int index = -1;
		double minDistance = Integer.MAX_VALUE;
		
		for(int i = 0; i < roots.size(); i++) {
			
			double re = z.getRe() - roots.get(i).getRe();
			double im = z.getIm() - roots.get(i).getIm();
			
			double distance = Math.sqrt(re * re + im * im);
			
			System.out.println("-----EVO ME U FORU PROVJERAVAN JEL " + distance + " MANJI OD " + treshold);
			if(distance <= treshold && distance < minDistance) {
				index = i;
			}
			
		}
		
		System.out.println("EVO INDEX MI JE SAD " + index);
		return index;
		
	}
	
}
