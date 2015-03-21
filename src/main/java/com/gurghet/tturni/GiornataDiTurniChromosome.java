package com.gurghet.tturni;

import org.jenetics.AbstractChromosome;
import org.jenetics.Chromosome;
import org.jenetics.EnumGene;
import org.jenetics.internal.util.Equality;
import org.jenetics.internal.util.Hash;
import org.jenetics.internal.util.reflect;
import org.jenetics.util.ISeq;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.jenetics.util.MSeq.toMSeq;

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
public final class GiornataDiTurniChromosome<T>
        extends AbstractChromosome<EnumGene<T>>
        implements Serializable {

    private ISeq<T> _validAlleles;
    private int _numTurni;

    /**
     * Create a new {@code AbstractChromosome} from the given {@code genes}
     * array. The genes array is not copied, but sealed, so changes to the given
     * genes array doesn't effect the genes of this chromosome.
     *
     * @param genes the genes that form the chromosome.
     * @throws NullPointerException     if the given gene array is {@code null}.
     * @throws IllegalArgumentException if the length of the gene array is
     *                                  smaller than one.
     */
    protected GiornataDiTurniChromosome(ISeq<EnumGene<T>> genes) {
        super(genes);
        _validAlleles = genes.get(0).getValidAlleles();
    }

    /**
     * Create a new, random chromosome with the given valid alleles.
     *
     * @param <T>     the gene type of the chromosome
     * @param alleles the valid alleles used for this permutation arrays.
     * @return a new chromosome with the given alleles
     * @since 2.0
     */
    @SafeVarargs
    public static <T> GiornataDiTurniChromosome<T> of(int numTurni, final T... alleles) {
        @SuppressWarnings("unchecked")
        GiornataDiTurniChromosome<T> giornataDiTurniChromosome = (GiornataDiTurniChromosome<T>) of(ISeq.of(alleles), numTurni);
        return giornataDiTurniChromosome;
    }

    /**
     * Create a new, random chromosome with the given valid alleles.
     *
     * @param <T>     the gene type of the chromosome
     * @param alleles the valid alleles used for this permutation arrays.
     * @return a new chromosome with the given alleles
     */
    public static <T> GiornataDiTurniChromosome of(final ISeq<? extends T> alleles, int numTurni) {
        final ISeq<EnumGene<T>> genes = IntStream.range(0, numTurni) // da 0 a 3
                .mapToObj(i -> EnumGene.of(alleles))
                .collect(toMSeq())
                .toISeq();

        @SuppressWarnings("unchecked")
        final GiornataDiTurniChromosome chromosome = new GiornataDiTurniChromosome(genes);
        chromosome._validAlleles = reflect.cast(alleles);
        chromosome._valid = true;
        chromosome._numTurni = numTurni;

        return chromosome;
    }

    @Override
    public Chromosome<EnumGene<T>> newInstance(final ISeq<EnumGene<T>> genes) {
        return new GiornataDiTurniChromosome<>(genes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Chromosome<EnumGene<T>> newInstance() {
        return of(_validAlleles, _numTurni);
    }

    @Override
    public int hashCode() {
        return Hash.of(getClass())
                .and(super.hashCode())
                .value();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj.getClass() == this.getClass() && Equality.of(this, obj).test(super::equals);
    }

    @Override
    public String toString() {
        return _genes.asList().stream()
                .map(g -> g.getAllele().toString())
                .collect(Collectors.joining("|")) + "\n";
    }
}
