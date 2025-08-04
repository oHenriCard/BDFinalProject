// Arquivo: App.java
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // --- Configurações ---
            System.out.print("Informe o caminho do arquivo de dados (ex: src/data.csv): ");
            String dataPath       = scanner.nextLine();
            System.out.println("Informe o suporte mínimo (ex: 0.2 para 20%): ");
            double minSupport     = Double.parseDouble(scanner.nextLine());
            System.out.println("Informe a confiança mínima (ex: 0.6 para 60%): ");
            final double minTrust = Double.parseDouble(scanner.nextLine());
            
            System.out.printf("Executando com Suporte Mínimo de %.1f%% e Confiança Mínima de %.1f%%.\n\n",
                    minSupport * 100, minTrust * 100);
            try {
                List<Set<String>> transactionList = ReadData.readTransactions(dataPath);

                Apriori apriori = new Apriori(minSupport, minTrust); //<- Instanciar
                Results results = apriori.exec(transactionList);
                results.display();

            } catch (IOException e) {
                System.err.println("ERRO: Não foi possível ler o arquivo de dados '" + dataPath + "'.");
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}