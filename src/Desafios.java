
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

import br.edu.icev.aed.forense.Alerta;
import br.edu.icev.aed.forense.AnaliseForenseAvancada;

public class Desafios implements AnaliseForenseAvancada{

    @Override
    public  Set<String> encontrarSessoesInvalidas(String caminho) throws IOException {

        List<Alerta> logs = LerLog.lerLogs(caminho);

        Set<String> sessoesInvalidas = new HashSet<>();
        
        Map<String, Stack<String>> pilhaPorUsuario = new HashMap<>();

        for(Alerta log : logs){

            long Timestamp = log.getTimestamp();
            String userId = log.getUserId();
            String sessionId = log.getSessionId();
            String action = log.getActionType();

            //Insere um par Chave-Valor
            pilhaPorUsuario.putIfAbsent(userId, new Stack<>());
            Stack<String> pilha = pilhaPorUsuario.get(userId);

            if(action.equals("LOGIN")){
                  // Se a pilha não estiver vazia, é um login aninhado → inválido
                if (!pilha.isEmpty()) {
                    sessoesInvalidas.add(sessionId);
                }
                pilha.push(sessionId);
            }
             else if (action.equals("LOGOUT")) {
                // Se a pilha estiver vazia → logout sem login
                if (pilha.isEmpty()) {
                    sessoesInvalidas.add(sessionId);
                } 
                // Se o topo não for a mesma sessão → logout incorreto
                else if (!pilha.peek().equals(sessionId)) {
                    sessoesInvalidas.add(sessionId);
                } 
                // Caso contrário, logout válido
                else {
                    pilha.pop();
                }
            }
        }
          // Sessões que ficaram abertas → inválidas
        for (Stack<String> pilha : pilhaPorUsuario.values()) {
            while (!pilha.isEmpty()) {
                sessoesInvalidas.add(pilha.pop());
            }
        }

        return sessoesInvalidas;

        
    }
      // Outros desafios não implementados
     @Override
    public List<String> reconstruirLinhaTempo(String caminho, String sessionId) throws IOException {
        System.out.println("[Desafio 2] Reconstruindo linha do tempo da sessão: " + sessionId);
        List<Alerta> logs = LerLog.lerLogs(caminho);
        Queue<String> fila = new LinkedList<>();
        
        
        
        for(Alerta log : logs){
            if(log.getSessionId().equals(sessionId)){
                fila.add(log.getActionType());
            }
        }
        if(fila.isEmpty()){
            System.out.println("Nenhuma sessão foi encontrada" + sessionId);
            return new ArrayList<>();
        }

        List<String> linhaDoTempo = new ArrayList<>();
         while (!fila.isEmpty()) {
            linhaDoTempo.add(fila.poll());

        
        }
            // Retorna a lista final, na ordem cronológica
            return linhaDoTempo;
       
    }
    
@Override
    public List<Alerta> priorizarAlertas(String caminho, int n) throws IOException {
        System.out.println("[Desafio 3] Priorizando " + n + " alertas mais severos.");

        // Lê todos os registros
        List<Alerta> logs = LerLog.lerLogs(caminho);

        // Lista que conterá os N alertas mais severos
        List<Alerta> alertasMaisSeveros = new ArrayList<>();

        // Se n <= 0 ou não há logs, não há o que priorizar
        if (n <= 0 || logs.isEmpty()) {
            return alertasMaisSeveros;
        }

        // PriorityQueue ordenada por severidade decrescente
        java.util.PriorityQueue<Alerta> filaPrioridade = new java.util.PriorityQueue<>(
                (a, b) -> Integer.compare(b.getSeverityLevel(), a.getSeverityLevel())
        );

        // Insere todos os alertas na fila de prioridade
        for (Alerta log : logs) {
            filaPrioridade.add(log);
        }

        // Remove os N primeiros mais severos
        for (int i = 0; i < n && !filaPrioridade.isEmpty(); i++) {
            alertasMaisSeveros.add(filaPrioridade.poll());
        }

        // Retorna a lista dos N alertas com maior nível de severidade
        return alertasMaisSeveros;
        
    }

    @Override
    public  Map<Long, Long> encontrarPicosTransferencia(String var1) throws IOException {
        System.out.println("[Desafio 4] Método ainda não implementado.");
        return new HashMap<>();
    }

    @Override
    public  Optional<List<String>> rastrearContaminacao(String var1, String var2, String var3) throws IOException {
        System.out.println("[Desafio 5] Método ainda não implementado.");
        return Optional.empty();
    }

    
}
