# API REST para autenticação e controle de acesso de usuários
## Objetivo do projeto
O objetivo deste projeto é criar uma API REST para autenticação e autorização/controle de acesso de usuários de um sistema.
Trata-se, na verdade, de um módulo que pode ser integrado a um sistema maior, como e-commerce, gestão de clientes, sistema de gestão de estoque, etc.
## Breve discussão conceitual
Apesar de similares, autenticação e autorização são conceitos diferentes. 
A autenticação é o processo de confirmação da identidade de um usuário e validação de suas respectivas credenciais de acesso, enquanto a autorização consiste na verificação dos direitos e/ou permissões do usuário para acessar determinados recursos do sistema.
Logo, a autenticação antecede a autorização, ou seja, o usuário somente será autorizado a acessar determinados recursos do sistema se ele possuir credenciais de acesso válidas e obtiver sucesso no processo de autenticação.
Neste projeto, a autenticação é feita através de um Json Web Token (JWT), o qual é gerado após a confirmação dos dados de login informados pelo usuário (nome de usuário e senha, no caso). Há, porém, outras formas de autenticação, como por exemplo, a autenticação por sessão, a autenticação de dois fatores, a autenticação por certificado digital, a autenticação biométrica, etc. 
Por sua vez, a autorização é feita através de um sistema de permissões baseado em papéis/funções (roles), os quais são predefinidos conforme as regras de negócio do sistema (inicialmente, foram concedidos apenas os de usuário comum e de administrador).
O JWT implementado é da biblioteca `auth0/java-jwt`, que pode ser encontrada em https://github.com/auth0/java-jwt e utiliza uma chave secreta para gerar um hash único a partir de um algoritmo de criptografia.
## Casos de uso
Em sua primeira versão, esta API é capaz de:
- Cadastrar novos usuários (apenas nome de usuário e senha foram implementados, mas novos campos podem ser adicionados facilmente);
- A senha definida pelo usuário é armazenada no banco de dados criptografada (hash) com o algoritmo BCrypt;
- Autenticar usuários previamente cadastrados no banco de dados;
- Gerar um token de acesso válido após a autenticação;
- Após a criação do token, o sistema não deve solicitar nova autenticação do usuário para as próximas requisições, enquanto o token estiver válido;
- Gerenciar a concessão das permissões de acesso de cada usuário, baseado nas funções (roles) que ele possui;
- Controlar o acesso aos endpoints da API, de acordo com as permissões de cada usuário e conforme o método HTTP utilizado na requisição (neste momento, apenas do tipo GET ou POST).

Para acessar o sistema, o usuário deve informar seu nome de usuário e senha previamente cadastrados. 
A API então realizará uma consulta no banco de dados para verificar se o usuário existe e se a senha informada está correta.
Caso a autenticação seja bem-sucedida, a API retornará um token de acesso válido do tipo JWT, que deverá ser utilizado nas próximas requisições para acessar os endpoints privados da API. Se algum dos dados seja inválido, uma mensagem de erro será retornada, informando o código `401 (Unauthorized)`.
Após a autenticação, todas as novas requisições para endpoints privados devem conter o token JWT no campo `Authorization` do cabeçalho da requisição. Isso é necessário porque a política de criação da sessão foi definida na classe `SecurityConfigurations.java` como `STATELESS`. Neste projeto, a validade do token é de um dia, porém isso pode ser facilmente alterado conforme as regras de negócio.
Nas requisições seguintes, a API então verifica se o token JWT é válido e se o usuário possui permissão para acessar a rota requisitada. Caso contrário, retorna um erro do tipo `403 (Forbidden)`.

O controle de acesso é definido pela clase `RouteController.java` da seguinte forma:
- Os endpoint `/home` e `/login` são públicos e acessíveis a qualquer pessoa, mesmo sem autenticação;
- O endpoint `/home/user` é privado e acessível apenas a usuários autenticados do tipo usuário comum ou administrador. Esse controle é feito com base na função (role) do usuário, de modo que esta rota pode ser acessada por aqueles com as funções `ROLE_USER` ou `ROLE_ADMIN`;
- O endpoint `/home/admin` é privado e acessível apenas a usuários autenticados do tipo administrador (role `ROLE_ADMIN`).

