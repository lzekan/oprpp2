package hr.fer.zemris.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

public class NewtonP1 {
	
	private static Complex[] roots = new Complex[4];
	private static int N = 0;
	private static int numOfTracks = 0;
	private static final int numberOfAvailableProcessors = Runtime.getRuntime().availableProcessors();

	
	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--workers") && N == 0) {
				String[] line = args[i].split("=");
				N = Integer.parseInt(line[1]);
			} else if(args[i].startsWith("-w") && N == 0){
				i++;
				String line = args[i];
				N = Integer.parseInt(line);
			} else if(args[i].startsWith("--tracks") && numOfTracks == 0){
				String[] line = args[i].split("=");
				numOfTracks = Integer.parseInt(line[1]);
			} else if(args[i].startsWith("-t") && numOfTracks == 0){
				i++;
				String line = args[i];
				numOfTracks = Integer.parseInt(line);
			} else {
				throw new IllegalArgumentException();
			}
			
		}
		
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer. \n"
				+ "Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		Scanner sc = new Scanner(System.in);
		String root = "";
		int index = 1;	
		
		System.out.print("Root " + index++ + " > ");
		root = sc.nextLine();
		
		List<Complex> rootsList = new ArrayList<>();
		
		while(!root.equals("done")) {
			System.out.print("Root " + index++ + " > ");
			Complex complex = new Complex();
			
			if(root.equals("1")) {
				complex = Complex.ONE;
			} else if (root.equals("-1")) {
				complex = Complex.ONE_NEG;
			} else if (root.equals("i")) {
				complex = Complex.IM;
			} else if (root.equals("-i")) {
				complex = Complex.IM_NEG;
			} else {
				
				double re, im;
				re = im = 0;
				
				String arg[] = root.split(" ");
				if (arg.length > 3 || arg.length == 2) {
					sc.close();
					throw new IllegalArgumentException();
				}
				
				if(arg.length == 3) {
					re = Integer.parseInt(arg[0]);
					
					if(arg[2].equals("i")) {
						im = 1;
					} else {
						im = Double.parseDouble(arg[2].substring(0, arg[2].length() - 1));
					}
					
					if(arg[1].equals("-")) {
						im = -im;
					}
					
					complex = new Complex(re, im);
					
				} else {
					
					if(arg[0].contains("i")) {
						re = 0;
						im = Double.parseDouble(arg[0].substring(0, arg[0].length() - 1));
					} else {
						re = Double.parseDouble(arg[0]);
						im = 0;
					}
					
					complex = new Complex(re, im);
				}
			
			}
			
			rootsList.add(complex);
			root = sc.nextLine();
			
		}
		
		sc.close();
		
		int indexArray = 0;
		
		for(Complex c : rootsList) {
			roots[indexArray++] = c;
		}
	
		
		FractalViewer.show(new MojProducer());
	
	}
	
	public static class PosaoIzracuna implements Runnable {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		AtomicBoolean cancel;
		int offset;
		
		public static PosaoIzracuna NO_JOB = new PosaoIzracuna();
		
		private PosaoIzracuna() {
			
		}
		
		public PosaoIzracuna(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, AtomicBoolean cancel, int offset) {
			
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
			this.offset = offset;
		}
		
		@Override
		public void run() {
			
			ComplexRootedPolynomial crp;
			ComplexPolynomial f = null;
			ComplexPolynomial fd;
			
			for(int y = yMin; y <= yMax; y++) {
				if(cancel.get()) break;
				for(int x = 0; x < width; x++) {
					double cre = x / (width-1.0) * (reMax - reMin) + reMin;
					double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
					
					Complex c  = new Complex(cre, cim);
					crp = new ComplexRootedPolynomial(c, roots);
					
					f = crp.toComplexPolynom();
					fd = f.derive();
					Complex zn = new Complex(cre, cim);
					
					int iters = 0;
					Complex znold;
					
					do {
						znold = new Complex(zn.getRe(), zn.getIm());
						zn = zn.sub(f.apply(zn).divide(fd.apply(zn)));
						iters++;
					} while(iters < m && (zn.sub(znold)).module() > 0.001);
					
					int index = crp.indexOfClosestRootFor(zn, 0.5);
					data[offset] = (short) (index + 1);
					offset++;
				}
			}
			
		}
	}
	
	
	public static class MojProducer implements IFractalProducer {
		
		private int m;
		private int offset;
		private short[] data;
		private BlockingQueue<Runnable> queue;
		ExecutorService executorService;
	
		
		@Override
		public void setup() {
			
			m = 16*16*16;
			offset = 0;
			
			if (numOfTracks == 0) {
				numOfTracks = 4 * numberOfAvailableProcessors;
			}
			
			queue = new LinkedBlockingQueue<>();
			
			if (N == 0) {
				N = numberOfAvailableProcessors;
			}
			
			executorService = Executors.newFixedThreadPool(N);
		
		}
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			
			
			data = new short[width * height];
			int brojYPoTraci = height / numOfTracks;
			
				
			for(int i = 0; i < N; i++) {
				
				queue.add(new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							Runnable p = null;
							
							try {
								p = queue.take();
								
								if(p == PosaoIzracuna.NO_JOB) {
									break;
								}
								
							} catch (InterruptedException e) {
								continue;
							}
							
							p.run();
						}
					}
				}));
				
			}
			
			///////////////////////////////////odi vrati
			
			for(int i = 0; i < numOfTracks; i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1) * brojYPoTraci - 1;
				
				if(i == numOfTracks-1) {
					yMax = height-1;
				}
				
				offset = brojYPoTraci * width * i;
				
				PosaoIzracuna posao = new PosaoIzracuna(
						reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, offset);
				
				while(true) {
					queue.add(posao);
					break;
				}
			}
			
			for(int i = 0; i < N; i++) {
				while(true) {
					queue.add(PosaoIzracuna.NO_JOB);
					break;
				}
			}
			
			for(int i = 0; i < N; i++) {
				try {
					queue.take().run();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
//			for(int i = 0; i < N; i++) {
//				while(true) {
//					try {
//						radnici[i].join();
//						break;
//					} catch (InterruptedException e) {
//						
//					}
//					
//				}
//			}
			
			System.out.printf("Started with %d workers and %d tracks\n", N, numOfTracks);
			observer.acceptResult(data, (short) (roots.length + 1), requestNo);
		}

		@Override
		public void close() {
			executorService.shutdown();
			
		}


	}
	
	

}
