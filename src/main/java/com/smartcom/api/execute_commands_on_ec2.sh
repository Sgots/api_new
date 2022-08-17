kill -9 $(lsof -t -i:8080)
echo "Killed process running on port 8080"

sudo systemctl restart smartcomAPI
echo "Started server using java -jar command"