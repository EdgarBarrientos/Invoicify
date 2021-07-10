# Invoicify
**team - Awesome**

1. Edgar Barrientos
2. Rangarajan Murugan
3. Sagubar Sathik
4. Segun Dapo
5. Sini Chacko
6. Peter Le

#
Invoicify is tool for contractors to record and bill companies for services.
Each invoice contains brief details about the products and services purchased. These products and services are commonly referred to simply as items on the invoice. Overall the software works like so:

1. A contractor creates an invoice when a company purchases a good or service.
2. They add items to the invoice for services rendered.
3. The invoice is sent to the companies for payment.
4. The contractor marks the invoice as paid when company pays the invoice.

#
This project will be using below features
1. Create REST APIs using Springboot,gradle,postgres DB,H2,Lombok
2. Junit will be used for Mock testing,Stub testing
3. Github actions for CI/CD
4. Github work flow
5. Heroku work flow
6. Restdocs/ Asciidoctor for API documentation

#
This project will run locally by doing the following:
1. docker build -t <image-name> .
2. docker-compose up 
3. If you want to test db connectivity to either H2 or Postgres profile, you can set the active profile in SPRING_PROFILES_ACTIVE.
4. Make sure that the Dockerfile includes the following (within Windows Environment):
  - RUN apt-get update && apt-get install -y dos2unix
  - RUN dos2unix gradlew
5. If the app runs in Heroku, add the following to Dockerfile:
  - RUN chmod +x ./gradlew
  


  
  
