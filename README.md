# Big Damas

**Big Damas** é a idealização de um projeto da materia de Sistemas Distribuidos. Trata-se de um clássico jogo de damas, com funcionalidade multiplayer e desenvolvido inteiramente em Java, onde as tradicionais peças pretas e brancas são substituidas por memes.

Este projeto foi desenvolvido como requisito acadêmico, tendo como objetivo principal a implementação e o estudo prático da sincronização de estados em uma arquitetura distribuída Cliente-Servidor.

### Objetivos do Projeto
* **Técnico:** Garantir a consistência de dados e a sincronização do tabuleiro entre duas máquinas distintas em tempo real, isolando as regras de negócio através de chamadas de métodos remotos.
* **Usuário:** Proporcionar uma experiência de jogo fluida, divertida e livre de dessincronização.

###  Tecnologias e Arquitetura
O sistema foi arquitetado de forma modular, dividindo responsabilidades claras para facilitar o desenvolvimento em equipe:
* **Linguagem Base:** Java.
* **Comunicação em Rede:** Java RMI.
* **Interface Gráfica:** Java Swing.
