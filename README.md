# shifter
Schedules shifts taking in account worker preferences and business needs. Currently based on 1st 2nd and 3rd shift at a hospital.

Doctors’ preferences are stored in the ‘Medico’ enum, they are then picked up by a genetic algorithm.

Each allele is a Doctor, each gene is a shift and each chromosome is a 24h day.

The algorithm takes quite some time to run but the results are already very good.

## Roadmap
1. Test shift relationship functions
2. Generalize Medico class
3. Tweak the genetic algorithm
