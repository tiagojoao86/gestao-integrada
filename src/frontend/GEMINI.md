# Gemini - Frontend

> **Nota:** Eu sou um assistente de IA. Se eu identificar informações importantes que possam ser adicionadas a este arquivo para melhorar nossas interações futuras (como novos comandos, convenções ou detalhes de arquitetura), irei sugerir atualizações. Sinta-se à vontade para me perguntar como melhorá-lo.

## Resumo do Projeto

Este projeto contém a interface de usuário (UI) para o sistema Gestão Integrada. É uma Single Page Application (SPA) construída para interagir com a API do backend.

## Stack Tecnológica

- TypeScript
- Angular
- Angular CLI
- CSS

## Comandos Essenciais

**Nota:** Execute os comandos a partir do diretório `src/frontend`.

- **Instalar Dependências:**
  ```bash
  npm install
  ```

- **Executar a Aplicação (desenvolvimento):**
  ```bash
  ng serve
  ```

- **Executar os Testes:**
  ```bash
  ng test
  ```

- **Verificação de Estilo (Lint):**
  ```bash
  ng lint
  ```

- **Gerar o Build de Produção:**
  ```bash
  ng build
  ```

## Arquitetura e Convenções

- A arquitetura é baseada em componentes, seguindo as melhores práticas do Angular.
- **Componentes Reutilizáveis:** Ficam em `src/app/components/base`.
- **Componentes de Tela (Features):** Organizados por funcionalidade, como em `src/app/components/cadastro`.
- **Serviços e Modelos de Dados:** Para manter a coesão, os serviços e modelos (DTOs) específicos de uma funcionalidade estão localizados dentro do diretório do seu respectivo componente. Por exemplo, `usuario.service.ts` e os DTOs de usuário estão em `src/app/components/cadastro/usuario/`.
- **Serviços e Modelos Compartilhados:** Lógica e modelos que são compartilhados por toda a aplicação ou por múltiplos componentes base ficam em `src/app/components/base/`, dentro de subdiretórios apropriados (ex: `auth`, `model`).

## Autorização e Proteção de Rotas

- **Autenticação:** Todas as rotas que exigem que o usuário esteja logado devem ser protegidas pelo `authGuard`. Ele valida se existe um token de autenticação ativo.
- **Autorização por Módulo:** Para controlar o acesso a funcionalidades específicas (como telas de cadastro), utilizamos o `moduleAuthorityGuard`.
  - Este guarda (`guard`) é configurado na definição da rota e recebe a `key` do módulo como um dado (`data`).
  - Exemplo de como proteger uma rota e exigir a permissão de "listar" para o módulo `CADASTRO_USUARIO`:
    ```typescript
    {
      path: 'usuario',
      loadComponent: () => import('./usuario/usuario.component').then(m => m.UsuarioComponent),
      canActivate: [authGuard, moduleAuthorityGuard],
      data: {
        module: 'CADASTRO_USUARIO'
      }
    }
    ```
  - O `moduleAuthorityGuard` utiliza o `AuthService` para verificar se o usuário logado possui a permissão necessária (`LISTAR`) para o módulo especificado.

- **Autorização por Grupo:** Para controlar o acesso a seções maiores da aplicação (como um grupo de cadastros), utilizamos o `groupAuthorityGuard`.
  - Este guarda (`guard`) é configurado na rota principal da seção e recebe o nome do grupo como um dado (`data`).
  - Exemplo de como proteger uma rota e exigir que o usuário pertença ao grupo `CADASTROS`:
    ```typescript
    {
      path: 'cadastros',
      loadComponent: () => import('./cadastro/cadastro.component').then(m => m.CadastroComponent),
      canActivate: [authGuard, groupAuthorityGuard],
      data: {
        group: 'CADASTROS'
      }
    }
    ```
  - O `groupAuthorityGuard` utiliza o `AuthService` para verificar se o usuário logado possui a permissão para o grupo especificado.

## Estrutura de Diretórios

- `src/app`: Código-fonte da aplicação (componentes, serviços, rotas).
- `src/assets`: Imagens, ícones e outros arquivos estáticos.
- `src/styles.css`: Estilos globais da aplicação.
- `angular.json`: Arquivo de configuração do Angular CLI.

## Como Você Pode Me Ajudar

- **Corrigindo meu curso:** Se você notar que estou tentando navegar para uma rota que não existe, ou usando um nome de arquivo incorreto, por favor, me corrija. Por exemplo, se eu usar `/principal/home` quando a rota correta é `/`, me avise. Isso me ajuda a aprender a estrutura do seu projeto mais rapidamente.