A natural evolução do projeto consiste na integração da API a uma interface web para gerenciamento dos usuários e permissões de acesso, bem como possibilitando ao próprio usuário alteração de alguns de seus dados, a recuperação de senha, o envio de e-mails de confirmação e outros recursos relacionados à conta.

## Tecnologias utilizadas
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA 
- Hibernate
- Flyway
- Maven
- Lombok
- Auth0 JWT 4.2.1
- MySQL

## Como executar o projeto
Após clonar o repositório, é necessário configurar o acesso ao banco de dados no arquivo `application.properties` (localizado em `src/main/resources`). Se a propriedade `spring.jpa.hibernate.ddl-auto` estiver definida como `create` ou `create-drop`, o banco de dados será criado automaticamente com a execução da V1 da migration. Caso contrário, é necessário criá-lo manualmente.
Feito isso, basta executar o comando `mvn spring-boot:run` na raiz do projeto.
Para testar o acesso às rotas, é necessário utilizar um cliente HTTP (Postman, Insomnia ou a extensão Thunder Client do VS Code, por exemplo) e disparar as requisicões para os endpoints definidos na classe `RouteController.java`, conforme descrito acima.
O acesso ao endpoint público `/home` pode ser feito sem autenticação mediante uma requisição do tipo `GET`.
Para testar a autenticação, é necessário enviar uma requisição do tipo `POST` para o endpoint `/login` contendo os campos `username` e `password` no corpo da requisição em formato Json. Caso a autenticação seja bem-sucedida, o token JWT será retornado no campo `Authorization` do cabeçalho da resposta.
A seguir, para acessar os endpoints privados, basta enviar uma requisição do tipo `GET` para o endpoint desejado (`/home/user` ou `/home/admin`), contendo o token JWT no campo `Authorization` do cabeçalho da requisição. Observe que usuários comuns somente terão acesso ao primeiro endpoit, enquanto administradores poderão acessar ambos.

----------------------------------------------------------------------------------------------------------------------------

# User authentication and access control REST API
## Project's goal
This project is an implementation of a REST API that handles with user's authentication and authorization/access control.
It is, in fact, a module that can be integrated into a larger system, such as e-commerce, customer management, inventory management system, etc.
## Brief conceptual proposal
Although similar, authentication and authorization are different concepts.
Authentication is the process of confirming a user's identity and validating their respective access credentials, while authorization consists of verifying the user's rights and/or permissions to access certain system resources.
Therefore, authentication precedes authorization, that is, the user will only be authorized to access certain system resources if he has valid access credentials and is successful in the authentication process.
In this project, authentication is done through a Json Web Token (JWT), which is generated after confirming the login information data by the user (username and password, in this case). There are, however, other forms of authentication, such as session authentication, two-factor authentication, public and private key authentication, biometric authentication, etc. 
In turn, authorization is done through a system of permissions based on roles/functions, which are predefined according to the system's business rules (initially, only common user and administrator permissions were granted).
The implemented JWT is from the `auth0/java-jwt` library, which can be found at https://github.com/auth0/java-jwt and uses a secret key to generate a unique encrypted hash.
## Use cases
In its first version, this API is capable of:
- Create new users (only username and password fields were implemented, but other can be added easily);
- The password defined by the user is stored in the database encrypted (hashed) with the BCrypt algorithm;
- Authenticate previously registered users;
- Generate a valid access token after authentication;
- After generating the token, the system should not request new authentication from that user for the next requests, while the token is valid;
- Manage the granting of access permissions for each user, based on the functions (roles) he/she has;
- Control access to the endpoints, according to each user's permissions and to the HTTP method used in the request (at this moment, only GET or POST types).

