import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import br.edu.icev.aed.forense.AnaliseForenseAvancada;

public class LerArquivo {
     public static void main(String[] args) {
           // Caminho do arquivo CSV
        String caminho = "arquivo_logs.csv";  // ajuste o nome se necessário

        // Cria a implementação da interface
        AnaliseForenseAvancada analise = new Desafios();

        try {
            System.out.println("🔍 Iniciando análise forense no arquivo: " + caminho);

            // Chama o Desafio 1
            Set<String> sessoesInvalidas = analise.encontrarSessoesInvalidas(caminho);

            System.out.println("\n⚠️ Sessões inválidas encontradas:");  
            if (sessoesInvalidas.isEmpty()) {
                System.out.println("✅ Nenhuma sessão inválida detectada.");
            } else {
                for (String id : sessoesInvalidas) {
                    System.out.println("- " + id + " - "  );
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        
    
        
    }
}
