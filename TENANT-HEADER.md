# Multi-Tenancy: Header X-Tenant-ID Obrigatório

## 🎯 Regra Geral

**TODA requisição para a API deve incluir o header `X-Tenant-ID`** (exceto rotas públicas).

### ❌ Sem Header = Erro 400

```bash
# Requisição SEM header
curl http://localhost:8080/gestao-integrada/api/usuarios

# Resposta:
{
  "error": "Tenant não identificado. Header X-Tenant-ID é obrigatório."
}
Status: 400 Bad Request
```

### ✅ Com Header = OK

```bash
# Requisição COM header
curl -H "X-Tenant-ID: empresa-solar" \
  http://localhost:8080/gestao-integrada/api/usuarios

# Resposta: lista de usuários do tenant 'empresa-solar'
Status: 200 OK
```

---

## 🚫 Rotas Que NÃO Exigem Tenant

### 1. Rotas Administrativas

```bash
# Criar tenant (usa X-Admin-Token ao invés de X-Tenant-ID)
POST /admin/tenants
Header: X-Admin-Token: seu-token-secreto

# Buscar/ativar/suspender tenant
GET/POST /admin/tenants/{tenantId}/*
Header: X-Admin-Token: seu-token-secreto
```

### 2. Health Check

```bash
# Health check da aplicação
GET /actuator/health
GET /health

# Não precisa de tenant
```

### 3. Rotas de Autenticação

**⚠️ IMPORTANTE**: Login **EXIGE tenant**!

```bash
# Login - PRECISA informar o tenant
POST /auth/login
Header: X-Tenant-ID: empresa-solar
Body: {
  "login": "admin",
  "senha": "admin123"
}

# Motivo: Cada tenant tem seus próprios usuários
# O usuário "admin" do tenant "empresa-solar" é diferente do "admin" do tenant "outra-empresa"
```

---

## 🔒 Por Que Bloquear Requisições Sem Tenant?

### 1. Segurança

Sem tenant definido, o sistema tentaria acessar o schema `public`, que contém apenas metadados dos tenants. Isso poderia:
- Expor dados sensíveis
- Causar erros de SQL
- Permitir acesso não autorizado

### 2. Isolamento de Dados

Cada tenant DEVE ter seus dados isolados. Sem identificação do tenant, não há como garantir esse isolamento.

### 3. Integridade

Operações como INSERT, UPDATE, DELETE sem tenant definido poderiam corromper dados ou criar registros órfãos.

---

## 🎨 Frontend: Como Garantir Header Sempre Presente

### Solução: HTTP Interceptor

```typescript
// tenant.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable()
export class TenantInterceptor implements HttpInterceptor {
  
  constructor(private authService: AuthService) {}
  
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    // Obter tenant do usuário logado
    const tenantId = this.authService.getTenantId();
    
    // Rotas que não precisam de tenant
    const publicRoutes = ['/admin/tenants', '/actuator', '/health'];
    const isPublicRoute = publicRoutes.some(route => req.url.includes(route));
    
    if (tenantId && !isPublicRoute) {
      // Adiciona header automaticamente
      req = req.clone({
        setHeaders: { 'X-Tenant-ID': tenantId }
      });
    }
    
    return next.handle(req);
  }
}
```

### Fluxo Completo

```
1. Usuário acessa: app.gestao-integrada.com
2. Tela de login pede: Empresa + Login + Senha
   ├─ Empresa: "empresa-solar" (ou seleção de dropdown)
   └─ Login: "admin"
3. Frontend faz login:
   POST /auth/login
   Header: X-Tenant-ID: empresa-solar
   Body: { login: "admin", senha: "admin123" }
4. Backend valida:
   ├─ Interceptor identifica tenant: empresa-solar
   ├─ Define: TenantContext.setTenantId("tenant_empresa_solar")
   ├─ Hibernate usa: SET search_path TO tenant_empresa_solar
   └─ Busca usuário: SELECT * FROM usuario WHERE login = 'admin'
5. Login OK → JWT gerado com tenant_id
6. Frontend salva:
   ├─ localStorage.setItem('token', jwt)
   └─ localStorage.setItem('tenant_id', 'empresa-solar')
7. Todas as próximas requisições:
   ├─ TenantInterceptor adiciona: X-Tenant-ID: empresa-solar
   └─ Dados isolados automaticamente!
```

---

## 🧪 Cenários de Teste

### Cenário 1: Requisição Normal (com tenant)

```bash
curl -H "X-Tenant-ID: empresa-solar" \
  -H "Authorization: Bearer {token}" \
  http://localhost:8080/gestao-integrada/api/usuarios
```

**Resultado**: ✅ Lista usuários do tenant `empresa-solar`

---

