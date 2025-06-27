# Desafio - Mercado Livre

Este é um aplicativo Android nativo desenvolvido como parte de um desafio técnico, simulando a experiência de busca de produtos do Mercado Livre. O aplicativo permite que o usuário pesquise por produtos, visualize uma lista de resultados e veja os detalhes de um item específico.

## 📱 Telas do Aplicativo
O aplicativo é composto por três telas principais:

1.  **Tela de Busca:** Interface inicial onde o usuário pode digitar um termo de busca ou selecionar sugestões. 
2.  **Tela de Resultados:** Exibe uma lista de produtos encontrados, com informações essenciais como imagem, título, preço, parcelamento e frete.
3.  **Tela de Detalhes:** Mostra informações completas de um produto selecionado, incluindo um carrossel de imagens, descrição detalhada e atributos técnicos.

## 🛠️ Tecnologias e Arquitetura
Este projeto foi construído seguindo as melhores e mais modernas práticas de desenvolvimento Android recomendadas pelo Google.

* **Linguagem:** Kotlin
* **Interface de Usuário (UI):** Jetpack Compose
* **Arquitetura:** MVVM 
* **Injeção de Dependência:** Hilt
* **Navegação:** Jetpack Navigation for Compose
* **Carregamento de Imagens:** Coil
* **Parsing de JSON:** Gson
  
## 🧪 Testes
O projeto inclui testes unitários para a camada de lógica (`ViewModel`) e a camada de dados (`Repository`), utilizando:
* **MockK:** Biblioteca para criar mocks (dublês) e simular dependências.
