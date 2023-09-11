package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.List;

public class ComplexPolynomial {

	public List<Complex> factors;
	
	public ComplexPolynomial(Complex ...factors) {
		
		int index = 0;
		Complex arrFactors[] = new Complex[factors.length];
		
		for(Complex factor : factors) {
			arrFactors[factors.length - 1 - index++] = factor;
		}
		
		this.factors = Arrays.asList(arrFactors);
	}
	
	public short order() {
		return (short) (factors.size() - 1);
	}
	
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		
		int newOrder = this.order() + p.order() + 1;
		Complex newFactors[] = new Complex[newOrder];
		
		for(int i = newOrder - 1; i > 0; i--) {
			
			Complex factor = Complex.ZERO;
			
			for(int j = 0; j < this.order(); j++) {
				
				for(int g = 0; g < p.order(); g++) {
					
					if(j + g == i) {
						factor = factor.add(factors.get(j).multiply(p.factors.get(g)));
					}
				}
				
			}
			
			newFactors[i] = factor;

		}
		
		return new ComplexPolynomial(newFactors);
		
	}
	
	public Complex apply(Complex c) {
		
		Complex result = new Complex();
		int counter = 0;
		
		for(Complex factor : factors) {
			
			result = result.add(factor.multiply(c.power(factors.size() - 1 - counter++)));
			
		}
		
		return result;
	}
	
	public ComplexPolynomial derive() {
		
//		for(Complex factor : factors) {
//			System.out.println(factor);
//		}
		
		Complex arrFactors[] = new Complex[factors.size() - 1];
		
		for(int i = factors.size() - 2; i >= 0; i--) {
			arrFactors[i] = factors.get(factors.size() - 2 - i);
			arrFactors[i] = arrFactors[i].multiply(new Complex(i + 1, 0));
		}
		
		//System.out.println("Derivation:");
		
		return new ComplexPolynomial(arrFactors);
		
	}
	
	
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < factors.size(); i++) {
			sb.append("(" + factors.get(i) + ")*z^" + (factors.size() - 1 - i) + " + ");
		}
		
		
		return sb.toString().substring(0, sb.length() - 7);
	
	}
	
	
	
}
