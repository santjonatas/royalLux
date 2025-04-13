# Royal Lux - Sistema de Gestão para Salão de Beleza

**Transformando sua Aparência de Forma Esplêndida, em Cada Detalhe.**

Royal Lux é um sistema web completo voltado para a administração de salões de beleza, desenvolvido com tecnologias modernas e boas práticas de engenharia de software. O sistema oferece uma experiência eficiente e intuitiva tanto para funcionários quanto para clientes.

## Funcionalidades

### Para Funcionários
- Autenticação segura com controle de acesso.
- Cadastro e gerenciamento de clientes.
- Agendamento de serviços.
- Administração de produtos e serviços oferecidos.

### Para Clientes
- Cadastro de perfil.
- Visualização de serviços disponíveis.

## Tecnologias Utilizadas

### Backend
- **Java 23**
- **Spring Boot**
- **Spring Security**
- **PostgreSQL**
- **JUnit + Mockito** (testes)
- **Arquitetura Limpa (Clean Architecture)**
- **Princípios SOLID**
- **API RESTful**

### Frontend
- **React**
- **TypeScript**
- **Vite**
- **HTML5 + CSS3**

### DevOps e Controle de Versão
- **Git**
- **GitHub**

## Estrutura do Projeto

A arquitetura do sistema é baseada em camadas bem definidas, promovendo manutenibilidade, testabilidade e escalabilidade:

```
royal-lux/
├── backend/
│   ├── core/
│   ├── infra/
│   └── presentation/
```

## Requisitos

- Java 23+
- Node.js 18+
- PostgreSQL

## Como Rodar o Projeto Localmente

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

### Banco de Dados
Certifique-se de ter uma instância PostgreSQL rodando e atualize as credenciais no `application.yml`.
