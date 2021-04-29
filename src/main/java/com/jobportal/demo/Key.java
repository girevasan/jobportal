package com.jobportal.demo;

import java.math.BigInteger;


public class Key
{
  private BigInteger _n;
  private BigInteger _e;

  public Key(BigInteger n, BigInteger e)
  {
    _n = n;
    _e = e;
  }

  public BigInteger getN()
  {
    return _n;
  }

  public BigInteger getE()
  {
    return _e;
  }
}
