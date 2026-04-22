# runs server

# export COLLECTION=huge_collection.json

export DB_URL="jdbc:postgresql://localhost:5432/lab7"
export DB_USER="danb"
export DB_PASSWORD="12345"

java -jar server/target/server-1.0-SNAPSHOT.jar