### Cenário 2: Requisição Sem Tenant

```bash
curl -H "Authorization: Bearer {token}" \
  http://localhost:8080/gestao-integrada/api/usuarios
```

**Resultado**: ❌ Erro 400
```json
{
  "error": "Tenant não identificado. Header X-Tenant-ID é obrigatório."
}
```

---

### Cenário 3: Tenant Inválido

```bash
curl -H "X-Tenant-ID: tenant-que-nao-existe" \
  -H "Authorization: Bearer {token}" \
  http://localhost:8080/gestao-integrada/api/usuarios
```

**Resultado**: Depende da validação:
- Se validar existência: ❌ Erro 403 "Tenant não existe"
- Se não validar: ✅ Cria schema vazio (não recomendado)

**Recomendação**: Adicionar validação no interceptor!

---

### Cenário 4: Rotas Públicas

```bash
# Health check (OK sem tenant)
curl http://localhost:8080/gestao-integrada/api/actuator/health
✅ 200 OK

# Admin - criar tenant (precisa X-Admin-Token)
curl -H "X-Admin-Token: token-secreto" \
  -X POST http://localhost:8080/gestao-integrada/api/admin/tenants \
  -d '{"tenantId": "novo-tenant", ...}'
✅ 201 Created
```

---

## ⚠️ Considerações Importantes

### 1. Login Multitenancy

Você tem 3 opções para o fluxo de login:

#### Opção A: Tenant no Header (ATUAL)
```
POST /auth/login
Header: X-Tenant-ID: empresa-solar
Body: { login: "admin", senha: "admin123" }
```
**Prós**: Simples, explicito  
**Contras**: Frontend precisa saber tenant antes do login

#### Opção B: Tenant no Body
```
POST /auth/login
Body: { 
  tenantId: "empresa-solar",
  login: "admin", 
  senha: "admin123" 
}
```
**Prós**: Mais intuitivo para UI  
**Contras**: Endpoint especial que não segue padrão

#### Opção C: Login por Subdomínio
```
POST https://empresa-solar.gestao-integrada.com/auth/login
Body: { login: "admin", senha: "admin123" }
```
**Prós**: UX perfeita, tenant implícito  
**Contras**: Requer DNS wildcard, certificado SSL wildcard

### 2. JWT com Tenant

**Recomendação**: Incluir `tenant_id` no JWT

```json
{
  "sub": "admin",
  "tenant_id": "empresa-solar",
  "roles": ["ADMIN"],
  "exp": 1234567890
}
```

Benefícios:
- Frontend não precisa guardar tenant separado
- Validação adicional: token só vale para o tenant correto
- Mais seguro

### 3. Validação de Existência

**Melhorar TenantInterceptor** para validar se tenant existe:

```java
@Override
public boolean preHandle(...) {
    String tenantId = request.getHeader(TENANT_HEADER);
    
    // Validar se tenant existe
    if (!tenantRepository.existsByTenantId(tenantId)) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"error\":\"Tenant inválido ou inexistente\"}");
        return false;
    }
    
    // Validar se tenant está ativo
    Tenant tenant = tenantRepository.findByTenantId(tenantId);
    if (tenant.getStatus() == TenantStatus.SUSPENDED) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"error\":\"Tenant suspenso. Entre em contato com o suporte.\"}");
        return false;
    }
    
    TenantContext.setTenantId(tenant.getSchemaName());
    return true;
}
```

---

## 📋 Checklist de Implementação Frontend

- [ ] Criar tela de login com campo "Empresa" ou dropdown de tenants
- [ ] Salvar `tenant_id` no localStorage após login bem-sucedido
- [ ] Criar `TenantInterceptor` que adiciona header `X-Tenant-ID` automaticamente
- [ ] Registrar interceptor no `app.config.ts`
- [ ] Tratar erro 400 "Tenant não identificado" no error handler global
- [ ] Incluir `tenant_id` no payload do JWT (backend)
- [ ] Extrair `tenant_id` do JWT ao invés de localStorage (mais seguro)
- [ ] Adicionar validação: JWT só vale para o tenant correto

---

## 🎯 Resumo

| Situação | Header X-Tenant-ID | Resultado |
|----------|-------------------|-----------|
| GET /api/usuarios | ❌ Ausente | 400 Bad Request |
| GET /api/usuarios | ✅ Presente | 200 OK - Dados do tenant |
| POST /admin/tenants | ❌ Não precisa | Usa X-Admin-Token |
| GET /actuator/health | ❌ Não precisa | 200 OK |
| POST /auth/login | ✅ Obrigatório | 200 OK + JWT |

**Regra de ouro**: Se é dado de negócio (usuários, perfis, etc) → **X-Tenant-ID obrigatório**!
