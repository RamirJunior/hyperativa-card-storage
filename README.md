# Hyperativa Card Storage API

API desenvolvida em **Java com Spring Boot** para cadastro e consulta de números de cartão, com suporte a upload de arquivos `.txt`, autenticação JWT, criptografia e escalabilidade para grandes volumes de dados.

## 📌 Funcionalidades

- **Autenticação**
    - Registro de usuário (`POST /hyperativa/auth/register`)
    - Login com geração de token JWT (`POST /Hyperativa/auth/login`)

- **Cartões**
    - Cadastro individual de cartão (`POST /hyperativa/cards`)
    - Upload de arquivo TXT contendo múltiplos cartões (`POST /hyperativa/cards/upload`)
    - Consulta de existência de cartão (`GET /hyperativa/cards/{cardNumber}`)

- **Validações**
    - Armazenamento seguro com **hash criptográfico** dos números dos cartões
    - Evita duplicações (não permite salvar o mesmo cartão duas vezes)

- **Logs**
    - Log de todas as requisições e respostas da API
    - Configuração para evitar exibição de SQL sensível no console


## 📂 Estrutura do Projeto (MVC)

- **Controller** → Camada de entrada das requisições HTTP
- **Service** → Regras de negócio (salvamento, consulta, validações)
- **Repository** → Persistência com Spring Data JPA
- **Utils** → Classes utilitárias (hashing, parsing de arquivo TXT)
- **Security** → Configuração JWT, filtros e permissões


## 🛠️ Tecnologias

- **Java 17+**
- **Spring Boot 3**
    - Spring Web
    - Spring Data JPA
    - Spring Security (JWT)
- **MySQL 8 (Docker)**
- **Adminer** (UI para gerenciar o banco)
- **Lombok**
- **JWT (JSON Web Token)**
- **BCrypt** para hashing de senhas
- **Maven** para build e dependências


## 🐳 Subindo o Banco com Docker

O projeto já inclui um `docker-compose.yml` para levantar o banco de dados e o Adminer.

👉 Subir os serviços:

```bash
docker-compose up -d
```

* MySQL disponível em: `localhost:3306`
* Adminer em: [http://localhost:8081](http://localhost:8081)



## 🔑 Endpoints da API

### 🔐 Autenticação

#### Registrar Usuário

```http
POST /hyperativa/auth/register
Content-Type: application/json

{
  "email": "user@email.com",
  "password": "12345678"
}
```

#### Login

```http
POST /hyperativa/auth/login
Content-Type: application/json

{
  "email": "user@email.com",
  "password": "12345678"
}
```

Resposta:

```json
{
  "token": "jwt-token-aqui"
}
```

> O token deve ser enviado no header das demais requisições:
>
> `Authorization: Bearer <token>`

---

### 💳 Cartões

#### Criar cartão

```http
POST /hyperativa/cards
Content-Type: application/json
Authorization: Bearer <token>

{
  "cardNumber": "4456897999999999"
}
```

#### Upload de arquivo TXT

```http
POST /hyperativa/cards/upload
Content-Type: multipart/form-data
Authorization: Bearer <token>

file=@arquivo.txt
```

#### Consultar cartão

```http
GET /hyperativa/cards/4456897999999999
Authorization: Bearer <token>
```

Resposta se existir:

```json
{
  "id": "f3e1e4c2-7d5c-4b9c-a1f8-0e123abc4567",
  "exists": true
}
```

Resposta se não existir:

```json
404 Not Found
```

---

## 🔒 Segurança

* Senhas de usuários são armazenadas com **BCrypt**
* Números de cartões são armazenados apenas como **hash (SHA-256)**
* Autenticação via **JWT**
* Configuração preparada para uso com **HTTPS** (recomendado em produção)

---

## 📈 Escalabilidade

* Leitura de arquivos TXT feita de forma **streaming** (linha a linha), evitando sobrecarregar a memória
* Integrado com **batch inserts** com tamanho de volumes personalizável para melhorar performance em grandes volumes
* Banco de dados configurado em container, fácil de escalar com réplicas

---

## ▶️ Como rodar o projeto

1. Suba o banco com Docker:

   ```bash
   docker-compose up -d
   ```
_Após subir o banco, ele estará disponível pelo Adminer em [http://localhost:8081](http://localhost:8081)_
* Para acesso direto ao banco utilize os dados abaixo:
  * Servidor: mysql
  * Usuário: User
  * Senha: User123
  * Base de Dados: hyperativa_db

2. Rode a aplicação:

   ```bash
   ./mvnw spring-boot:run
   ```

3. Acesse a API em:
   [http://localhost:8080](http://localhost:8080)

<hr>

_**Aviso importante / Disclaimer**_

_Contexto: este repositório contém a solução desenvolvida para um teste técnico;_
_Dados sensíveis: o arquivo fornecido pelo avaliador (arquivo `.txt`) NÃO está incluído neste repositório. Todos os exemplos no código e nos testes usam **dados gerados/teste** ou **valores mascarados**._

_Resumo das decisões de segurança:_

​	_- Não foram utilizados números de cartão reais._

​	_- Não incluí o arquivo original recebido pelo avaliador._

​	_- O repositório contém código para validação/transformação de dados e exemplos com cartões de teste apenas._

​	_- Veja o arquivo <a href="PROIBIDO_USO.md">`PROIBIDO_USO.md`</a> para restrições de uso._