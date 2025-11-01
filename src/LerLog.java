import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import br.edu.icev.aed.forense.Alerta;

public class LerLog {
    
    public static List<Alerta> lerLogs(String caminho) throws IOException{
        List<Alerta> logs = new ArrayList<>(); 

        try (BufferedReader buff = new BufferedReader(new FileReader(caminho))){
              String linha = buff.readLine(); // lê e ignora o cabeçalho

            while ((linha = buff.readLine()) != null) {
            linha = linha.trim();
            if (linha.isEmpty()) continue; // evita linhas vazias

            
            String[] partes = linha.split(",");
            if (partes.length < 7) continue;
                      
                    

                long timestamp = Long.parseLong(partes[0].trim().replace("\"", ""));
                String userId = partes[1].trim().replace("\"", "");
                String sessionId = partes[2].trim().replace("\"", "");
                String action = partes[3].trim().replace("\"", "").toUpperCase();
                String target = partes[4].trim().replace("\"", "");
                int severityLevel = Integer.parseInt(partes[5].trim().replace("\"", ""));
                Long bytesTransferred = Long.parseLong(partes[6].trim().replace("\"", ""));


                logs.add(new Alerta(timestamp, userId, sessionId, action, target,severityLevel, bytesTransferred));
            }


         
        }

        return logs;

         

    }
}
