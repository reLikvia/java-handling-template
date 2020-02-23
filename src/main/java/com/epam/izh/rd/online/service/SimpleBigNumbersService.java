package com.epam.izh.rd.online.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class SimpleBigNumbersService implements BigNumbersService {

    /**
     * Метод делит первое число на второе с заданной точностью
     * Например 1/3 с точностью 2 = 0.33
     * @param range точность
     * @return результат
     */
    @Override
    public BigDecimal getPrecisionNumber(int a, int b, int range) {
        BigDecimal bigDecimalA = BigDecimal.valueOf(a);
        BigDecimal bigDecimalB = BigDecimal.valueOf(b);
        BigDecimal result = bigDecimalA.divide(bigDecimalB,range, RoundingMode.HALF_UP);
        return result;
    }

    /**
     * Метод находит простое число по номеру
     *
     * @param range номер числа, считая с числа 2
     * @return простое число
     */
    @Override
    public BigInteger getPrimaryNumber(int range) {
        BigInteger prime = BigInteger.valueOf(2);
        while (range != 0) {
            prime = prime.nextProbablePrime();
            range--;
        }
        return prime;
    }
}
