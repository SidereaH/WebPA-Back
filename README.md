# Демонстрация

https://github.com/user-attachments/assets/f9d054ea-4a8b-4a04-926c-f16304d46222
# Запуск приложения
```
git clone https://github.com/realSlimPudge/PriceGuru_Frontend
git clone https://github.com/SidereaH/WebPA-Back
cd PriceGuru_Frontend
docker build -t priceguru-frontend .
docker run -p 3000:80 -d priceguru-frontend 
cd ..
cd WebPA-Back
git checkout reworkerproj
docker build -t priceguru-backend .
docker run -p 8080:8080 -d priceguru-backend 
```
http://localhost:3000/ - результат

[Связанные события](https://github.com/SidereaH/Portfolio/tree/main/Autumn%20Hackathon%20(DSTU)%2026-27.10.2024)
[Frontend](https://github.com/realSlimPudge/PriceGuru_Frontend)
