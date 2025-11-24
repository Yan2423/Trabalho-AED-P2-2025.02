 import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Optional;



import br.edu.icev.aed.forense.Alerta;
import br.edu.icev.aed.forense.AnaliseForenseAvancada;

public class LerArquivo {

    public static void main(String[] args) {

        // Caminho do arquivo CSV
        String caminho = "arquivo_logs.csv";

        // Implementação da interface
        AnaliseForenseAvancada analise = new Desafios();

        try {
            System.out.println("===== DESAFIO 1: Sessões inválidas =====");

            // ----------------- DESAFIO 1 -----------------
            Set<String> sessoesInvalidas = analise.encontrarSessoesInvalidas(caminho);

            if (sessoesInvalidas.isEmpty()) {
                System.out.println("Nenhuma sessão inválida encontrada.\n");
            } else {
                System.out.println("Sessões inválidas encontradas:");
                for (String sessao : sessoesInvalidas) {
                    System.out.println(" - " + sessao);
                }
            }

            // ----------------- DESAFIO 2 -----------------
            System.out.println("\n===== DESAFIO 2: Linha do Tempo =====");

            for (String sessionId : sessoesInvalidas) {
                List<String> linhaTempo = analise.reconstruirLinhaTempo(caminho, sessionId);

                System.out.println("\nSessão: " + sessionId);
                if (linhaTempo.isEmpty()) {
                    System.out.println("   Nenhum evento encontrado.");
                } else {
                    for (String acao : linhaTempo) {
                        System.out.println("   -> " + acao);
                    }
                }
                System.out.println("-----------------------------");
            }

            // ----------------- DESAFIO 3 -----------------
            System.out.println("\n===== DESAFIO 3: Alertas mais severos =====");

            int n = 5; // Quantos alertas exibir
            List<Alerta> maisSeveros = analise.priorizarAlertas(caminho, n);

            if (maisSeveros.isEmpty()) {
                System.out.println("Nenhum alerta encontrado.");
            } else {
                for (Alerta alerta : maisSeveros) {
                    System.out.println("Severidade: " + alerta.getSeverityLevel() +
                            " | Sessão: " + alerta.getSessionId() +
                            " | Ação: " + alerta.getActionType());
                }
            }

            // ----------------- DESAFIO 4 -----------------
            System.out.println("\n===== DESAFIO 4: Picos de transferência =====");

            Map<Long, Long> picos = analise.encontrarPicosTransferencia(caminho);

            if (picos.isEmpty()) {
                System.out.println("Nenhum pico encontrado.");
            } else {
                for (Map.Entry<Long, Long> entry : picos.entrySet()) {
                    System.out.println("Timestamp " + entry.getKey() +
                            " → próximo pico em " + entry.getValue());
                }
            }

            // ----------------- DESAFIO 5 -----------------
            System.out.println("\n===== DESAFIO 5: Rastrear Contaminação =====");

            String recursoInicial = "arquivo.exe";   // Ajuste se quiser
            String recursoAlvo = "config.sys";       // Ajuste se quiser

            Optional<List<String>> rota =
                    analise.rastrearContaminacao(caminho, recursoInicial, recursoAlvo);

            System.out.println("\nCaminho de contaminação (" +
                    recursoInicial + " → " + recursoAlvo + "):");

            if (rota.isEmpty()) {
                System.out.println("Nenhuma rota encontrada.");
            } else {
                rota.get().forEach(r -> System.out.println(" -> " + r));
            }

            System.out.println("\n===== FIM =====");

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }
}