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
            new Integer[]{3, 4, 5, 6, 9, 10, 23, 24}, // ferie
            new Integer[]{1, 2, 8, 11, 13, 15, 20, 21, 22, 29},
            new Integer[]{7, 14, 27, 28}, // pomeriggi
            new Integer[]{1, 9, 15}, // notti
            //new Integer[]{35},
            156 - 5 * 7.6,
            5,
            4),
    DOTTORE_MOTTA(
            new Integer[]{},
            new Integer[]{},
            new Integer[]{},
            new Integer[]{}, // pomeriggi
            new Integer[]{}, // notti
            //new Integer[]{},
            156 - 0 * 7.6,
            0,
            0),
    DOTTSARINALDI(
            new Integer[]{},
            new Integer[]{27, 28, 29, 30}, // ferie
            new Integer[]{},
            new Integer[]{}, // pomeriggi
            new Integer[]{23}, // notti
            //new Integer[]{},
            156 - 4 * 7.6,
            5,
            2),
    DOTTORE_BAUDO(
            new Integer[]{8},
            new Integer[]{9, 10, 11, 12, 13, 14, 15, 16, 17}, // ferie
            new Integer[]{},
            new Integer[]{}, // pomeriggi
            new Integer[]{2, 11, 22, 25, 29}, // notti
            //new Integer[]{},
            156 - 7 * 7.6,
            5,
            5),
    DOTTSSA_COSTA(
            new Integer[]{1, 13, 15, 20, 22, 27, 29}, // ambulatorio
            new Integer[]{16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26}, // ferie
            new Integer[]{},
            new Integer[]{}, // pomeriggi
            new Integer[]{4, 16}, // notti
            //new Integer[]{},
            156 - 7 * 7.6,
            5,
            2),
    DOTTR_MASOUDI(
            new Integer[]{14, 15, 16, 21, 22, 23, 28, 29, 30}, // ambulatorio
            new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, // ferie
            new Integer[]{},
            new Integer[]{}, // pomeriggi
            new Integer[]{6, 8}, // notti
            //new Integer[]{},
            156 - 7 * 7.6,
            5,
            1);


    private int numPomeriggiDaFare;
    private int numNottiDaFare;
    private Integer[] giorniDiAmbulatorio;
    private Integer[] giorniFerie;
    private Integer[] mattinePreferite;
    private Integer[] pomeriggiPreferiti;
    private Integer[] nottiPreferite; // in cui voglio lavorare
    //private Integer[] preferenze; // [mattina e pomeriggio]
    private double monteOre;

    Medico(Integer[] giorniDiAmbulatorio,
           Integer[] giorniFerie,
           Integer[] mattinePreferite,
           Integer[] pomeriggiPreferiti,
           Integer[] nottiPreferite,
           //Integer[] preferenze,
           double monteOre,
           int numPomeriggiDaFare,
           int numNottiDaFare) {
        this.giorniDiAmbulatorio = giorniDiAmbulatorio;
        this.giorniFerie = giorniFerie;
        this.mattinePreferite = mattinePreferite;
        this.pomeriggiPreferiti = pomeriggiPreferiti;
        this.nottiPreferite = nottiPreferite;
        //this.preferenze = preferenze;
        this.monteOre = monteOre;
        this.numPomeriggiDaFare = numPomeriggiDaFare;
        this.numNottiDaFare = numNottiDaFare;
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

    public boolean isAmbulatorio(Integer numeroGiorno) {
        return Arrays.stream(giorniDiAmbulatorio).anyMatch(numeroGiorno::equals);
    }

    public double monteOre() {
        return monteOre;
    }

    public int oreDiAmbulatorio() {
        return this.giorniDiAmbulatorio.length * 6;
    }

    public int pomeriggiCheDovrebbeFare() {
        return numPomeriggiDaFare;
    }

    public int nottiCheDovrebbeFare() {
        return numNottiDaFare;
    }

    public boolean èMattinaPreferita(Integer numeroGiorno) {
        return Arrays.stream(mattinePreferite).anyMatch(numeroGiorno::equals);
    }
}