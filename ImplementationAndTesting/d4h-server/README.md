# Data 4 Help

## Building

```sh
    mvn clean install
```

If you want to skip tests:

```sh
    mvn clean install -DskipTests
```

## Test Deploy
```sh
    docker-compose up --build
```
If it produces an error saying that the port is already allocated it is necessary to do the following commands:
1.. This command is useful to see which dockers are still running
```sh
   docker ps
    
```
2.. These command are necessary to stop and delete all dockers found with docker ps.
```sh
   docker container stop CONTAINER ID
   docker container rm CONTAINER ID 
   docker container prune
```
3.. Going back to step 1, no container must be found, it is now possible to user the deploy test command.
## Delete composition
```sh
    docker-compose rm 
```

## Delete volumes
```sh
    docker volume ls
    docker volume rm name volume
```