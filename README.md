Sistema de Gestão de Saúde - Postinho
📋 Descrição do Projeto
Sistema web desenvolvido para a disciplina de Programação Web II, focado na gestão de uma unidade básica de saúde (postinho). O sistema oferece funcionalidades completas para agendamento de consultas e controle de estoque de medicamentos.

🎯 Objetivos
Implementar um sistema de agendamento de consultas médicas

Controlar o estoque de medicamentos da unidade

Gerenciar o cadastro de profissionais de saúde

Fornecer interface web intuitiva para atendentes e pacientes

🛠️ Tecnologias Utilizadas
Backend
Java 17 - Linguagem de programação

Spring Boot 3.x - Framework principal

Spring Data JPA - Persistência de dados

Spring Security - Controle de acesso e autenticação

Maven - Gerenciamento de dependências

Frontend
Thymeleaf - Template engine

Bootstrap 4 - Framework CSS

HTML5 - Estrutura das páginas

JavaScript - Interatividade

Banco de Dados
MySQL - Banco de dados relacional

H2 Database - Banco em memória (desenvolvimento)

📊 Funcionalidades Implementadas
✅ Módulo Médico (Completo)
CRUD completo de profissionais de saúde

Cadastro com dados completos (nome, CRM, especialidade, telefone)

Edição e exclusão lógica de médicos

Listagem com status de atividade

Validações de dados e CRM único

🔄 Em Desenvolvimento
Módulo Horários Disponíveis

Módulo Agendamento de Consultas

Módulo Controle de Estoque

Módulo Pacientes

Sistema de Autenticação e Roles

🏗️ Arquitetura do Sistema
text
src/
├── main/
│   ├── java/
│   │   └── com/example/demo/
│   │       ├── controller/     # Controladores MVC
│   │       ├── model/          # Entidades JPA
│   │       ├── repository/     # Interfaces Spring Data
│   │       ├── service/        # Lógica de negócio
│   │       └── DemoApplication.java
│   └── resources/
│       ├── templates/          # Views Thymeleaf
│       │   └── medico/
│       │       ├── index.html  # Listagem
│       │       ├── create.html # Cadastro
│       │       └── edit.html   # Edição
│       └── application.properties
🚀 Como Executar o Projeto
Pré-requisitos
Java 17 ou superior

Maven 3.6+

MySQL 8.0+

IDE (Spring Tool Suite, IntelliJ ou VS Code)

Configuração do Banco de Dados
sql
CREATE DATABASE sistema_saude;
Configuração da Aplicação
properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/sistema_saude
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
server.port=8080
Execução
bash
# Clonar o repositório
git clone [url-do-repositorio]

# Navegar até o diretório
cd projeto-web2

# Executar com Maven
mvn spring-boot:run

# Ou compilar e executar
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
📝 Entidades do Sistema
Medico
java
@Entity
public class Medico {
    private Long id;
    private String nome;
    private String crm;          // Único
    private String especialidade;
    private String telefone;
    private boolean ativo;
}
Próximas Entidades
Paciente - Dados dos pacientes

HorarioDisponivel - Agenda médica

Consulta - Agendamentos

Remedio - Controle de estoque

MovimentacaoEstoque - Auditoria

Diagnostico - CID-10

👥 Roles do Sistema
Atendente (ROLE_ATENDENTE)
Gerenciar médicos e horários

Controlar estoque de medicamentos

Cadastrar diagnósticos

Gerenciar usuários

Paciente (ROLE_PACIENTE)
Visualizar horários disponíveis

Marcar consultas

Consultar disponibilidade de medicamentos

🔒 Segurança
Spring Security para autenticação

Roles para controle de acesso

BCrypt para hash de senhas

CSRF protection habilitada

📈 Status do Projeto
✅ Concluído
Configuração do ambiente Spring Boot

Conexão com banco de dados MySQL

Entidade Medico com JPA

CRUD completo de médicos

Interface web com Thymeleaf

🚧 Em Desenvolvimento
Sistema de autenticação

Módulo de agendamentos

Controle de estoque

Relacionamentos entre entidades

📋 Pendente
Testes unitários e integração

Validações avançadas

Relatórios e estatísticas

Deploy em produção

👨‍💻 Desenvolvedores
[Seu Nome] - Desenvolvimento full-stack

📄 Licença
Este projeto é desenvolvido para fins acadêmicos na disciplina de Programação Web II.

🎓 Disciplina: Programação Web II
🏫 Instituição: [Nome da Universidade/Faculdade]
📅 Período: [Ano/Semestre]

