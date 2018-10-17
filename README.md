[![Build Status](https://travis-ci.org/thaynarasilvapinto/SimuladorBanco.svg?branch=master)](https://travis-ci.org/thaynarasilvapinto/SimuladorBanco)
[![codecov](https://codecov.io/gh/thaynarasilvapinto/SimuladorBanco/branch/master/graph/badge.svg)](https://codecov.io/gh/thaynarasilvapinto/SimuladorBanco)

# Simulador de Operações Bancárias 
Um projeto de treinamento para preparar o(a) estagiário(a) para que o mesmo consiga desenvolver features reais. 

### Pré-requisitos
O que você precisa para instalar o software e instalá-lo

* [Docker](https://docs.docker.com/install/#backporting)
* [Docker-Compose](https://docs.docker.com/compose/install/#uninstallation) 
* [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=pt-BR)

Os links dos referidos softwares ensinam como deve ser feita a instalação em casa caso particular.

### Comandos auxiliares para o docker
Após o docker e do docker-compose instalados, é preciso entrar no diretorio do projeto, e executar os seguintes comandos.
O primeiro para startar o docker
```
systemctl start docker.service
```
Para iniciar os containers utiliza-se o comando,  [para mais informações sobre o comando](https://docs.docker.com/compose/reference/up/) 
```
docker-compose up
```
Para deletar o banco de dados é preciso executar o seguinte comando, é importante lembrar que para ele ser executado é preciso estar no diretorio do projeto.
```
docker-compose down
```
Para sair do docker-compose up, é só dar Ctrl+c

### Motivação
O projeto tem como objetivo treinar e desenvolver os seguintes tópicos.

* Maven 
* Git
* TDD
* Spring MVC
* Spring Boot 
* Mock MVC
* Kotlin
* Docker-Compose
* Postgresql
* Spring Data

### Desafio
###### Objetivo:
Criar um sistema que simule um Banco 
###### Descrição:
O sistema deve permitir cadastrar cliente/conta. Após o cliente cadastrado
deve ser possível realizar as seguintes operações:
* Depósito;
* Saque;
* Transferência;
* Saldo;
* Extrato.
###### Regras de négocio
* O saldo da conta nunca poderá ser negativo;
* Não pode ser possível realizar saque ou transferência quando o saldo na conta é
insuficiente;
* A conta de destino deve ser válida;
* O cliente só poderá ter uma conta (validar por CPF por exemplo);
* Ao criar a conta na resposta de sucesso deverá constar o Id da conta para futuras
movimentações;
* Ao solicitar um extrato, deverá constar toda movimentação da conta, como
transferência, depósito e saque;
* Ao solicitar transferência tanto a conta de destino quanto a de origem devem ser
válidas;
* Não pode ser possível realizar uma transferência para você mesmo, ou seja, conta
de origem não pode ser igual a conta de destino;
###### Requisitos técnicos
* Os serviços devem ser expostos via REST com as respostas em Json;
* O gerenciamento das dependências do projeto deve ser feita utilizando Maven;
* A linguagem utilizada deve ser JAVA/KOTLIN;
* O sistema deve ser inicializado usando Spring Boot;
* O projeto deverá estar "commitado" em seu GitHub;
* A cobertura de testes deve ser de no mínimo 60% (Utilizar o codecov no GitHub);
* Criar um docker compose para subir um Postgres;
* Criar o DER (Diagrama de entidade e relacionamento);
* Criar a persistência no banco Postgres;
* Separar em módulos (módulo para API, módulo para o Controller, módulo service,
módulo para o domain e um módulo para o repository);
* Migrar para Kotlin, caso o projeto tenha sido desenvolvido em JAVA;
* Colocar testes integrados para o Controller;
* Criação das collections no Postman;
* Criar o README utilizando Markdown com detalhes do projeto, DER, regras de
negócio e como inicializar e testar seu projeto;
* Refactor nos testes;