package com.zynaps.bioforge.schemes;

import com.zynaps.bioforge.Creature;
import com.zynaps.bioforge.generators.RandomNumberGenerator;
import java.util.List;

public interface SelectionScheme {
    List<Creature> apply(List<Creature> creatures, RandomNumberGenerator random);
}
