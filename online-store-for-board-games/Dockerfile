FROM node:18-alpine
WORKDIR /react-front-deployment/
COPY online-store-for-board-games/public/ /react-front-deployment/public
COPY online-store-for-board-games/src/ /react-front-deployment/src
COPY online-store-for-board-games/package.json /react-front-deployment/
RUN npm install
CMD ["npm", "start"]