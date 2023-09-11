package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

public class Complex {
	
	private double re, im;
	
	public static final Complex ZERO = new Complex(0,0); 
	public static final Complex ONE = new Complex(1,0); 
	public static final Complex ONE_NEG = new Complex(-1,0); 
	public static final Complex IM = new Complex(0,1); 
	public static final Complex IM_NEG = new Complex(0,-1);
	
	public Complex() {
		this(0, 0);
	}
	
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	public double module() {
		return Math.sqrt(re * re + im * im);
	}
	
	public double getRe() {
		return this.re;
	}
	
	public double getIm() {
		return this.im;
	}
	
	public Complex multiply(Complex c) {
		double re = this.re * c.getRe() - this.im * c.getIm();
		double im = this.im * c.getRe() + this.re * c.getIm();
		
		return new Complex(re, c.im != 0 && this.im != 0 ? -im : im);
	}
	
	public Complex divide(Complex c) {
		double re = (this.re * c.getRe() + this.im * c.getIm()) / (c.getRe() * c.getRe() + c.getIm() * c.getIm());
		double im = (this.im * c.getRe() - this.re * c.getIm()) / (c.getRe() * c.getRe() + c.getIm() * c.getIm());
		
		return new Complex(re, im);
	}
	
	public Complex add(Complex c) {
		double re = this.re + c.getRe();
		double im = this.im + c.getIm();
		
		return new Complex(re, im);
	}
	
	public Complex sub(Complex c) {
		double re = this.re - c.getRe();
		double im = this.im - c.getIm();
		
		return new Complex(re, im);
	}
	
	public Complex negate() {
		return new Complex(-this.re, -this.im);
	}
	
	public Complex power(int n) {
		
		Complex c = new Complex(this.re, this.im);
		
		for(int i = 0; i < n-1; i++) {
			c = c.multiply(this);
		}
		
		return c;
		
	}
	
	public List<Complex> root(int n){
		
		List<Complex> roots = new ArrayList<>();
		
		double pow = 1.0 / (n * 1.0);
		
		double r = Math.sqrt(re * re + im * im);
        double angle = Math.atan(im/re); 
        
        if(re < 0) {
        	angle -= Math.PI;
        }
        
        System.out.println(r + " " + angle);
        
        for (int k = 0; k < n; k++) {
        	
            double rootAngle = (angle + 2 * Math.PI * k) / n;
            double rootRe = Math.pow(r, pow) * Math.cos(rootAngle);
            double rootIm = Math.pow(r, pow) * Math.sin(rootAngle);
            roots.add(new Complex(rootRe, rootIm));
        
        }

        return roots;
		
	}
	
	@Override
	public String toString() {
		return this.re + (this.im >= 0 ? " + " + this.im + "i" : " - " + (-this.im) + "i");
	}
	
	
	
	
	

}
