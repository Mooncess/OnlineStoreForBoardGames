FROM node:18-alpine
WORKDIR /react-front-deployment/
COPY public/ /react-front-deployment/public
COPY src/ /react-front-deployment/src
COPY package.json /react-front-deployment/
RUN npm install
CMD ["npm", "start"]