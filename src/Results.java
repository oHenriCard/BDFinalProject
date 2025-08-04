import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Results {
    private final Map<Set<String>, Double> frequentItemSets;
    private final List<Rule> validRules;
    
    public Results(Map<Set<String>, Double> frequentItemSets, List<Rule> validRules) {
        this.frequentItemSets = frequentItemSets;
        this.validRules       = validRules;
    }

    public void display() {
        displayFrequentItemSets();
        displayAssociationRules();
    }

    private void displayFrequentItemSets() {
        System.out.println("-=-= ItemSets Frequentes =-=-");
        List<Map.Entry<Set<String>, Double>> orderList = new ArrayList<>(frequentItemSets.entrySet());
        orderList.sort((e1, e2) -> {
            int sizeCompare = Integer.compare(e1.getKey().size(), e2.getKey().size());
            if(sizeCompare == 0)
                return e1.toString().compareTo(e2.toString());
            return sizeCompare;
        });

        for (Map.Entry<Set<String>, Double> entry : orderList) 
            System.out.println(
                    String.format(
                        Locale.of("pt", "BR"), 
                        "%s (%.1f%%)", entry.getKey(), entry.getValue() * 100));;
    }

    private void displayAssociationRules() {
        System.out.println("-=-= Regras de Associação =-=-");
        validRules.stream()
                  .map(Rule::toString)
                  .sorted()
                  .forEach(System.out::println);
    }
}
