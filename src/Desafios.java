
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
    public  List<Alerta> priorizarAlertas(String var1, int var2) throws IOException{
        System.out.println("[Desafio 3] Método ainda não implementado.");
        List<Alerta> logs = LerLog.lerLogs(var1);
        return new ArrayList<>();
        
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