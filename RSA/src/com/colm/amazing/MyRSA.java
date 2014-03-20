package com.colm.amazing;

import java.math.BigInteger;  
import java.util.Random;

public class MyRSA
{	
	//RSA stuff
	public BigInteger p;  
    public BigInteger q;  
    public BigInteger N;  
    public BigInteger phi;  
    public BigInteger e;  
    public BigInteger d;  
    public int bitlength = 1024;
    public Random r;  
     
    public MyRSA() 
     {
    	//RSA
        r = new Random();  
        p = BigInteger.probablePrime(bitlength, r);  
        q = BigInteger.probablePrime(bitlength, r);  
        N = p.multiply(q); //generate public key 
            
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));  
        e = BigInteger.probablePrime(3, r);  
          
        while(phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0 ) 
        {  
            e.add(BigInteger.ONE);  
        }  
        d = e.modInverse(phi); //generate private key
    }
    
    //encrypt function
    public BigInteger encrypt(BigInteger message) 
    {       
        return message.modPow(e, N);
    }
    
    //decrypt function    
    public BigInteger decrypt(BigInteger message) 
    {  
        return message.modPow(d, N);  
    }
}