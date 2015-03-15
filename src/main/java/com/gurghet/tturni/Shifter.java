package com.gurghet.tturni;

import org.jenetics.*;
import org.jenetics.engine.Engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jenetics.engine.EvolutionResult.toBestPhenotype;
import static org.jenetics.engine.limit.bySteadyFitness;

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
public class Shifter {
    static int[] weekend = new int[]{1, 7, 8, 14, 15, 21, 22, 28, 29};
    static int[] nottiAssegnate = new int[]{3, 5, 7, 13, 14, 17, 18, 19, 21, 24, 26, 28, 30, 31};
    static int[] pomeriggiAssegnati = new int[]{1, 7, 8, 14, 15, 21, 22, 28, 29};
    static boolean fine = false;

    private static Integer eval(Genotype<EnumGene<Medico>> genotype) {
        // il giorno dopo la notte non è possibile avere turni per lo stesso medico

        int result = 0;

        for (Medico medico : Medico.values()) {
            medico.oreAssegnate = 0;
        }

        for (int i = 0; i < 31; i++) {
            int quantiNeAbbiamoOggi = i + 1;
            GiornataDiTurniChromosome<Medico> oggi = (GiornataDiTurniChromosome<Medico>) genotype.getChromosome(i);
            GiornataDiTurniChromosome<Medico> domani = null;
            if (i < 30) {
                domani = (GiornataDiTurniChromosome<Medico>) genotype.getChromosome(i + 1);
            }


            Medico medicoDiDomaniMattina1 = null;
            Medico medicoDiDomaniMattina2 = null;
            Medico medicoDiDomaniPomeriggio = null;
            Medico medicoDiDomaniNotte = null;

            // Mattina
            EnumGene<Medico> mattina1 = oggi.getGene(0);
            EnumGene<Medico> mattina2 = oggi.getGene(1);
            // Pomeriggio
            EnumGene<Medico> pomeriggio = oggi.getGene(2);
            // Notte
            EnumGene<Medico> notte = oggi.getGene(3);

            Medico medicoDiMattina1 = mattina1.getAllele();
            Medico medicoDiMattina2 = mattina2.getAllele();
            Medico medicoDiPomeriggio = pomeriggio.getAllele();
            Medico medicoDiNotte = notte.getAllele();

            medicoDiMattina1.oreAssegnate += 6;
            //if (fine) System.out.println(medicoDiMattina1 + "fa il mattino quindi " + medicoDiMattina1.oreAssegnate + " ore");
            if (!èweekend(quantiNeAbbiamoOggi)) {
                medicoDiMattina2.oreAssegnate += 6;
            }
            //if (fine) System.out.println(medicoDiMattina2 + "fa il mattino quindi " + medicoDiMattina2.oreAssegnate + " ore");
            if (!èPomeriggioAssegnato(quantiNeAbbiamoOggi)) {
                medicoDiPomeriggio.oreAssegnate += 6;
                //if (fine) System.out.println(medicoDiPomeriggio + "fa il pomeriggio quindi " + medicoDiPomeriggio.oreAssegnate + " ore");
            }
            if (!èNotteAssegnata(quantiNeAbbiamoOggi)) {
                medicoDiNotte.oreAssegnate += 12;
                //if (fine) System.out.println(medicoDiNotte + "fa la notte quindi " + medicoDiNotte.oreAssegnate + " ore");
            }

            if (domani != null) {
                // Mattina
                EnumGene<Medico> mattina1diDomani = domani.getGene(0);
                EnumGene<Medico> mattina2diDomani = domani.getGene(1);
                // Pomeriggio
                EnumGene<Medico> pomeriggioDiDomani = domani.getGene(2);
                // Notte
                EnumGene<Medico> notteDiDomani = domani.getGene(3);

                medicoDiDomaniMattina1 = mattina1diDomani.getAllele();
                medicoDiDomaniMattina2 = mattina2diDomani.getAllele();
                medicoDiDomaniPomeriggio = pomeriggioDiDomani.getAllele();
                medicoDiDomaniNotte = notteDiDomani.getAllele();
            }


            // Mattina e pomeriggio
            if (medicoDiMattina1.equals(medicoDiPomeriggio) && !èPomeriggioAssegnato(quantiNeAbbiamoOggi)) {
                result -= medicoDiMattina1.gravitàMattinaEPomeriggio();
            }
            if (medicoDiMattina2.equals(medicoDiPomeriggio) && !èweekend(quantiNeAbbiamoOggi) && !èPomeriggioAssegnato(quantiNeAbbiamoOggi)) {
                result -= medicoDiMattina2.gravitàMattinaEPomeriggio();
            }

            // Mattina e notte
            if (medicoDiMattina1.equals(medicoDiNotte) && !èNotteAssegnata(quantiNeAbbiamoOggi)) {
                result -= 50;
            }
            if (medicoDiMattina2.equals(medicoDiNotte) && !èweekend(quantiNeAbbiamoOggi) && !èNotteAssegnata(quantiNeAbbiamoOggi)) {
                result -= 50;
            }

            // Pomeriggio e notte
            if (medicoDiPomeriggio.equals(medicoDiNotte) && !èNotteAssegnata(quantiNeAbbiamoOggi) && !èPomeriggioAssegnato(quantiNeAbbiamoOggi)) {
                result -= 65;
            }

            // Notte e mattina
            if (domani != null && medicoDiNotte.equals(medicoDiDomaniMattina1) && !èNotteAssegnata(quantiNeAbbiamoOggi)) {
                result -= 65;
            }
            if (domani != null && medicoDiDomaniNotte.equals(medicoDiDomaniMattina2) && !èNotteAssegnata(quantiNeAbbiamoOggi) && !èweekend(quantiNeAbbiamoOggi + 1)) {
                result -= 65;
            }

            // Notte e pomeriggio
            if (domani != null && medicoDiNotte.equals(medicoDiDomaniPomeriggio) && !èPomeriggioAssegnato(quantiNeAbbiamoOggi + 1) && !èNotteAssegnata(quantiNeAbbiamoOggi)) {
                result -= 65;
            }

            // Medico sdoppiato
            if (medicoDiMattina1.equals(medicoDiMattina2) && !èweekend(quantiNeAbbiamoOggi)) {
                result -= 50;
            }

            // Mattina anche domani
            if (domani != null && medicoDiMattina1.equals(medicoDiDomaniMattina1) ||
                    medicoDiMattina1.equals(medicoDiDomaniMattina2)) {
                result += medicoDiDomaniMattina1.mattineConsecutive();
            }
            if (domani != null && (medicoDiMattina2.equals(medicoDiDomaniMattina1) ||
                    medicoDiMattina2.equals(medicoDiDomaniMattina2)) && !èweekend(quantiNeAbbiamoOggi)) {
                result += medicoDiDomaniMattina2.mattineConsecutive();
            }

            // Notte anche domani
            if (domani != null && medicoDiNotte.equals(medicoDiDomaniNotte) && !èNotteAssegnata(quantiNeAbbiamoOggi) && !èNotteAssegnata(quantiNeAbbiamoOggi + 1)) {
                if (fine)
                    System.out.println(medicoDiNotte + " lavore il " + quantiNeAbbiamoOggi + " notte e anche la notte dopo");
                result -= 50;
            }

            // Notte preferita
            if (medicoDiNotte.isNottePreferita(quantiNeAbbiamoOggi) && !èNotteAssegnata(quantiNeAbbiamoOggi)) {
                // non può essere assegnata se c’è già la chirurgia o neuro
                result += 35;
            }

            // Pomeriggio preferito
            if (medicoDiPomeriggio.isPomeriggioPreferito(quantiNeAbbiamoOggi) && !èPomeriggioAssegnato(quantiNeAbbiamoOggi)) {
                // non può essere assegnato se c’è già la chirurgia o neuro
                result += 15;
            }

            // Mattino proibito
            if (medicoDiMattina1.isProibito(quantiNeAbbiamoOggi)) {
                result -= 65;
            }
            if (medicoDiMattina2.isProibito(quantiNeAbbiamoOggi) && !èweekend(quantiNeAbbiamoOggi)) {
                result -= 65;
            }

            // Pomeriggio proibito
            if (medicoDiPomeriggio.isProibito(quantiNeAbbiamoOggi) && !èPomeriggioAssegnato(quantiNeAbbiamoOggi)) {
                result -= 65;
            }

            // Notte proibita
            if (medicoDiNotte.isProibito(quantiNeAbbiamoOggi) && !èNotteAssegnata(quantiNeAbbiamoOggi)) {
                result -= 65;
            }
            if (medicoDiNotte.isProibito(quantiNeAbbiamoOggi + 1) && !èNotteAssegnata(quantiNeAbbiamoOggi)) {
                result -= 65;
            }

            // Ferie
            int mediciInFerieCheLavorano = 0;
            if (medicoDiMattina1.isFerie(quantiNeAbbiamoOggi)) {
                mediciInFerieCheLavorano += 1;
            }
            if (medicoDiMattina2.isFerie(quantiNeAbbiamoOggi) && !èweekend(quantiNeAbbiamoOggi)) {
                mediciInFerieCheLavorano += 1;
            }
            if (medicoDiPomeriggio.isFerie(quantiNeAbbiamoOggi) && !èPomeriggioAssegnato(quantiNeAbbiamoOggi)) {
                mediciInFerieCheLavorano += 1;
            }
            if (medicoDiNotte.isFerie(quantiNeAbbiamoOggi) && !èNotteAssegnata(quantiNeAbbiamoOggi)) {
                mediciInFerieCheLavorano += 1;
            }
            int gravitàLavoroInFerie = 50;
            result = result - mediciInFerieCheLavorano * gravitàLavoroInFerie;
        }

        Double penalitàMonteOre = 0D;
        for (Medico medico : Medico.values()) {
            double differenza = medico.oreAssegnate - medico.monteOre();
            penalitàMonteOre += Math.pow((differenza / 6), 4);
            if (fine)
                System.out.println(medico + " ha una differenza di " + differenza + ", quindi la penalità ammonta a " + penalitàMonteOre + ". Totale: " + medico.oreAssegnate);
        }


        return result * 40 - penalitàMonteOre.intValue();
    }

