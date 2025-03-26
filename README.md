# MS Event Manager API 🎟️

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-green?logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-success?logo=mongodb)
![AWS](https://img.shields.io/badge/AWS-EC2-orange?logo=amazon-aws)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-orange?logo=swagger)

Microsserviço para gestão completa de eventos com integração em tempo real com serviços externos e validação de ingressos.

## 📋 Visão Geral
Solução Spring Boot para gestão de eventos que oferece:
- Integração com API ViaCEP para dados de endereço
- Comunicação com microsserviço de ingressos (ms-ticket-manager)
- Documentação API automática com Swagger UI
- Configuração pronta para deployment em AWS EC2

## 🚀 Funcionalidades Principais
- **Criação de Eventos**
  - Validação de CEP em tempo real via ViaCEP
  - Mapeamento automático de endereço
- **Gestão Inteligente**
  - Verificação de ingressos vendidos antes de exclusão
  - Ordenação de eventos por nome

## Acesso
- **URL**
  - http://34.229.179.29:8080
- **Documentação com Swagger**
  - http://34.229.179.29:8080/swagger-ui/index.html
# MS Ticket Manager API 🎫

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-green?logo=spring)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-success?logo=mongodb)
![AWS](https://img.shields.io/badge/AWS-EC2-orange?logo=amazon-aws)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-orange?logo=swagger)

Microsserviço especializado na gestão de ingressos com integração em tempo real com o MS Event Manager.

## 📋 Visão Geral
Solução Spring Boot para gestão de ingressos que oferece:
- Verificação de existência de ingressos por evento
- Integração com MS Event Manager para validação de eventos
- Soft-delete de ingressos
- Documentação API automática com Swagger UI

## 🚀 Funcionalidades Principais
- **Emissão de Ingressos**
  - Validação de evento existente
  - Status de transação (concluído/cancelado)
- **Gestão Flexível**
  - Atualização de dados do comprador
  - Cancelamento não-destrutivo(soft delete)
  - Verificação de vínculos com eventos

## Acesso
- **URL**
  - http://184.72.148.217:8081
- **Documentação com Swagger**
  - http://184.72.148.217:8081/swagger-ui/index.html
