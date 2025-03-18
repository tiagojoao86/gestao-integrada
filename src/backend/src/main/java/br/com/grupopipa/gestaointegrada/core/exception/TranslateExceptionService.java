package br.com.grupopipa.gestaointegrada.core.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TranslateExceptionService {

    public TranslateExceptionService() {
    }

    public String translateErrorMessage(String originalMessage) {
        return requestAI(buildPrompt(originalMessage));
    }

    public String requestAI(String prompt) {
        WebClient webClient = WebClient.create("http://localhost:11434/api");

        AIRequest requestBody = new AIRequest("gemma3:1b", prompt);

        // Enviando a requisição POST
        List<AIResponse> response = webClient.post()
                .uri("/generate")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(AIResponse.class)
                .collectList()
                .block();

        return response.stream().map(AIResponse::getResponse).reduce("", String::concat);
    }

    private String buildPromptWithError(String originalMessage) {
        StringBuilder prompt = new StringBuilder(originalMessage);

        /*
         * prompt.
         * append("Traduza em poucas palavras para um usuário leigo o que aconteceu nessa mensagem de erro: "
         * );
         * prompt.append(originalMessage + ". ");
         * prompt.append(
         * "Mas retorne apenas uma mensagem bem simplificada, por exemplo: Erro ao inserir um appuser pois já existe um appuser com esse login. "
         * );
         * prompt.append(
         * "Também omita qualquer informação técnica sobre o banco. Mas respeite o nome dos campos conforme na mensagem."
         * );
         */

        return prompt.toString();
    }

    private String buildPrompt(String originalMessage) {
        StringBuilder prompt = new StringBuilder();

        /*
         * prompt.append("Oi, você faz parte de um sistema de gestão. ");
         * prompt.append(
         * "A partir de agora sua função é traduzir as mensagens de erro que vem do banco de dados ou do JPA/Hibernate "
         * );
         * prompt.
         * append("de uma forma bem simples para o usuário final entenda o erro que ocorreu durante o cadastro. "
         * );
         * prompt.append("Por exemplo, se você receber a uma mensagem como: ");
         * prompt.append(
         * "could not execute statement [ERROR: duplicate key value violates unique constraint 'appuser_username_key' "
         * );
         * prompt.
         * append("Detail: Key (username)=(tiago.pereira) already exists.] [insert into appuser "
         * );
         * prompt.append(
         * "(createdAt,createdBy,name,password,updatedAt,updatedBy,username,id) values (?,?,?,?,?,?,?,?)]; "
         * );
         * prompt.
         * append("SQL [insert into appuser (createdAt,createdBy,name,password,updatedAt,updatedBy,username,id) "
         * );
         * prompt.
         * append("values (?,?,?,?,?,?,?,?)]; constraint [appuser_username_key]. ");
         * prompt.append("Você deve responder ao usuário simplesmente: ");
         * prompt.append("Já existe um 'appuser' com o login 'tiago.pereira'. ");
         * prompt.
         * append("Usando essas informações, qual seria a mensagem a partir desse erro?. "
         * );
         */
        prompt.append(
                "Resuma o erro abaixo em uma linha. Usando palavras simples para um usuário leigo.");
        prompt.append(originalMessage);

        return prompt.toString();
    }

}

@Getter
class AIResponse {
    @JsonProperty("model")
    private String model;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("response")
    private String response;

    @JsonProperty("done")
    private boolean done;

    @JsonProperty("done_reason")
    private String doneReason;

    @JsonProperty("context")
    private Integer[] context;

    @JsonProperty("total_duration")
    private Long totalDuration;

    @JsonProperty("load_duration")
    private Long loadDuration;

    @JsonProperty("prompt_eval_count")
    private Integer promptEvalCount;

    @JsonProperty("prompt_eval_duration")
    private Long promptEvalDuration;

    @JsonProperty("eval_count")
    private Integer evalCount;

    @JsonProperty("eval_duration")
    private Long evalDuration;
}

@AllArgsConstructor
@Getter
class AIRequest {
    private String model;
    private String prompt;
}
