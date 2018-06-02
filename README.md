This application is part of a coding challenge.

to run locally:
    
    - $ sbt run 
    
to run on docker:

    - # generate the docker image locally.
    - $ sbt docker:publishLocal
    - $ docker run --name vaguecode-8080 -p 8080:9000 vaguecode:1.0.0
    - # where 1.0.0 is the version you are running.