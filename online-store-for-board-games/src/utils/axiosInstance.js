import axios from 'axios';

const axiosInstance = axios.create({
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// axiosInstance.interceptors.request.use(
//   (config) => {
//     // Извлекаем JWT токен из Cookies
//     const cookies = document.cookie.split('; ');
//     const tokenCookie = cookies.find(cookie => cookie.startsWith('access='));
//     const token = tokenCookie ? tokenCookie.split('=')[1] : null;

//     // Если токен есть, добавляем его в cookies
//     if (token) {
//       config.headers.Authorization = `Bearer ${token}`;
//     }

//     return config;
//   },
//   (error) => {
//     return Promise.reject(error);
// });

// axiosInstance.interceptors.response.use(
//   response => {
//     if (response.status === 401) {
//       console.log("Словили 401");
//       const accessCookie = document.cookie
//         .split('; ')
//         .find(cookie => cookie.startsWith('access='))
//         .split('=')[1];
      
//       // Отправляем запрос на обновление токена
//       return axios.post('http://localhost:8099/api/auth/token', { token: accessCookie }, { withCredentials: true })
//         .then(res => {
//           // Обновляем значение `access` в cookie
//           document.cookie = `access=${res.data.accessToken}; Path=/; Max-Age=3600; HttpOnly`;

//           // Повторно отправляем исходный запрос с обновленным токеном
//           return axiosInstance(response.config);
//         })
//         .catch(err => {
//           throw err;
//         });
//     }

//     return response;
//   },
//   error => {
//     return Promise.reject(error);
//   }
// );

axiosInstance.interceptors.response.use(response => {
  return response;
}, async error => {
  if (error.response && error.response.status === 401) {
    console.log('Ошибка 401: Пользователь не авторизован. Пожалуйста, выполните вход.');

    try {
      const response = await axios.post('http://localhost:8099/api/auth/token', null, {
        withCredentials: true,
      });

      return axios(error.config);
    } catch (refreshError) {
      console.error('Ошибка при обновлении токена:', refreshError);
      // Можно выполнить дополнительные действия при ошибке обновления токена
      return Promise.reject(refreshError);
    }
  }

  return Promise.reject(error);
});

export default axiosInstance;