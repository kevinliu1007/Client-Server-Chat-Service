package service;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * <h1>RSA Module</h1>
 * RSA encryption/decryption class that construct message translator.
 */
public class RSA
{
    private BigInteger e;
    private BigInteger d;
    private BigInteger n;
    private SecureRandom random = new SecureRandom();


    /**
     * RSA constructor.
     */
    public RSA() {
        BigInteger firstPrime = BigInteger.probablePrime(4096, random);
        BigInteger secondPrime = BigInteger.probablePrime(4096, random);
        BigInteger phi = firstPrime.subtract(BigInteger.ONE).multiply(secondPrime.subtract(BigInteger.ONE));

        e = BigInteger.probablePrime(4096 / 2, random);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e.add(BigInteger.ONE);
        }

        d = e.modInverse(phi);

        n = firstPrime.multiply(secondPrime);
    }

    /**
     * RSA constructor with given e, d, n values.
     *
     * @param e e
     * @param d d
     * @param n n
     */
    public RSA(BigInteger e, BigInteger d, BigInteger n) {
        this.e = e;
        this.d = d;
        this.n = n;
    }

    /**
     * Encrypt an message given its byte array.
     *
     * @param message   byte array of the message
     *
     * @return  encrypted byte array
     */
    public byte[] encrypt(byte[] message) {
        return (new BigInteger(message)).modPow(e, n).toByteArray();
    }

    /**
     * Dycrypt an message given its byte array.
     *
     * @param message   byte array of the encrypted message
     *
     * @return  decrypted byte array
     */
    public byte[] decrypt(byte[] message) {
        return (new BigInteger(message)).modPow(d, n).toByteArray();
    }

    /**
     * Getter for e value.
     *
     * @return  e
     */
    public BigInteger getE() {
        return e;
    }

    /**
     * Getter for d value.
     *
     * @return  d
     */
    public BigInteger getD() {
        return d;
    }

    /**
     * Getter for n value.
     *
     * @return
     */
    public BigInteger getN() {
        return n;
    }
}