package com.zynaps.bioforge.schemes;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomNumberGenerator;
import java.util.ArrayList;
import java.util.List;

public class TournamentSelection implements SelectionScheme {

    private final int arity;

    public TournamentSelection(int arity) {
        this.arity = Math.max(1, arity);
    }

    @Override
    public List<Creature> apply(List<Creature> creatures, RandomNumberGenerator random) {
        var total = 2 * creatures.size();
        List<Creature> breeders = new ArrayList<>(total);
        for (var i = 0; i < total; ++i) {
            var selected = creatures.get(random.nextInt(creatures.size()));
            for (var j = 1; j < arity; ++j) {
                var candidate = creatures.get(random.nextInt(creatures.size()));
                if (candidate.getFitness() > selected.getFitness()) {
                    selected = candidate;
                }
            }
            breeders.add(selected);
        }
        return breeders;
    }
}
