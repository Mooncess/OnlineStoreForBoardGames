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
    const [error, setError] = useState('');

    const handleLogin = async () => {
        try {
            const response = await axiosInstance.post('http://localhost:8099/api/auth/login', { login, password }, { withCredentials: true });

            if (response.status === 200) {
                console.log("Успешный вход");
                navigate('/profile');
            } else {
                console.log("Что-то пошло не так");
            }
        } catch (error) {
            console.error('Ошибка при запросе на сервер:', error);
            if (error.response.status === 500) {
                setError('Неверный email или пароль');
            }
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
                <input required type="login" placeholder="Email" value={login} className="log-input" onChange={(e) => setLogin(e.target.value)} />
                <input required type="password" placeholder="Пароль" value={password} className="log-input" onChange={(e) => setPassword(e.target.value)} />
                
                {error && <p style={{ color: 'red' }}>{error}</p>}

                <button onClick={handleLogin} className="black-button">Войти</button>
                <button onClick={handleRegistrationRedirect} className="black-button">Зарегистрироваться</button> {/* Кнопка для перехода на страницу регистрации */}
            </div>
            <MyFooter />
        </div>
    );
};

export default LoginPage;