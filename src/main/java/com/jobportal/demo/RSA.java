package com.jobportal.demo;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA
{
	private BigInteger _p;
	private BigInteger _q;
	private BigInteger _n;
	private BigInteger _t;
	private BigInteger _e;
	private BigInteger _d;
  private Key _pubKey;
  private Key _privKey;

	public RSA(int bitlen)
	{
		SecureRandom r = new SecureRandom();
		_p = new BigInteger(bitlen, 100, r).nextProbablePrime();
		_q = new BigInteger(bitlen, 100, r).nextProbablePrime();
		_n = _p.multiply(_q);
		_t = (_p.subtract(BigInteger.ONE)).multiply(_q.subtract(BigInteger.ONE));
		_e = getCoPrime(_t);
		_d = _e.modInverse(_t);
    _pubKey = new Key(_n, _e);
    _privKey = new Key(_n, _d);
	}
	
	public int mdc(int a, int b)
	{
		int r = a%b;
		if (r > 0)
			return mdc(b, r);
		return b;
	}
	
	public boolean isCoPrime(BigInteger c, BigInteger n)
	{
		BigInteger one = new BigInteger("1");
		if (c.gcd(n).equals(one))
			return true;
		return false;
	}
	
	public BigInteger getCoPrime(BigInteger n)
	{
		BigInteger c = new BigInteger(n.toString());
		BigInteger one = new BigInteger("1");
		BigInteger two = new BigInteger("2");
		c = c.subtract(two);

		while (c.intValue() > 1)
		{
			if (c.gcd(n).equals(one))
				break;

			c = c.subtract(one);
		}

		return c;
	}
	

	public BigInteger getP()
	{
		return _p;
	}

	public BigInteger getQ()
	{
		return _q;
	}

	public BigInteger getN()
	{
		return _n;
	}

	public BigInteger getT()
	{
		return _t;
	}

	public BigInteger getE()
	{
		return _e;
	}

	public BigInteger getD()
	{
		return _d;
	}

  public Key getPublicKey()
  {
    return _pubKey;
  }

  public Key getPrivateKey()
  {
    return _privKey;
  }
}