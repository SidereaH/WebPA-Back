git clone https://github.com/realSlimPudge/PriceGuru_Frontend
git clone https://github.com/SidereaH/WebPA-Back

cd PriceGuru_Frontend
docker build -t priceguru-frontend .
cd ..

cd WebPA-Back
docker build -t priceguru-backend .
cd ..
# Запуск контейнеров
docker run -p 3000:80 priceguru-frontend
docker run -p 8080:8080 priceguru-backend
