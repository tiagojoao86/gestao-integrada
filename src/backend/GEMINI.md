# Gemini - Backend

> **Nota:** Eu sou um assistente de IA. Se eu identificar informações importantes que possam ser adicionadas a este arquivo para melhorar nossas interações futuras (como novos comandos, convenções ou detalhes de arquitetura), irei sugerir atualizações. Sinta-se à vontade para me perguntar como melhorá-lo.

## Resumo do Projeto

Este projeto contém a API RESTful para o sistema Gestão Integrada. É responsável pela lógica de negócios, acesso ao banco de dados e segurança.

## Stack Tecnológica

- Java
- Spring Boot
- Maven
- Flyway (para migrações de banco de dados)
- JPA/Hibernate

## Comandos Essenciais

**Nota:** Execute os comandos a partir do diretório `src/backend`.

- **Compilar o Projeto:**
  ```bash
  ./mvnw compile
  ```

- **Executar os Testes:**
  ```bash
  ./mvnw test
  ```

- **Executar a Aplicação (desenvolvimento):**
  ```bash
  ./mvnw spring-boot:run
  ```

- **Verificação de Estilo (Lint):**
  *(Confirme o comando no `pom.xml`, mas geralmente é algo como:)*
  ```bash
  ./mvnw checkstyle:check
  ```

- **Gerar o Build de Produção:**
  ```bash
  ./mvnw package
  ```

## Arquitetura e Convenções

- A arquitetura segue o padrão Model-View-Controller (MVC), comum em aplicações Spring.
- As entidades do banco de dados estão em `src/main/java/br/com/grupopipa/gestaointegrada/core/entity`.
- Os serviços (lógica de negócio) estão em `src/main/java/br/com/grupopipa/gestaointegrada/core/service`.
- As migrações de banco de dados com Flyway estão em `src/main/resources/db/migration`. Crie novos scripts de migração para qualquer alteração no schema.

## Estrutura de Diretórios

- `src/main/java`: Código-fonte da aplicação.
- `src/main/resources`: Arquivos de configuração, scripts SQL e chaves.
- `src/test/java`: Testes unitários e de integração.
