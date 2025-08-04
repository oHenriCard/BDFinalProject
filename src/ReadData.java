import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReadData {
    public static List<Set<String>> readTransactions(String dataPath) throws IOException {
        List<Set<String>> transactionList = new ArrayList<>();
        try (BufferedReader buffRead = new BufferedReader(new FileReader(dataPath))) {
            String line;
            while ((line = buffRead.readLine()) != null) {
                if(line.trim().isEmpty())
                    continue;
                String[] items = line.split(",");
                Set<String> transaction = new HashSet<>();
                for(String item : items)
                    transaction.add(item.trim());
                if(!transaction.isEmpty())
                    transactionList.add(transaction);
            }
        }
        return transactionList;
    }
}
