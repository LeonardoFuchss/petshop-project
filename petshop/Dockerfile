
# Usa a imagem do OpenJDK 22 como base
FROM openjdk:22-jdk
# Cria um diretório chamado 'petshop' dentro do container
RUN mkdir /petshop
# Define o diretório de trabalho
WORKDIR /petshop
# Copia o arquivo JAR gerado do projeto para dentro do container
COPY target/*.jar /petshop/petshop.jar
# Comando para rodar a aplicação
CMD ["java", "-jar", "/petshop/petshop.jar"]

# rodar no terminal: docker build -t aplicacao-spring
# para criar a imagem.