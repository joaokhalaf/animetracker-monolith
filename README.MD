# 🎌 AniTracker

Uma aplicação web para rastrear, organizar e descobrir seus animes favoritos.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green?style=flat-square&logo=spring)
![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-3.4-blue?style=flat-square&logo=tailwindcss)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)

## 📖 Sobre o Projeto

O **AniTracker** é uma aplicação web completa que permite aos usuários descobrir novos animes, gerenciar seus favoritos e criar listas personalizadas. A aplicação consome dados da [Jikan API](https://jikan.moe/) (API não oficial do MyAnimeList) para fornecer informações atualizadas sobre animes.

## Funcionalidades

### Busca e Descoberta
- **Pesquisa de animes** por nome com resultados ordenados por avaliação
- **Animes da temporada atual** - descubra os lançamentos mais recentes
- **Top animes** - veja os animes mais bem avaliados de todos os tempos
- **Animes populares** - explore os títulos mais populares da comunidade

### Sistema de Favoritos
- Adicione animes aos seus favoritos com um clique
- Visualize todos os seus animes favoritos em uma página dedicada
- Remova animes dos favoritos facilmente

### Listas Personalizadas
- **Crie listas customizadas** para organizar seus animes (ex: "Para assistir", "Assistindo", "Melhores de 2024")
- **Adicione descrições** às suas listas
- **Gerencie animes** em cada lista - adicione ou remova conforme desejar
- **Edite e exclua** listas quando necessário

### Autenticação de Usuários
- Registro de novos usuários
- Login seguro com Spring Security
- Dados de favoritos e listas vinculados ao perfil do usuário

### Interface Responsiva
- Design moderno e responsivo com Tailwind CSS
- Navegação intuitiva e experiência fluida em desktop e mobile

## Tecnologias

### Backend
| Tecnologia | Descrição |
|------------|-----------|
| **Java 21** | Linguagem de programação principal |
| **Spring Boot 3.5** | Framework para desenvolvimento da aplicação |
| **Spring Security** | Autenticação e autorização de usuários |
| **Spring Data JPA** | Persistência de dados e ORM |
| **Spring WebFlux** | Cliente HTTP reativo para consumo de APIs externas |
| **Thymeleaf** | Template engine para renderização de páginas HTML |
| **Hibernate Validator** | Validação de dados |
| **PostgreSQL** | Banco de dados relacional |

### Frontend
| Tecnologia | Descrição |
|------------|-----------|
| **Tailwind CSS** | Framework CSS utilitário para estilização |
| **Bootstrap Icons** | Biblioteca de ícones |
| **JavaScript** | Interatividade e requisições assíncronas |

### Infraestrutura
| Tecnologia | Descrição |
|------------|-----------|
| **Docker** | Containerização da aplicação |
| **Maven** | Gerenciamento de dependências e build |

### APIs Externas
| API | Descrição |
|-----|-----------|
| **Jikan API** | API não oficial do MyAnimeList para dados de animes |

## Estrutura do Projeto

```
src/main/java/br/com/animetracker/AniTracker/
├── api/jikan/           # DTOs para integração com Jikan API
├── config/              # Configurações (Security, OpenAPI, WebClient)
├── controller/          # Controllers da aplicação
├── model/               # Entidades JPA
├── repository/          # Repositórios Spring Data
├── security/            # Classes de segurança customizadas
└── service/             # Lógica de negócio

src/main/resources/
├── static/
│   ├── css/             # Arquivos CSS (Tailwind)
│   ├── js/              # Scripts JavaScript
│   └── images/          # Imagens estáticas
└── templates/           # Templates Thymeleaf
```

## Páginas da Aplicação

| Página | Descrição |
|--------|-----------|
| **Home** | Página inicial com busca, animes da temporada e top animes |
| **Resultados** | Lista de animes encontrados na pesquisa |
| **Favoritos** | Lista de animes favoritos do usuário |
| **Minhas Listas** | Gerenciamento de listas personalizadas |
| **Detalhes da Lista** | Visualização dos animes em uma lista específica |
| **Login/Registro** | Autenticação de usuários |

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.