To access the system, the user must inform his previously registered username and password.
The API will then query the database to verify that the user exists and the password is correct.
If authentication is successful, the API should return a valid JWT that must be used in future requests to access the private endpoints. If any of the data is invalid, an error message will be returned, stating the code `401 (Unauthorized)`.
After authentication, all new requests to private endpoints must contain the JWT token in the `Authorization` field of the request header. This is necessary because the session creation policy was defined in the `SecurityConfigurations` class as `STATELESS`. In this project, the validity of the token is one day, but this can be easily changed according to business rules.
In subsequent requests, the API then checks whether the JWT token is valid and whether the user has permission to access the requested route. Otherwise, it returns a `403 (Forbidden)` error.

Access control is defined by the `RouteController` class as follows:
- The `/home` and `/login` endpoints are public and accessible to anyone, even without authentication;
- The endpoint `/home/user` is private and accessible only to authenticated common or admin users. This control is done based on the user's role, so this specific route can be accessed by those with the roles `ROLE_USER` or `ROLE_ADMIN`;
- The `/home/admin` endpoint is private and accessible only to authenticated admin-type users (role `ROLE_ADMIN`).

Further up, the evolution of the project consists of integrating the API into a web interface for managing users and access permissions, as well as enabling the user to change some of his data, recover passwords, send confirmation e-mails and other account-related features.

## Technologies used
- Java 17
- SpringBoot 3
- Spring Security
- Spring Data JPA
- Hibernate
- Flyway
- Maven
- Lombok
- Auth0 JWT 4.2.1
- MySQL

## How to run the project
After cloning the repository, it is necessary to configure database access in the `application.properties` file (located in `src/main/resources`). If the `spring.jpa.hibernate.ddl-auto` property is set to `create` or `create-drop`, the database will be created automatically when running V1 of the migration. Otherwise, you have to do it manually.
Once that's done, just run the command `mvn spring-boot:run` in the root folder.
To test access to routes, it is necessary to use an HTTP client (such as Postman, Insomnia or the Thunder Client extension from VS Code) and send requests to the endpoints defined in the `RouteController` class, as described above.
Access to the public endpoint `/home` can be done without authentication through a `GET` type request.
To test the authentication, it is necessary to send a `POST` request to the `/login` endpoint containing the `username` and `password` fields in the request body in Json format. If authentication is successful, the JWT token will be returned in the `Authorization` field of the response header.
Then, to access the private endpoints, just send a `GET` request to the desired endpoint (`/home/user` or `/home/admin`), containing the JWT token in the `Authorization` field of the header of the request. Please note that regular users will only have access to the first endpoint, while administrators will be able to access both.

----------

Endpoint: `/home`
<img width="1203" alt="Captura de Tela 2022-12-23 às 10 12 43" src="https://user-images.githubusercontent.com/70707151/209350496-485c48be-0925-49fc-82bb-d945b5771926.png">
Endpoint: `/login` | Account: User
<img width="1203" alt="Captura de Tela 2022-12-23 às 10 13 10" src="https://user-images.githubusercontent.com/70707151/209350655-0e90d7e5-2888-49e2-83ce-7438bc466384.png">
Endpoint: `/home/user` | Account: User
<img width="1203" alt="Captura de Tela 2022-12-23 às 10 14 29" src="https://user-images.githubusercontent.com/70707151/209350840-18899075-a78b-46ea-a6ae-327c8af20233.png">
Endpoint: `/home/admin` | Account: User
<img width="1203" alt="Captura de Tela 2022-12-23 às 10 14 44" src="https://user-images.githubusercontent.com/70707151/209350934-aefc4b48-aab3-4886-bb2c-78f30fb5ac93.png">
Endpoint: `/login` | Account: Admin
<img width="1203" alt="Captura de Tela 2022-12-23 às 10 14 55" src="https://user-images.githubusercontent.com/70707151/209351149-bf390976-1bab-47e8-b613-6d73b4a76728.png">
Endpoint: `/home/admin` | Account: Admin
<img width="1203" alt="Captura de Tela 2022-12-23 às 10 15 36" src="https://user-images.githubusercontent.com/70707151/209351000-d3b60161-8433-44d5-9136-5b2a7404000c.png">
