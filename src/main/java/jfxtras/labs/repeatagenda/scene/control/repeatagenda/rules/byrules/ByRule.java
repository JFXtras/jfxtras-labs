package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rules.byrules;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public interface ByRule {

    Stream<LocalDateTime> stream();
}
