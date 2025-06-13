sudo docker stop eduflow
sudo docker run --name eduflow -e POSTGRES_USER=eduflow -e POSTGRES_PASSWORD=eduflow -e POSTGRES_DB=eduflow -p 5432:5432 -d --rm postgres:12.22
./gradlew clean build
./gradlew --no-daemon run 


Usuarios:
admin/admin
demo/demo