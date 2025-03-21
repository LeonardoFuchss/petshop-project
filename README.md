# 🐾 Petshop API

## 📌 Descrição

### Sistema de controle de atendimentos de uma petshop, fornecendo gerenciamento de cadastros de clientes e seus respectivos pets. O sistema possui dois tipos de acesso:

🔹 Admin: Pode incluir, excluir, alterar e visualizar qualquer cadastro. <br>
🔹 Cliente: Pode visualizar e alterar apenas os seus registros e/ou registros dos seus pets.

---

## 🎯 Objetivo

Implementar um serviço que permita a gestão de clientes e pets através de uma interface HTTP/REST.

---

## 🚀 Tecnologias Utilizadas e Requisitos

✅ Linguagem: Java 17 ou superior <br>
✅ Framework: Spring Boot / Quarkus / Micronaut <br>
✅ Banco de Dados: Relacional (PostgreSQL, MySQL, etc.) <br>
✅ Autenticação: JWT (JSON Web Token) <br>
✅ Controle de Acesso: Role-based authorization <br>
... Testes: Testes unitários para garantir o funcionamento da API <br>
✅ Controle de Versão: Git (repositório público) <br>

---

## 🛠 Como Executar o Projeto Localmente

1 - Clone o repositório (git clone <repo-url>) -> (cd nome-projeto)

2 - Instale o docker.

3 - Abra o projeto com Intellij (java 21)

4 - Configure o banco de dados no arquivo docker-compose.yml

5 - Rode o seguinte comando no terminal para subir os containers: docker compose up -d

---

## 🔑 Autenticação

A API utiliza autenticação baseada em JWT. Para acessar os endpoints protegidos:

1️⃣ Realize o login para obter um token JWT.

2️⃣ Utilize o token no cabeçalho das requisições




