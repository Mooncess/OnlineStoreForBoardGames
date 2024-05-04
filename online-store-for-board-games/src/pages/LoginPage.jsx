import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/LoginPage.css';

const LoginPage = () => {
    const navigate = useNavigate();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        try {
            const response = await axiosInstance.post('http://localhost:8099/api/auth/login', { login, password }, { withCredentials: true });

            if (response.status === 200) {
                console.log("Успешный вход");
                navigate('/profile');
            } else {
                console.log("Что-то пошло не так");
                // Обработка ошибки входа
            }
        } catch (error) {
            console.error('Ошибка при запросе на сервер:', error);
        }
    };

    const handleRegistrationRedirect = () => {
        navigate('/registration');
    };

    return (
        <div>
            <MyNavbar />
            <div className="login-container">
                <h2>Вход</h2>
                <input type="login" placeholder="Email" value={login} onChange={(e) => setLogin(e.target.value)} />
                <input type="password" placeholder="Пароль" value={password} onChange={(e) => setPassword(e.target.value)} />
                <button onClick={handleLogin} className="black-button">Войти</button>
                <button onClick={handleRegistrationRedirect} className="black-button">Зарегистрироваться</button> {/* Кнопка для перехода на страницу регистрации */}
            </div>
            <MyFooter />
        </div>
    );
};

export default LoginPage;