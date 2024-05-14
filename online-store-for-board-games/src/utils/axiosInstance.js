import axios from 'axios';

const axiosInstance = axios.create({
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

axiosInstance.interceptors.response.use(
    response => {
        return response;
    },
    async error => {
        if (error.response && error.response.status === 401) {
            console.log('Ошибка 401: Пользователь не авторизован. Пожалуйста, выполните вход.');

            try {
                const response = await axios.post(`${process.env.REACT_APP_JWT_SERVER_URL}/api/auth/token`, null, {
                    withCredentials: true,
                });
                console.log("Обновил токен");
                console.log(response.status);

                // Повторяем исходный запрос с обновленным токеном
                return axios(error.config);
            } catch (accessError) {
                if (accessError.response && accessError.response.status === 500) {
                  try {
                    const response = await axios.post(`${process.env.REACT_APP_JWT_SERVER_URL}/api/auth/refresh`, null, {
                        withCredentials: true,
                    });
                    console.log("Обновил рефреш токен");
                    console.log(response.status);
    
                    // Повторяем исходный запрос с обновленным токеном
                    return axios(error.config);
                } catch (refreshError) {
                    window.location.href = '/login';
                    // Можно выполнить дополнительные действия при ошибке обновления токена
                    return Promise.reject(refreshError);
                }
                }
                // Можно выполнить дополнительные действия при ошибке обновления токена
                return Promise.reject(accessError);
            }
        } else if (error.response && error.response.status === 403) {
            return error.response;
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;