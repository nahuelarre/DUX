# DUX

Instrucciones Swagger: 

1_ Primero se debe usar el endpoint /auth/login para conseguir el token de autenticación. username: test, password: 12345
2_ Luego se debe usar ese token en "Authorize" (Arriba a la derecha del endpoint /auth/login) para poder
utilizar el resto de los endpoints
3_ Ya se pueden probar el resto de los endpoints

Instrucciones Dockerfile: 

En caso de usar dockerfile se puede clonar el dockerfile desde docker con el siguiente comando:

docker pull nahuelarre/dux-app

y luego se levanta con este comando:

docker run -p 8080:8080 nahuelarre/dux-app