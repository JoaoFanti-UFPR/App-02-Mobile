# Documentação do Projeto: Filmes Nossol

## 1. Tema e Explicação do Aplicativo
O **Filmes Nossol** é um aplicativo Android voltado para o gerenciamento de uma biblioteca pessoal de filmes. Ele permite que o usuário cadastre filmes que já assistiu ou que ainda deseja assistir, armazenando informações essenciais como título, gênero, ano de lançamento, nota pessoal e status de acompanhamento. A proposta é oferecer uma interface simples e direta para criar e organizar listas de filmes favoritas e pendências cinematográficas.

## 2. Regras de Negócios e Ordenação Utilizadas
- **Validação de Campos:** É obrigatório o preenchimento de todos os dados do filme (título, gênero, ano e nota) para realizar o salvamento.
- **Validação da Nota:** A nota atribuída ao filme deve estar obrigatoriamente dentro de um intervalo de **0 a 5**.
- **Validação de Ano:** O ano de lançamento do filme não pode ser inferior a **1888** (ano histórico considerado o marco inicial do cinema).
- **Cálculo de Estatísticas:** A tela inicial recalcula dinamicamente a quantidade total de filmes exibidos na lista atual e a sua nota média baseada apenas nos resultados filtrados na tela.
- **Ordenação Padrão:** Por padrão, a lista de filmes é apresentada ordenada **alfabeticamente por título** (`Título ASC`), facilitando a localização visual pelo usuário.

## 3. Filtros de Dados Disponíveis
A tela inicial (`MainActivity`) disponibiliza várias maneiras para buscar e segmentar o acervo de filmes cadastrados:
- **Busca por Texto / Título:** Existe uma barra de busca que permite procurar por letras ou palavras presentes no título do filme (consulta literal via cláusula `LIKE`).
- **Filtro de Status:**
  - **Todos:** Remove todos os filtros e lista a base completa.
  - **Assistidos:** Exibe apenas os filmes marcados com o status de que o usuário já consumiu (`Assistido`).
  - **Para Assistir:** Exibe exclusivamente os filmes marcados na lista de desejos (`Desejado` / `Para Assistir`).

## 4. Definição da Estrutura do Projeto (Android Studio)
O projeto segue o padrão arquitetural clássico MVC (Model-View-Controller) simplificado nativo do Android:
- **`model/`**: Pacote que contém as classes de entidades de domínio do negócio, como `Filme.kt`, definindo a estrutura dos dados trabalhados na aplicação.
- **`db/`**: Abriga a classe de infraestrutura `DBHelper.kt`, responsável na criação e versionamento do banco de dados (SQLite).
- **`dao/`**: Data Access Object. Contém `FilmeDAO.kt`, concentrando as operações de CRUD do banco (Inserir, Atualizar, Excluir, Listar).
- **`adapter/`**: Contém `FilmeAdapter.kt`, necessário para interligar os dados da lista manipulada (modelo) com a interface gráfica (`ListView`).
- **`Raiz Principal`**: Onde residem as Controllers/Activities como `MainActivity.kt` (Tela inicial) e `AddEditActivity.kt` (Tela de formulários).
- **`res/`**: Diretório principal de recursos que engloba:
  - `layout/`: Telas XML (`activity_main.xml`, `activity_add_edit.xml`, e listagem `item_movie.xml`).
  - `values/`: Diretrizes de design system constantes (cores, textos de interface `strings.xml`, e temas).

## 5. Telas Desenvolvidas
1. **MainActivity (Tela Principal):** Atua como o painel principal. Exibe o resumo do acervo (Total e Média de Nota), a barra de pesquisas, os botões de filtro rápido e a ListView (Lista rolável) mostrando todos os filmes organizados. Contém o "Floating Action Button (FAB)" para novas inclusões.
2. **AddEditActivity (Tela de Cadastro / Edição):** Contém os formulários (`TextInputEditText`) e `RadioGroups` de interação do usuário para salvar novas referências. Seu comportamento difere caso receba um filme já existente (modo de Edição e permite a exclusão) ou venha vazia (novo cadastro).

## 6. Fluxo de Navegação e Ações
* **Início:** O usuário abre o app, caindo diretamente na `MainActivity`. Os dados salvas em banco local são carregados via `onResume()`. Se o banco for novo, dados iniciais (mock) são injetados automaticamente.
* **Nova Adição:** 
  - Ao clicar no Float Action Button `(+)`, é chamado um *Intent* com destino a `AddEditActivity`.
  - Usuário preenche os campos -> clica em *Salvar* -> App faz validação -> insere no Banco de Dados -> fecha tela e volta ativando atualização na listagem.
* **Leitura/Busca:**
  - Na tela inicial, digitar na barra superior e clicar no botão buscar irá recarregar o *Adapter* apresentando apenas resultados contendo os termos. 
  - Apertar em *Assistidos* ou *Para assistir* irá substituir imediatamente os itens e a métrica de total exibida.
* **Edição / Remoção:**
  - Tocar encima de uma das linhas de filmes já cadastrados (na tela Principal).
  - O app passa o objeto `Filme` pela intent até a tela `AddEditActivity`. 
  - Os formulários preenchem-se automaticamente.
  - Ao modificar e *Salvar*, os dados são atualizados ("Update" na base local).
  - Ao tocar no botão de *Excluir* (habilitado dinamicamente no modo edição interativo), ocorre o _delete_ permanente, a Activity finalizará (`finish()`) e o aplicativo retornará para `MainActivity`, sem o filme na lista.