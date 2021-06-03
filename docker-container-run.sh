sudo docker run --name mariadb -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=qlsl1440 --network ddng-network mariadb -v ddng-volume:/ddng

sudo docker run -d --name eureka-server -p 18761:18761 --network ddng-network -v ddng-volume:/ddng chiwoo2074/eureka-server
sudo docker run -d --name customer-api -p 18005:18005 --network ddng-network -v ddng-volume:/ddng chiwoo2074/customer-api
sudo docker run -d --name schedule-api -p 18020:18020 --network ddng-network -v ddng-volume:/ddng chiwoo2074/schedule-api
sudo docker run -d --name sale-api -p 18015:18015 --network ddng-network -v ddng-volume:/ddng chiwoo2074/sale-api
sudo docker run -d --name utils-api -p 18010:18010 --network ddng-network -v ddng-volume:/ddng chiwoo2074/utils-api
sudo docker run -d --name gate-way -p 18090:18090 --network ddng-network -v ddng-volume:/ddng chiwoo2074/gate-way
sudo docker run -d --name admin-ui-bootstrap -p 8080:8080 --network ddng-network -v ddng-volume:/ddng chiwoo2074/admin-ui-bootstrap
sudo docker run -d --name user-ui -p 80:18080 --network ddng-network -v ddng-volume:/ddng chiwoo2074/user-ui