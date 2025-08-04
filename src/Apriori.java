import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Apriori {
    private final double minSupport;
    private final double minConfianca;
    

    public Apriori(double minSupport, double minConfianca) {
        this.minSupport   = minSupport;
        this.minConfianca = minConfianca;
    }

    public Results exec(List<Set<String>> transactionList) {
        Map<Set<String>, Double> frequentItemSets = generateFrequentItemSets(transactionList);
        List<Rule> validRules = generateAssociationRules(frequentItemSets, transactionList.size());
        return new Results(frequentItemSets, validRules);
    }

    public Map<Set<String>, Double> generateFrequentItemSets(List<Set<String>> transactionList) {
        Map<Set<String>, Double>  allFrequentItemSets = new HashMap<>();
        Map<Set<String>, Integer> C1 = new HashMap<>();
        // Contagem dos itens individuais (passo 1 do Apriori).
        for(Set<String> transaction : transactionList) {
            for(String item : transaction) {
                Set<String> itemSet = new HashSet<>(Collections.singletonList(item));
                C1.put(itemSet, C1.getOrDefault(itemSet,0) + 1);
            }
        }
        //Filtra os itens frequentes.
        List<Set<String>> lk = filterBySupport(C1, transactionList.size());
        lk.forEach(itemSet -> allFrequentItemSets.put(itemSet, (double) C1.get(itemSet) / transactionList.size()));
        // Gera itemsets de tamanho crescente
        for(int k = 2; !lk.isEmpty(); k++) {
            //Gera os candidatos
            List<Set<String>> ck = generateCandidates(lk, k); 
            //Conta as ocorrências
            Map<Set<String>, Integer> CkCount = countOccurrences(ck, transactionList); 
            //Filtra por suporte
            lk = filterBySupport(CkCount, transactionList.size());
            lk.forEach(itemSet -> allFrequentItemSets.put(itemSet, (double) CkCount.get(itemSet) / transactionList.size()));
        }
        return allFrequentItemSets;
    }

    public List<Rule> generateAssociationRules(Map<Set<String>, Double> frequentItemSets, int transactionTotal) {
        List<Rule> validRules = new ArrayList<>();
        for (Map.Entry<Set<String>, Double> entry : frequentItemSets.entrySet()) {
            Set<String> itemSet = entry.getKey();
            if (itemSet.size() < 2) continue;

            //Gera todos os subconjuntos possíveis
            List<Set<String>> subsets = generateSubsets(itemSet);

            for (Set<String> antecedent : subsets) {
                if (antecedent.isEmpty() || antecedent.equals(itemSet)) continue;

                Double suporteAntecedente = frequentItemSets.get(antecedent);
                if (suporteAntecedente == null) continue;

                // Calcula a confiança: suporte(itemset) / suporte(antecedente
                double confianca = entry.getValue() / suporteAntecedente;

                if (confianca >= minConfianca) {
                    Set<String> consequent = new HashSet<>(itemSet);
                    consequent.removeAll(antecedent);
                    validRules.add(new Rule(antecedent, consequent, entry.getValue(), confianca));
                }
            }
        }
        return validRules;
    }
    private Map<Set<String>, Integer> countOccurrences(List<Set<String>> itemSets, List<Set<String>> transactionList) {
        Map<Set<String>, Integer> counts = new HashMap<>();
        for (Set<String> transaction : transactionList) {
            for (Set<String> itemset : itemSets) {
                if (transaction.containsAll(itemset)) {
                    counts.put(itemset, counts.getOrDefault(itemset, 0) + 1);
                }
            }
        }
        return counts;
    }

    private List<Set<String>> filterBySupport(Map<Set<String>, Integer> counts, int transactionTotal) {
        List<Set<String>> frequentItemSets = new ArrayList<>();
        for (Map.Entry<Set<String>, Integer> entry : counts.entrySet()) {
            if ((double) entry.getValue() / transactionTotal >= minSupport) {
                frequentItemSets.add(entry.getKey());
            }
        }
        return frequentItemSets;
    }

    private List<Set<String>> generateCandidates(List<Set<String>> lk_1, int k) {
        Set<Set<String>> candidate = new HashSet<>();
        for (int i = 0; i < lk_1.size(); i++) {
            for (int j = i + 1; j < lk_1.size(); j++) {
                Set<String> join = new HashSet<>(lk_1.get(i));
                join.addAll(lk_1.get(j));
                if (join.size() == k) {
                    candidate.add(join);
                }
            }
        }
        return new ArrayList<>(candidate);
    }

    private List<Set<String>> generateSubsets(Set<String> set) {
        List<Set<String>> AllSubsets = new ArrayList<>();
        List<String> elements = new ArrayList<>(set);
        int n = elements.size();
        for (int i = 1; i < (1 << n); i++) {
            Set<String> subsets = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subsets.add(elements.get(j));
                }
            }
            if (!subsets.isEmpty()) {
                AllSubsets.add(subsets);
            }
        }
        return AllSubsets;
    }
    
    
}
