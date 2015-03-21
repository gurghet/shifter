package com.gurghet.tturni;

import org.jenetics.*;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.jenetics.engine.EvolutionResult.toBestPhenotype;

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
    public static final int oreAssegnate = 0;
    public static final int pomeriggiAssegnati = 1;
    public static final int nottiAssegnate = 2;
    public static final int notteEMattinaAssegnati = 3;
    public static final int nottiAPagamentoAssegnate = 4;
    public static final int pomeriggiConsecutivi = 5;
    public static final int ferieLavorate = 6;
    public static final int mattinaENotteAssegnati = 7;
    public static final int mattinaEPomeriggioAssegnati = 8;
    public static final int mattineCheLavoraGiàAssegnate = 9;
    public static final int mattineConsecutive = 10;
    public static final int mattinePreferiteBeccate = 11;
    public static final int notteEPomeriggioAssegnati = 12;
    public static final int nottiCheLavoraGiàAssegnate = 13;
    public static final int pomeriggiCheLavoraGiàAssegnate = 14;
    public static final int nottiConsecutive = 15;
    public static final int nottiPreferiteBeccate = 16;
    public static final int pomeriggioENotteAssegnati = 17;
    public static final int pomeriggiPreferitiBecccati = 18;
    static int[] weekend = new int[]{4, 5, 6, 11, 12, 18, 19, 25, 26};
    static int[] nottiAssegnateAChir = new int[]{2, 4, 5, 7, 9, 11, 13, 14, 15, 16, 18, 21, 23, 25, 27, 28, 30};
    static int[] pomeriggiAssegnatiAChir = new int[]{4, 6, 12, 18, 19, 25, 26};
    static int[] nottiAPagamento = new int[]{8, 19, 22, 24};
    static boolean fine = false;

    private static Integer eval(Genotype<EnumGene<Medico>> genotype) {
        // il giorno dopo la notte non è possibile avere turni per lo stesso medico

        int mattineNonValide = 0;

        HashMap<Medico, Integer[]> medicalParam = new HashMap<>();
        for (Medico medico : Medico.values()) {
            Integer[] params = new Integer[19];
            for (int i = 0; i < 19; i++) {
                params[i] = 0;
            }
            medicalParam.put(medico, params);
        }

        for (int i = 0; i < 30; i++) {
            int quantiNeAbbiamoOggi = i + 1;
            GiornataDiTurniChromosome<Medico> oggi = (GiornataDiTurniChromosome<Medico>) genotype.getChromosome(i);
            GiornataDiTurniChromosome<Medico> domani = null;
            if (i < 29) {
                domani = (GiornataDiTurniChromosome<Medico>) genotype.getChromosome(i + 1);
            }

            Medico medicoDiDomaniMattina1 = null;
            Medico medicoDiDomaniMattina2 = null;
            Medico medicoDiDomaniPomeriggio = null;
            Medico medicoDiDomaniNotte = null;
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

            medicalParam.get(medicoDiMattina1)[oreAssegnate] += 6;
            //if (fine) System.out.println(medicoDiMattina1 + "fa il mattino quindi " + medicoDiMattina1.oreAssegnate + " ore");
            if (!èweekend(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina2)[oreAssegnate] += 6;
            }
            //if (fine) System.out.println(medicoDiMattina2 + "fa il mattino quindi " + medicoDiMattina2.oreAssegnate + " ore");
            if (!èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiPomeriggio)[oreAssegnate] += 6;
                medicalParam.get(medicoDiPomeriggio)[pomeriggiAssegnati] += 1;
                //if (fine) System.out.println(medicoDiPomeriggio + "fa il pomeriggio quindi " + medicoDiPomeriggio.oreAssegnate + " ore");
            }
            if (!èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiNotte)[oreAssegnate] += 12;
                medicalParam.get(medicoDiNotte)[nottiAssegnate] += 1;
                if (èNotteAPagamento(quantiNeAbbiamoOggi)) {
                    medicalParam.get(medicoDiNotte)[nottiAPagamentoAssegnate] += 1;
                }
                //if (fine) System.out.println(medicoDiNotte + "fa la notte quindi " + medicoDiNotte.oreAssegnate + " ore");
            }


            // Mattina e pomeriggio
            if (medicoDiMattina1.equals(medicoDiPomeriggio) && !èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina1)[mattinaEPomeriggioAssegnati] += 1;
                if (fine)
                    System.out.println("mattina e pomeriggio");
            }
            if (medicoDiMattina2.equals(medicoDiPomeriggio) && !èweekend(quantiNeAbbiamoOggi) && !èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina2)[mattinaEPomeriggioAssegnati] += 1;
                if (fine)
                    System.out.println("mattina e pomeriggio");
            }

            // Mattina e notte
            if (medicoDiMattina1.equals(medicoDiNotte) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina1)[mattinaENotteAssegnati] += 1;
                if (fine)
                    System.out.println("mattina e notte");
            }
            if (medicoDiMattina2.equals(medicoDiNotte) && !èweekend(quantiNeAbbiamoOggi) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina2)[mattinaENotteAssegnati] += 1;
                if (fine)
                    System.out.println("mattina e notte");
            }

            // Pomeriggio e notte
            if (medicoDiPomeriggio.equals(medicoDiNotte) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi) && !èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiPomeriggio)[pomeriggioENotteAssegnati] += 1;
                if (fine)
                    System.out.println("pomeriggio e notte");
            }

            // Notte e mattina
            if (domani != null && medicoDiNotte.equals(medicoDiDomaniMattina1) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiNotte)[notteEMattinaAssegnati] += 1;
                if (fine)
                    System.out.println("notte e mattina");
            }
            if (domani != null && medicoDiNotte.equals(medicoDiDomaniMattina2) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi) && !èweekend(quantiNeAbbiamoOggi + 1)) {
                medicalParam.get(medicoDiNotte)[notteEMattinaAssegnati] += 1;
                if (fine)
                    System.out.println("notte e mattina");
            }

            // Notte e pomeriggio
            if (domani != null && medicoDiNotte.equals(medicoDiDomaniPomeriggio) && !èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi + 1) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiNotte)[notteEPomeriggioAssegnati] += 1;
                if (fine)
                    System.out.println("notte e pomeriggio");
            }

            // Medico sdoppiato
            if (medicoDiMattina1.equals(medicoDiMattina2) && !èweekend(quantiNeAbbiamoOggi)) {
                if (fine)
                    System.out.println("medico sdoppiato");
                mattineNonValide += 1;
            }

            // Mattina anche domani
            if (domani != null && medicoDiMattina1.equals(medicoDiDomaniMattina1) || (medicoDiMattina1.equals(medicoDiDomaniMattina2) && !èweekend(quantiNeAbbiamoOggi + 1))) {
                medicalParam.get(medicoDiMattina1)[mattineConsecutive] += 1;
            }
            if (domani != null && (medicoDiMattina2.equals(medicoDiDomaniMattina1) || medicoDiMattina2.equals(medicoDiDomaniMattina2)) && !èweekend(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina2)[mattineConsecutive] += 1;
            }

            // Pomeriggio anche domani
            if (domani != null && medicoDiPomeriggio.equals(medicoDiDomaniPomeriggio) && !èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi) && !èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi + 1)) {
                medicalParam.get(medicoDiPomeriggio)[pomeriggiConsecutivi] += 1;
            }

            // Notte anche domani
            if (domani != null && medicoDiNotte.equals(medicoDiDomaniNotte) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi + 1)) {
                medicalParam.get(medicoDiNotte)[nottiConsecutive] += 1;
            }

            // Notte preferita
            if (medicoDiNotte.isNottePreferita(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiNotte)[nottiPreferiteBeccate] += 1;
            }

            // Pomeriggio preferito
            if (medicoDiPomeriggio.isPomeriggioPreferito(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiPomeriggio)[pomeriggiPreferitiBecccati] += 1;
            }

            // Mattina preferita
            if (medicoDiMattina1.èMattinaPreferita(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina1)[mattinePreferiteBeccate] += 1;
            }
            if (medicoDiMattina2.èMattinaPreferita(quantiNeAbbiamoOggi) && !èweekend(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina2)[mattinePreferiteBeccate] += 1;
            }

            // Mattino proibito
            if (medicoDiMattina1.isAmbulatorio(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina1)[mattineCheLavoraGiàAssegnate] += 1;
            }
            if (medicoDiMattina2.isAmbulatorio(quantiNeAbbiamoOggi) && !èweekend(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina2)[mattineCheLavoraGiàAssegnate] += 1;
            }

            // Pomeriggio proibito
            if (medicoDiPomeriggio.isAmbulatorio(quantiNeAbbiamoOggi) && !èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiPomeriggio)[pomeriggiCheLavoraGiàAssegnate] += 1;
            }

            // Notte proibita
            if (medicoDiNotte.isAmbulatorio(quantiNeAbbiamoOggi) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiNotte)[nottiCheLavoraGiàAssegnate] += 1;
            }
            // Notte precedente ad ambulatorio
            if (medicoDiNotte.isAmbulatorio(quantiNeAbbiamoOggi + 1) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiNotte)[nottiCheLavoraGiàAssegnate] += 1;
            }

            // Ferie
            if (medicoDiMattina1.isFerie(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina1)[ferieLavorate] += 1;
                if (fine)
                    System.out.println(medicoDiMattina1 + " lavora il " + quantiNeAbbiamoOggi + " che avrebbe ferie");
            }
            if (medicoDiMattina2.isFerie(quantiNeAbbiamoOggi) && !èweekend(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiMattina2)[ferieLavorate] += 1;
                if (fine)
                    System.out.println(medicoDiMattina2 + " lavora il " + quantiNeAbbiamoOggi + " che avrebbe ferie");
            }
            if (medicoDiPomeriggio.isFerie(quantiNeAbbiamoOggi) && !èPomeriggioAssegnatoAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiPomeriggio)[ferieLavorate] += 1;
                if (fine)
                    System.out.println(medicoDiPomeriggio + " lavora il " + quantiNeAbbiamoOggi + " che avrebbe ferie");
            }
            if (medicoDiNotte.isFerie(quantiNeAbbiamoOggi) && !èNotteAssegnataAdAltriReparti(quantiNeAbbiamoOggi)) {
                medicalParam.get(medicoDiNotte)[ferieLavorate] += 1;
                if (fine)
                    System.out.println(medicoDiNotte + " lavora il " + quantiNeAbbiamoOggi + " che avrebbe ferie");
            }
        }

        Double penalitàMonteOre = 0D;
        int penalitàSforarePomeriggi = 0;
        int penalitàSforareNotti = 0;
        int penalitàDeviazioneNottiAPagamento = 0;
        int penalitàPomeriggiConsecutivi = 0;
        int penalitàMattinaENotte = 0;
        int penalitàMattinaEPomeriggio = 0;
        int penalitàPomeriggioENotte = 0;
        int penalitàNotteEMattina = 0;
        int penalitàNotteEPomeriggio = 0;
        int bonusMattineConsecutive = 0;
        int penalitàNottiConsecutive = 0;
        int bonusMattineBeccate = 0;
        int bonusPomeriggiBeccati = 0;
        int bonusNottiBeccate = 0;
        int penalitàMattineAmbulatorioAssegnate = 0;
        int penalitàPomeriggiAmbulatorioAssegnate = 0;
        int penalitàNottiAmbulatorioAssegnate = 0;
        int penalitàFerieLavorate = 0;
        for (Medico medico : Medico.values()) {
            Integer[] medicoParam = medicalParam.get(medico);
            double differenza = medicoParam[oreAssegnate] - medico.monteOre() + medico.oreDiAmbulatorio();
            int pomeriggiSforati = Math.max(medicoParam[pomeriggiAssegnati] - medico.pomeriggiCheDovrebbeFare(), 0);
            int nottiSforate = Math.max(medicoParam[nottiAssegnate] - medico.nottiCheDovrebbeFare(), 0);
            int deviazioneNottiAPagamento = Math.abs(medicoParam[nottiAPagamentoAssegnate] - 1);
            penalitàMonteOre += 0;
            penalitàSforarePomeriggi += pomeriggiSforati * 7;
            penalitàSforareNotti += nottiSforate * 8;
            penalitàDeviazioneNottiAPagamento += deviazioneNottiAPagamento * 10;
            penalitàPomeriggiConsecutivi += medicoParam[pomeriggiConsecutivi] * 3;
            penalitàMattinaENotte += medicoParam[mattinaENotteAssegnati] * 10;
            penalitàMattinaEPomeriggio += medicoParam[mattinaEPomeriggioAssegnati] * 9;
            penalitàPomeriggioENotte += medicoParam[pomeriggioENotteAssegnati] * 10;
            penalitàNotteEMattina += medicoParam[notteEMattinaAssegnati] * 10;
            penalitàNotteEPomeriggio += medicoParam[notteEPomeriggioAssegnati] * 9;
            bonusMattineConsecutive += (-Math.pow(medicoParam[mattineConsecutive], 2) + 6 * medicoParam[mattineConsecutive]) * 0.4;
            penalitàNottiConsecutive += medicoParam[nottiConsecutive] * 7;
            bonusMattineBeccate += medicoParam[mattinePreferiteBeccate] * 3;
            bonusPomeriggiBeccati += medicoParam[pomeriggiPreferitiBecccati] * 3;
            bonusNottiBeccate += medicoParam[nottiPreferiteBeccate] * 6;
            penalitàMattineAmbulatorioAssegnate += medicoParam[mattineCheLavoraGiàAssegnate] * 10;
            penalitàPomeriggiAmbulatorioAssegnate += medicoParam[pomeriggiCheLavoraGiàAssegnate] * 10;
            penalitàNottiAmbulatorioAssegnate += medicoParam[nottiCheLavoraGiàAssegnate] * 10;
            penalitàFerieLavorate += medicoParam[ferieLavorate] * 10;
            if (fine)
                System.out.println(medico + " ha una differenza di " + differenza + ", quindi la penalità ammonta a " + penalitàMonteOre + ". Totale: " + medicoParam[oreAssegnate]);
        }

        if (fine) System.out.println("medico sdoppiato: -" + mattineNonValide * 10);
        if (fine) System.out.println("FerieLavorate: -" + penalitàFerieLavorate);
        if (fine) System.out.println("bonusPomeriggi: +" + bonusPomeriggiBeccati);
        if (fine) System.out.println("bonusNotti: +" + bonusNottiBeccate);
        if (fine) System.out.println("mattine beccate: +" + bonusMattineBeccate);
        if (fine) System.out.println("mattine consecutive: + " + bonusMattineConsecutive);
        if (fine) System.out.println("penalitàmattineambulatorio: -" + penalitàMattineAmbulatorioAssegnate);
        if (fine) System.out.println("pome ambu: -" + penalitàPomeriggiAmbulatorioAssegnate);
        if (fine) System.out.println("notte ambu: -" + penalitàNottiAmbulatorioAssegnate);
        if (fine) System.out.println("notti consecutive: -" + penalitàNottiConsecutive);
        if (fine) System.out.println("mattine-notti: -" + penalitàMattinaENotte);
        if (fine) System.out.println("mattine-pome: -" + penalitàMattinaEPomeriggio);
        if (fine) System.out.println("pome-notti: -" + penalitàPomeriggioENotte);
        if (fine) System.out.println("notte-mattin: -" + penalitàNotteEMattina);
        if (fine) System.out.println("notte e pome: -" + penalitàNotteEPomeriggio);
        if (fine) System.out.println("pomeriggi di fila: -" + penalitàPomeriggiConsecutivi);
        if (fine) System.out.println("pome sforato: -" + penalitàSforarePomeriggi);
        if (fine) System.out.println("notte sforata: -" + penalitàSforareNotti);
        if (fine) System.out.println("notte a pagamento: -" + penalitàDeviazioneNottiAPagamento);

        return -penalitàMonteOre.intValue()
                - mattineNonValide * 10
                - penalitàFerieLavorate
                + bonusPomeriggiBeccati
                + bonusNottiBeccate
                + bonusMattineBeccate
                + bonusMattineConsecutive
                - penalitàMattineAmbulatorioAssegnate
                - penalitàPomeriggiAmbulatorioAssegnate
                - penalitàNottiAmbulatorioAssegnate
                - penalitàNottiConsecutive
                - penalitàMattinaENotte
                - penalitàMattinaEPomeriggio
                - penalitàPomeriggioENotte
                - penalitàNotteEMattina
                - penalitàNotteEPomeriggio
                - penalitàPomeriggiConsecutivi
                - penalitàSforarePomeriggi
                - penalitàSforareNotti
                - penalitàDeviazioneNottiAPagamento;
    }

    private static boolean èNotteAPagamento(Integer giorno) {
        return Arrays.stream(nottiAPagamento).anyMatch(giorno::equals);
    }

    private static boolean èPomeriggioAssegnatoAdAltriReparti(Integer giorno) {
        return Arrays.stream(pomeriggiAssegnatiAChir).anyMatch(giorno::equals);
    }

    private static boolean èNotteAssegnataAdAltriReparti(Integer giorno) {
        return Arrays.stream(nottiAssegnateAChir).anyMatch(giorno::equals);
    }

    private static boolean èweekend(Integer giorno) {
        return Arrays.stream(weekend).anyMatch(giorno::equals);
    }

    public static void main(String[] args) {
        List<GiornataDiTurniChromosome<Medico>> meseOspedaliero = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            meseOspedaliero.add(GiornataDiTurniChromosome.of(4, Medico.values()));
        }

        Engine<EnumGene<Medico>, Integer> engine = Engine.builder
                (                            // phenotype: {
                        Shifter::eval,               //   environment,
                        Genotype.of(meseOspedaliero) //   genotype
                )                            // }
                //.alterers(new Mutator<>(), new MultiPointCrossover<>())
                .alterers(new Mutator<>(), new SinglePointCrossover<>())
                        //.offspringFraction(0.7)
                .populationSize(65)
                .build();

        EvolutionStatistics<Integer, ?> evolutionStatistics = EvolutionStatistics.ofComparable();

        final Phenotype<EnumGene<Medico>, Integer> bestPhenotype = engine.stream()
                //.limit(bySteadyFitness(10000))
                .limit(20000)
                .peek(evolutionStatistics)
                .collect(toBestPhenotype());

        for (int i = 0; i < 30; i++) {
            System.out.println(i + 1 + ": " + bestPhenotype.getGenotype().getChromosome(i).getGene(0)
                    + " " + (èweekend(i + 1) ? "XXXXXXXXXXXXX" : bestPhenotype.getGenotype().getChromosome(i).getGene(1))
                    + "|" + (èPomeriggioAssegnatoAdAltriReparti(i + 1) ? "XXXXXXXXXXXXX" : bestPhenotype.getGenotype().getChromosome(i).getGene(2))
                    + "|" + (èNotteAssegnataAdAltriReparti(i + 1) ? "XXXXXXXXXXXXX" : bestPhenotype.getGenotype().getChromosome(i).getGene(3)));
        }

        System.out.println(evolutionStatistics);

        Shifter.fine = true;
        System.out.println("Fitness: " + bestPhenotype.getFitnessFunction().apply(bestPhenotype.getGenotype()) + "/ " + bestPhenotype.getFitness() + ", generazione: " + bestPhenotype.getGeneration());
    }

}