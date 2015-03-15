package com.gurghet.tturni;

import java.util.Arrays;

/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Andrea Francesco Passaglia
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public enum Medico {
    DOTTPASSAGLIA(
            new Integer[]{},
            new Integer[]{10, 11, 12, 13, 14, 15, 23, 24},
            new Integer[]{6, 18, 20, 30},
            new Integer[]{1, 9, 15},
            new Integer[]{35},
            156 - 6 * 7.6),
    DOTTORE_MOTTA(
            new Integer[]{},
            new Integer[]{28, 29, 30, 31},
            new Integer[]{},
            new Integer[]{27},
            new Integer[]{},
            156 - 2 * 7.6),
    DOTTSARINALDI(
            new Integer[]{},
            new Integer[]{},
            new Integer[]{26},
            new Integer[]{23},
            new Integer[]{},
            156),
    DOTTORE_BAUDO(
            new Integer[]{},
            new Integer[]{16, 17, 18, 19, 20, 21, 22},
            new Integer[]{5, 9, 23, 24},
            new Integer[]{2, 11, 22, 25, 29},
            new Integer[]{},
            156 - 5 * 7.6),
    DOTTSSA_COSTA(
            new Integer[]{},
            new Integer[]{},
            new Integer[]{},
            new Integer[]{4, 16},
            new Integer[]{},
            156),
    DOTTR_MASOUDI(
            new Integer[]{3, 4, 5, 10, 11, 12, 17, 18, 19, 23, 25, 26, 31},
            new Integer[]{18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 19, 30, 31},
            new Integer[]{2, 13},
            new Integer[]{6, 8},
            new Integer[]{},
            156 - 17 * 7.6);

    public int oreAssegnate;
    private Integer[] giorniProibiti;
    private Integer[] giorniFerie;
    private Integer[] pomeriggiPreferiti;
    private Integer[] nottiPreferite; // in cui voglio lavorare
    private Integer[] preferenze; // [mattina e pomeriggio]
    private double monteOre;

    Medico(Integer[] giorniProibiti,
           Integer[] giorniFerie,
           Integer[] pomeriggiPreferiti,
           Integer[] nottiPreferite,
           Integer[] preferenze,
           double monteOre) {
        this.giorniProibiti = giorniProibiti;
        this.giorniFerie = giorniFerie;
        this.pomeriggiPreferiti = pomeriggiPreferiti;
        this.nottiPreferite = nottiPreferite;
        this.preferenze = preferenze;
        this.monteOre = monteOre;
    }

    public boolean isFerie(Integer numeroGiorno) {
        return Arrays.stream(giorniFerie).anyMatch(numeroGiorno::equals);
    }

    public boolean isPomeriggioPreferito(Integer numeroGiorno) {
        return Arrays.stream(pomeriggiPreferiti).anyMatch(numeroGiorno::equals);
    }

    public boolean isNottePreferita(Integer numeroGiorno) {
        return Arrays.stream(nottiPreferite).anyMatch(numeroGiorno::equals);
    }

    public boolean isProibito(Integer numeroGiorno) {
        return Arrays.stream(giorniProibiti).anyMatch(numeroGiorno::equals);
    }

    public int gravitàMattinaEPomeriggio() {
        if (preferenze.length >= 2) {
            return preferenze[1];
        } else {
            return 60;
        }
    }

    public int mattineConsecutive() {
        if (preferenze.length >= 1) {
            return preferenze[0];
        } else {
            return 25;
        }
    }

    public double monteOre() {
        return monteOre;
    }
}