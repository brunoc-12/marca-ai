# 🚀 Marca Aí — API de Agendamentos

<p align="center">
  <strong>Uma solução para gerenciamento de agendamentos, desenvolvida com Java e Spring Boot.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-Backend-orange?logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring%20Boot-Framework-6DB33F?logo=springboot" />
  <img src="https://img.shields.io/badge/Oracle-Database-F80000?logo=oracle" />
  <img src="https://img.shields.io/badge/Status-Em%20desenvolvimento-yellow" />
</p>

## 📌 Sobre o projeto

O **Marca Aí** é uma aplicação Back-end desenvolvida para facilitar o gerenciamento de agendamentos e organizar o atendimento de clientes.

A proposta é oferecer uma solução que centralize as informações dos clientes e seus respectivos agendamentos, tornando o processo de organização mais simples e eficiente.

O projeto está sendo desenvolvido utilizando Java e Spring Boot, com persistência de dados em Oracle Database e integração com Google Calendar como parte da proposta de gerenciamento de compromissos.

Além de solucionar um problema real, o Marca Aí também representa minha evolução prática em desenvolvimento Back-end, aplicação de conceitos de Programação Orientada a Objetos, construção de APIs REST e integração entre sistemas.

## 🎯 Objetivos

- Desenvolver uma API REST para gerenciamento de agendamentos.
- Aplicar boas práticas de desenvolvimento Back-end.
- Trabalhar com persistência e manipulação de dados relacionais.
- Implementar validações e regras de negócio.
- Explorar integrações com serviços externos.
- Desenvolver uma aplicação com organização e separação de responsabilidades.

## ⚙️ Tecnologias utilizadas

| Tecnologia | Aplicação |
|---|---|
| Java | Linguagem principal |
| Spring Boot | Desenvolvimento da API |
| Spring Data JPA | Persistência e acesso aos dados |
| Hibernate | Mapeamento objeto-relacional |
| Oracle Database | Banco de dados relacional |
| Maven | Gerenciamento de dependências e build |
| Google Calendar API | Integração com calendário |
| Git e GitHub | Versionamento de código |

## 🏗️ Arquitetura do projeto

A aplicação segue uma organização em camadas, buscando manter o código organizado, modular e de fácil manutenção.

```text
src/
└── main/
    ├── java/
    │   └── io/github/brunoc12/marcaai/
    │       ├── controller/
    │       ├── service/
    │       ├── repository/
    │       ├── model/
    │       └── ...
    │
    └── resources/
        └── application.properties
```

**Responsabilidades das camadas:**

- **Controller:** recebe as requisições HTTP e disponibiliza os endpoints da API.
- **Service:** concentra as regras de negócio e o processamento das operações.
- **Repository:** realiza o acesso e a comunicação com o banco de dados.
- **Model:** representa as entidades e estruturas de dados da aplicação.

## ✨ Funcionalidades

- [x] Estrutura inicial da aplicação Spring Boot.
- [x] Configuração de conexão com Oracle Database.
- [x] Organização da aplicação em camadas.
- [x] Desenvolvimento das estruturas de agendamento.
- [x] Implementação de componentes de acesso a dados e regras de negócio.
- [ ] Finalização e validação dos fluxos de agendamento.
- [ ] Integração completa com Google Calendar.
- [ ] Ampliação dos testes automatizados.

> O checklist deve acompanhar o estado real de cada funcionalidade antes da publicação da versão final.

## 🔌 API REST

A aplicação disponibiliza a estrutura de endpoints para gerenciamento de agendamentos.

**Recurso principal:**

| Método | Endpoint | Descrição |
|---|---|---|
| API | `/agendamentos` | Recurso de gerenciamento de agendamentos |

As operações e seus respectivos contratos devem ser consultados na implementação dos controllers.

## 🗄️ Banco de dados

O projeto utiliza Oracle Database para armazenamento e gerenciamento das informações.

A integração é realizada através do Spring Data JPA e Hibernate, permitindo o mapeamento das entidades Java para estruturas relacionais.

Configuração utilizada no ambiente local:

- Banco: Oracle Database
- Serviço: XEPDB1
- Usuário da aplicação: `marcaai`

## 🔐 Configuração de ambiente

As credenciais e informações sensíveis são configuradas por variáveis de ambiente, evitando a exposição de dados privados no repositório.

Variáveis utilizadas:

```properties
SPRING_DATASOURCE_PASSWORD=sua_senha

GOOGLE_CLIENT_ID=seu_client_id

GOOGLE_CLIENT_SECRET=seu_client_secret
```

O arquivo `application.properties` utiliza essas variáveis para configurar a conexão com o banco e a integração com Google Calendar.

**Importante:** nunca publique senhas, tokens, client secrets ou arquivos `.env` com credenciais reais no GitHub.

## 💻 Como executar o projeto

### 1. Pré-requisitos

Antes de executar a aplicação, você precisará ter:

- Java JDK instalado.
- Oracle Database configurado.
- Git instalado.
- IDE de sua preferência (IntelliJ IDEA, Eclipse ou VS Code).

### 2. Clone o repositório

```bash
git clone https://github.com/brunoc-12/marca-ai.git
```

### 3. Entre na pasta do projeto

```bash
cd marca-ai
```

### 4. Configure as variáveis de ambiente

No PowerShell, configure as variáveis para a sessão atual:

```powershell
$env:SPRING_DATASOURCE_PASSWORD="sua_senha"
$env:GOOGLE_CLIENT_ID="seu_client_id"
$env:GOOGLE_CLIENT_SECRET="seu_client_secret"
```

### 5. Execute a aplicação

Utilizando o Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Para executar a validação do build:

```powershell
.\mvnw.cmd clean verify
```

A aplicação poderá ser acessada localmente através de:

```text
http://localhost:8080
```

## 🔗 Google Calendar

O projeto contempla uma integração com Google Calendar para conectar o gerenciamento de agendamentos a um calendário externo.

A configuração OAuth utiliza o seguinte endereço de redirecionamento:

```text
http://localhost:8080/oauth2/callback
```

Para utilizar essa integração, é necessário configurar as credenciais OAuth 2.0 no Google Cloud Console e disponibilizar as variáveis de ambiente correspondentes.

## 📚 Conhecimentos aplicados

Durante o desenvolvimento deste projeto, estou aprofundando conhecimentos em:

- Java e Programação Orientada a Objetos.
- Desenvolvimento de APIs REST.
- Spring Boot e Spring Data JPA.
- Arquitetura em camadas.
- Operações CRUD.
- Mapeamento objeto-relacional (ORM).
- Oracle Database e SQL.
- Tratamento de exceções e validações.
- Integração com APIs externas.
- Versionamento de código com Git.

## 👨‍💻 Desenvolvedor

**Bruno Costa**

Estudante de Análise e Desenvolvimento de Sistemas na FIAP, com foco em desenvolvimento Back-end utilizando Java e Spring Boot.

Tenho interesse em arquitetura de software, bancos de dados, APIs REST e desenvolvimento de soluções que resolvam problemas reais.

- GitHub: [@brunoc-12](https://github.com/brunoc-12)
- LinkedIn: [Meu perfil profissional](https://www.linkedin.com/)

---

<p align="center">
  Desenvolvido por Bruno Costa
</p>

<p align="center">
  <strong>Marca Aí — Tecnologia para organizar melhor o seu tempo.</strong>
</p>
