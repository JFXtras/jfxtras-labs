package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rules;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rules.byrules.ByRule;

/** Interface that produces a stream of LocalDateTime start times for repeatable appointment */
// Experimental
public interface FrequencyRule {

    Stream<LocalDateTime> stream();
    
    Collection<ByRule> getByRules();
    void setByRules(Collection<ByRule> c);
}
