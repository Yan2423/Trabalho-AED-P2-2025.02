
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
    public Map<Long, Long> encontrarPicosTransferencia(String caminho) throws IOException {
        System.out.println("[Desafio 4] Encontrando picos de transferência.");

        // Lê todos os registros
        List<Alerta> logs = LerLog.lerLogs(caminho);

        // Mapa resultado: timestampAtual -> timestampDoPróximoEventoComMaisBytes
        Map<Long, Long> mapaDePicos = new HashMap<>();

        if (logs.isEmpty()) {
            return mapaDePicos;
        }

        // Listas auxiliares para facilitar o acesso por índice
        List<Long> timestamps = new ArrayList<>();
        List<Long> bytesTransferidos = new ArrayList<>();

        for (Alerta log : logs) {
            timestamps.add(log.getTimestamp());
            bytesTransferidos.add(log.getBytesTransferred());
        }

        // Pilha de índices para aplicar o padrão Next Greater Element
        Stack<Integer> pilha = new Stack<>();

        // Percorre de trás pra frente
        for (int i = bytesTransferidos.size() - 1; i >= 0; i--) {

            long bytesAtuais = bytesTransferidos.get(i);

            // Remove da pilha os índices que NÃO têm bytes maiores que o atual
            while (!pilha.isEmpty() && bytesTransferidos.get(pilha.peek()) <= bytesAtuais) {
                pilha.pop();
            }

            // Se ainda restou algum índice na pilha, ele é o próximo pico de transferência
            if (!pilha.isEmpty()) {
                int indicePico = pilha.peek();
                long timestampAtual = timestamps.get(i);
                long timestampPico = timestamps.get(indicePico);
                mapaDePicos.put(timestampAtual, timestampPico);
            }

            // Empilha o índice atual
            pilha.push(i);
        }

        // Retorna o mapa de timestamp → próximo timestamp com BYTES_TRANSFERRED maior
        return mapaDePicos;
    }
    
    @Override
    public Optional<List<String>> rastrearContaminacao(String caminho,
                                                       String recursoInicial,
                                                       String recursoAlvo) throws IOException {
        System.out.println("[Desafio 5] Rastreando contaminação de " +
                recursoInicial + " até " + recursoAlvo + ".");

        // Lê todos os registros
        List<Alerta> logs = LerLog.lerLogs(caminho);

        // Grafo: recurso → lista de recursos acessados em seguida
        Map<String, List<String>> grafo = new HashMap<>();
        // Último recurso de cada sessão
        Map<String, String> ultimoRecursoPorSessao = new HashMap<>();
        // Conjunto de todos os recursos que aparecem
        Set<String> recursosExistentes = new HashSet<>();

        for (Alerta log : logs) {
            String sessionId = log.getSessionId();
            String recurso = log.getTargetResource();

            if (recurso == null || recurso.isEmpty()) {
                continue;
            }

            recursosExistentes.add(recurso);

            // Se já existia um recurso anterior nessa sessão, cria aresta anterior -> atual
            if (ultimoRecursoPorSessao.containsKey(sessionId)) {
                String anterior = ultimoRecursoPorSessao.get(sessionId);

                grafo.putIfAbsent(anterior, new ArrayList<>());
                grafo.get(anterior).add(recurso);
            }

            // Atualiza último recurso da sessão
            ultimoRecursoPorSessao.put(sessionId, recurso);
        }

        // Caso especial: recursoInicial == recursoAlvo
        if (recursoInicial.equals(recursoAlvo)) {
            if (recursosExistentes.contains(recursoInicial)) {
                List<String> caminhoTrivial = new ArrayList<>();
                caminhoTrivial.add(recursoInicial);
                // Retorna um caminho trivial contendo apenas o próprio recurso
                return Optional.of(caminhoTrivial);
            } else {
                // Recurso nunca apareceu no log
                return Optional.empty();
            }
        }

        // Se algum dos dois recursos não existir no log, não há caminho
        if (!recursosExistentes.contains(recursoInicial)
                || !recursosExistentes.contains(recursoAlvo)) {
            return Optional.empty();
        }

        // BFS para encontrar caminho mais curto
        Queue<String> fila = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        Map<String, String> pai = new HashMap<>();

        fila.add(recursoInicial);
        visitados.add(recursoInicial);

        boolean encontrou = false;

        while (!fila.isEmpty()) {
            String atual = fila.poll();

            if (atual.equals(recursoAlvo)) {
                encontrou = true;
                break;
            }

            List<String> vizinhos = grafo.get(atual);
            if (vizinhos == null) continue;

            for (String v : vizinhos) {
                if (!visitados.contains(v)) {
                    visitados.add(v);
                    pai.put(v, atual);
                    fila.add(v);
                }
            }
        }

        if (!encontrou) {
            // Não há caminho entre recursoInicial e recursoAlvo
            return Optional.empty();
        }

        // Reconstrói o caminho do recursoAlvo até o recursoInicial usando o mapa de pais
        List<String> Rota = new ArrayList<>();
        String atual = recursoAlvo;

        while (atual != null) {
            Rota.add(atual);
            atual = pai.get(atual);
        }

        // Inverte para ficar na ordem recursoInicial → recursoAlvo
        java.util.Collections.reverse(Rota);

        // Retorna o caminho encontrado encapsulado em Optional
        return Optional.of(Rota);
    }

    
}
