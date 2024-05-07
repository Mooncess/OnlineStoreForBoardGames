import axios from 'axios';

const axiosInstance = axios.create({
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosInstance.interceptors.response.use(response => {
  return response;
}, async error => {
  if (error.response && error.response.status === 401) {
    console.log('Ошибка 401: Пользователь не авторизован. Пожалуйста, выполните вход.');

    try {
      const response = await axios.post('http://localhost:8099/api/auth/token', null, {
        withCredentials: true,
      });
      console.log("Обновил токен");
      console.log(response.status);

      // Повторяем исходный запрос с обновленным токеном
      return axios(error.config);
    } catch (refreshError) {
      if (refreshError.response && refreshError.response.status === 500) {
        window.location.href = '/login';
      }
      // Можно выполнить дополнительные действия при ошибке обновления токена
      return Promise.reject(refreshError);
    }
  }
  else if (error.response && error.response.status === 403) {
    return error.response;
  }
  return Promise.reject(error);
});

export default axiosInstance;