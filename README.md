# Nome do Projeto: Projeto Consisa (ou o nome real do seu projeto)

Este repositório contém o código-fonte da aplicação Java (Backend) desenvolvida com [Jakarta EE/Spring - especifique o que você usa] para o Projeto Consisa, e instruções detalhadas para configurar e executar a aplicação em um ambiente de desenvolvimento local (sem Docker) e também via Docker.

## Pré-requisitos

Para configurar e executar este projeto, você precisará dos seguintes softwares instalados em sua máquina:

* **Java Development Kit (JDK):** Versão 8 (ou a versão que sua aplicação usa).
    * Verifique sua versão com: `java -version`
* **Apache Maven:** Versão 3.6.0 ou superior (ou Gradle, se você usa Gradle).
    * Verifique sua versão com: `mvn --version`
* **WildFly Application Server:** Versão **Full 26.0.0.Final**.
    * Baixe-o de: [https://wildfly.org/downloads/](https://wildfly.org/downloads/)
* **Git:** Para clonar este repositório.
* **IDE (Ambiente de Desenvolvimento Integrado):** IntelliJ IDEA (Community ou Ultimate) ou Eclipse IDE for Enterprise Java and Web Developers são recomendados para depuração e desenvolvimento.
* **Docker Desktop (Opcional - para execução via contêiner):** Se você pretende usar a abordagem Docker.

## 1. Configuração do Ambiente Local (Sem Docker - para Desenvolvimento e Depuração)

Siga estes passos para configurar e executar a aplicação diretamente em sua máquina, sem o uso de contêineres Docker. Esta é a abordagem recomendada para depuração.

### 1.1. Clonar o Repositório

Abra um terminal e clone este repositório para o seu computador:

```bash
git clone [https://github.com/seu-usuario/seu-projeto.git](https://github.com/seu-usuario/seu-projeto.git)
cd seu-projeto
