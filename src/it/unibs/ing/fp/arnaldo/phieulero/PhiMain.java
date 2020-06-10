package it.unibs.ing.fp.arnaldo.phieulero;

import java.util.Vector;

import it.unibs.fp.mylib.InputDati;

public class PhiMain {
	
	static byte[][] mcdTable;
	static int N;

	public static void main(String[] args) {
		
		System.out.println("Calcolatore di phi(n)\n");
		N = InputDati.leggiIntero("Inserisci il numero n: ");
				
		long time = System.currentTimeMillis();
		System.out.println("\nIterative phi(n) = " + iterativePhi(N) + "\t\t\t\t\tElapsed time: " + (System.currentTimeMillis() - time) + " ms");
		
		if (N < 10000) {
			mcdTable = new byte[N + 1][N + 1];
			time = System.currentTimeMillis();
			System.out.println("\nV_3 (Dynamic) Recursive phi(n) = " + thirdRecursivePhi(N) + "\t\t\tElapsed time: "
					+ (System.currentTimeMillis() - time) + " ms");
		}
		
		time = System.currentTimeMillis();
		System.out.println("\nV_2 Recursive phi(n) = " + secondRecursivePhi(N) + "\t\t\t\tElapsed time: " + (System.currentTimeMillis() - time) + " ms");
		
		time = System.currentTimeMillis();
		System.out.println("\nV_1 Recursive phi(n) = " + recursivePhi(N) + "\t\t\t\tElapsed time: " + (System.currentTimeMillis() - time) + " ms");
		
		time = System.currentTimeMillis();
		System.out.println("\nV_1.1 Recursive phi(n) = " + invertedRecursivePhi(N) + "\t\t\t\tElapsed time: " + (System.currentTimeMillis() - time) + " ms");
		
		
	}
	
	
	public static Vector<Integer> primeList(int n) { // Erastotene algorithm
		Vector<Integer> primelist = new Vector<Integer>();
		if (n == 1) {
			primelist.add(n);
		}
		for (int i = 1; i < n; i++) { // write numbers from 2 to n
			primelist.add((i + 1));
		}
		int temp = 0;
		while (primelist.get(temp) < Math.sqrt(n)) {
			for (int i = temp + 1; i < primelist.size(); i++) {
				if (primelist.get(i) % primelist.get(temp) == 0) { // remove multiples
					primelist.remove(i);
					i--;
				}
			} temp++;
		}
		return primelist; // returns a list of prime numbers up to n
	}

	// RECURSIVE SOLUTION // (Warning: Very Inefficient!) [Try 99999 vs. 100001 and see what happens :p]
	
	public static int recursivePhi(int n) {
		if (n == 1) return 1;
		int arr[] = getCoprimeFactors(n);
		if (arr != null) {
			return recursivePhi(arr[0]) * recursivePhi(arr[1]);
		} else {
			arr = getP_K(n); // arr[0] is p, arr[1] is k
			return ((arr[0] - 1) * power(arr[0], arr[1] - 1));
		}
	}
	
	public static int mcd(int i, int n) { // GCD calculation (Greatest Common Divisor) [MCD in italiano]
		if (n == 0) {
			return i;
		} else {
			return mcd(n, i % n);
		}
	}
	
	public static boolean areCoprimes(int i, int n) {
		if (mcd(i, n) == 1) return true;
		return false;
	}
	
	public static int[] getCoprimeFactors(int n) {
		for (int i = 1; i < n; i++) {
			for (int j = 1; j < n; j++) {
				if (areCoprimes(i, j) && i*j == n) {
					int arr[] = {i, j};
					return arr;
				}
			}
		} return null;
	}
	
	
	public static int power(int p, int l) {
		if (l == 0) return 1;
		int res = p;
		for (int k = 1; k < l; k++)
			res *= p;
		return res;
	}

	public static int[] getP_K(int n) {
		
		Vector<Integer> primelist = primeList(n);
		for (int p : primelist) {
			int k = 0;
			while (power(p, k) <= n) {
				if (power(p ,k) == n) {
					int arr[] = {p, k};
					return arr;
				} k++;
			}
		} return null;
	}
	
	// INVERTED METHOD CALL RECURSIVE SOLUTION //
	
	public static int invertedRecursivePhi(int n) {
		if (n == 1) return 1;
		int arr[] = getP_K(n); // arr[0] is p, arr[1] is k
		if (arr != null) {
			return ((arr[0] - 1) * power(arr[0], arr[1] - 1));
			
		} else {
			arr = getCoprimeFactors(n);
			return recursivePhi(arr[0]) * recursivePhi(arr[1]);
		}
	}
	
	// SECOND RECURSIVE SOLUTION // (way faster than first)
	
	public static int secondRecursivePhi(int n) { // recursion is limited to GCD [MCD] calculation
		int counter = 0;
		for (int i = 1; i < n; i++) {
			if (areCoprimes(i, n)) counter++;
		} return counter;
	}
	
	// THIRD (DYNAMIC) RECURSIVE SOLUTION // (slow and too much memory usage)
	
	public static boolean dynamicAreCoprime(int i, int n, byte[][] coprimeTable) { // GCD calculation (Greatest Common Divisor) [MCD in italiano]
		
		byte temp = coprimeTable[i][n];
		
		if (temp == 0) { // if mcd has not yet been calculated
			if (n == 0) {
				if (i == 1) {
					coprimeTable[i][n] = 1; // 1 stands for true
					coprimeTable[n][i] = 1;
					return true;
				} else {
					coprimeTable[i][n] = 2; // 2 stands for false
					coprimeTable[n][i] = 2;
					return false;
				}
			} else {
				boolean bool = dynamicAreCoprime(n, i % n, coprimeTable);
				if (bool) {
					coprimeTable[i][n] = 1;
					coprimeTable[n][i] = 1;
					return bool;
				} else {
					coprimeTable[i][n] = 2; // 2 stands for false
					coprimeTable[n][i] = 2;
					return bool;
				}
			}	
		} else {
			if (temp == 1) return true;
			return false;
		}
	}
	
	public static int thirdRecursivePhi(int n) { // recursion is limited to GCD [MCD] calculation
		int counter = 0;
		for (int i = 1; i < n; i++) {
			if (dynamicAreCoprime(i, n, mcdTable)) counter++;
		} return counter;
	}

	// ITERATIVE SOLUTION // (A LOT faster than alternate solution)
	
	public static int iterativePhi(int n) { 
		if (n == 1) return 1;
		double temp = 1;
		Vector<Integer> factors = factorize(n);
		for (int i = 0; i < factors.size(); i++) {
			temp *= (1 - (1.0/factors.get(i)));
		} return (int) (temp * n);
	}

	public static Vector<Integer> factorize(int n) { 
		
		Vector<Integer> factors = new Vector<Integer>();
		Vector<Integer> primelist = primeList((int) Math.sqrt(n)); // we consider primes up to sqrt of n
		
		while (!isPrime(n)) {
			for (int i : primelist) {
				if (n % i == 0) {
					if (!factors.contains(i))
						factors.add(i);
					n = n / i;
					break;
				}
			} 
		} if (!factors.contains(n)) factors.add(n);
		
		return factors;
	}

	public static boolean isPrime(int n) {
		 for (int i = 2; i <= Math.sqrt(n); ++i)
		        if (n % i == 0) return false;
		    return true;
	}
	

}
