package io.github.odunlamizo.jcm.model;

import java.util.Map;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnvRecord {

    private Map<String, String> values;
}
