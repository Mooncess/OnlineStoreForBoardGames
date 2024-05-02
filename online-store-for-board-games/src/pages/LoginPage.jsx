import React, { useState } from 'react';
import axiosInstance from '../utils/axiosInstance';

const LoginPage = () => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        try {
            const response = await axiosInstance.post('http://localhost:8099/api/auth/login', { login, password }, { withCredentials: true });
            
            if (response.status === 200) {
                console.log("Успешный вход");
                
                // Перенаправляем на страницу профиля или куда вам нужно
            } else {
                console.log("Что-то пошло не так");
                // Обработка ошибки входа
            }
        } catch (error) {
            console.error('Ошибка при запросе на сервер:', error);
        }
    };

    return (
        <div>
            <h2>Вход</h2>
            <input type="login" placeholder="Email" value={login} onChange={(e) => setLogin(e.target.value)} />
            <input type="password" placeholder="Пароль" value={password} onChange={(e) => setPassword(e.target.value)} />
            <button onClick={handleLogin}>Войти</button>
        </div>
    );
};

export default LoginPage;