    private static boolean èPomeriggioAssegnato(Integer giorno) {
        return Arrays.stream(pomeriggiAssegnati).anyMatch(giorno::equals);
    }

    private static boolean èNotteAssegnata(Integer giorno) {
        return Arrays.stream(nottiAssegnate).anyMatch(giorno::equals);
    }

    private static boolean èweekend(Integer giorno) {
        return Arrays.stream(weekend).anyMatch(giorno::equals);
    }

    public static void main(String[] args) {
        List<GiornataDiTurniChromosome<Medico>> meseOspedaliero = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            meseOspedaliero.add(GiornataDiTurniChromosome.of(Medico.values()));
        }

        Engine<EnumGene<Medico>, Integer> engine = Engine.builder
                (                            // phenotype: {
                        Shifter::eval,               //   environment,
                        Genotype.of(meseOspedaliero) //   genotype
                )                            // }
                .alterers(new Mutator<>(0.5), new MultiPointCrossover<>(0.3, 4)).populationSize(8000).build();

        final Phenotype<EnumGene<Medico>, Integer> bestPhenotype = engine.stream()
                .limit(bySteadyFitness(100))
                .limit(10000)
                .collect(toBestPhenotype());

        for (int i = 0; i < 31; i++) {
            System.out.println(i + 1 + ": " + bestPhenotype.getGenotype().getChromosome(i).getGene(0)
                    + " " + (èweekend(i + 1) ? "XXXXXXXXXXXXX" : bestPhenotype.getGenotype().getChromosome(i).getGene(1))
                    + "|" + (èPomeriggioAssegnato(i + 1) ? "XXXXXXXXXXXXX" : bestPhenotype.getGenotype().getChromosome(i).getGene(2))
                    + "|" + (èNotteAssegnata(i + 1) ? "XXXXXXXXXXXXX" : bestPhenotype.getGenotype().getChromosome(i).getGene(3)));
        }

        Shifter.fine = true;
        System.out.println("Fitness: " + Shifter.eval(bestPhenotype.getGenotype()) + ", generazione: " + bestPhenotype.getGeneration());

    }
}