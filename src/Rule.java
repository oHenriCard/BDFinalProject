import java.util.Locale;
import java.util.Set;

public class Rule {
    private final Set<String> antecedent;
    private final Set<String> consequent;
    private final double      support;
    private final double      trust;
    
    public Rule(Set<String> antecedent, Set<String> consequent, double support, double trust) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.support    = support;
        this.trust      = trust;
    }

    @Override
    public String toString() {
        return String.format(Locale.of("pt", "BR"),
                     "%s --> %s (Suporte: %.1f%%, Confiança: %.1f%%)",
                     antecedent, consequent, support * 100, trust * 100);
    }
}
