import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import br.edu.icev.aed.forense.AnaliseForenseAvancada;

public class LerArquivo {
     public static void main(String[] args) {
           // Caminho do arquivo CSV
        String caminho = "arquivo_logs.csv";  // ajuste o nome se necessário

        // Cria a implementação da interface
        AnaliseForenseAvancada analise = new Desafios();

        
           try {
            //  Executa o Desafio 1 → encontra sessões inválidas
            Set<String> sessoesInvalidas = analise.encontrarSessoesInvalidas(caminho);

            if (sessoesInvalidas.isEmpty()) {
                System.out.println("Nenhuma sessão inválida encontrada.");
                return;
            }

            System.out.println("\n Sessões inválidas encontradas:");
            for (String sessao : sessoesInvalidas) {
                System.out.println(" - " + sessao);
            }

            // Para cada sessão inválida, reconstrói a linha do tempo (Desafio 2)
            System.out.println("\n Reconstruindo linha do tempo das sessões inválidas...\n");

            for (String sessionId : sessoesInvalidas) {
                List<String> linhaTempo = analise.reconstruirLinhaTempo(caminho, sessionId);

                System.out.println(" Sessão: " + sessionId);
                if (linhaTempo.isEmpty()) {
                    System.out.println("   Nenhum evento encontrado.");
                } else {
                    for (String acao : linhaTempo) {
                        System.out.println("   -> " + acao);
                    }
                }
                System.out.println("-----------------------------");
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

    
        
    }
}
