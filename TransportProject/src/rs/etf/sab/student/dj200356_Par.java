/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;


import rs.etf.sab.operations.PackageOperations.Pair;

/**
 *
 * @author Jelena
 */
public class dj200356_Par<A,B> implements Pair<A,B> {

    public A first;
    public B second;

    public dj200356_Par(A first, B second) {
        this.first = first;
        this.second = second;
    }

	@Override
	public A getFirstParam() {
		// TODO Auto-generated method stub
		return first;
	}

	@Override
	public B getSecondParam() {
		// TODO Auto-generated method stub
		return second;
	}

   

    public static boolean equals(dj200356_Par a, dj200356_Par b){
        return a.first.equals(b.first) && a.second.equals(b.second);
    }

    
    
}
