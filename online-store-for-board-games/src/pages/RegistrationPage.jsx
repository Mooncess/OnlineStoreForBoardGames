import React, { useState } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { useNavigate, Link } from 'react-router-dom';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/RegistrationPage.css';

const RegistrationPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();

    const handleRegister = async () => {
        if (!username || !password || !firstName || !lastName || !phoneNumber) {
            setError('Пожалуйста, заполните все поля');
            return;
        }

        if (!username.includes('@') || !username.includes('.')) {
            setError('Некорректный email. Пожалуйста, введите действительный email адрес');
            return;
        }

        try {
            const response = await axiosInstance.post(`${process.env.REACT_APP_JWT_SERVER_URL}/api/registration`, { username, password, firstName, lastName, phoneNumber });

            if (response.status === 200) {
                console.log("Регистрация успешна");
                navigate('/login');
            } else {
                console.log("Что-то пошло не так");
                // Обработка ошибки регистрации
            }
        } catch (error) {
            if (error.response.status === 400) {
                setError('Пользователь с таким email уже существует');
            }
        }
    };

    return (
        <div>
            <MyNavbar />
            <div className="registration-container">
                <h2>Регистрация</h2>
                <input required type="username" placeholder="Email" value={username} className="reg-input" onChange={(e) => setUsername(e.target.value)} />
                <input required type="password" placeholder="Пароль" value={password} className="reg-input" onChange={(e) => setPassword(e.target.value)} />
                <input required type="text" placeholder="Имя" value={firstName} className="reg-input" onChange={(e) => setFirstName(e.target.value)} />
                <input required type="text" placeholder="Фамилия" value={lastName} className="reg-input" onChange={(e) => setLastName(e.target.value)} />
                <input required type="text" placeholder="Номер телефона" value={phoneNumber} className="reg-input" onChange={(e) => setPhoneNumber(e.target.value)} />

                {error && <p style={{ color: 'red' }}>{error}</p>}

                <button onClick={handleRegister} className="black-button">Зарегистрироваться</button>
                <Link to="/login" className="black-button">Уже есть аккаунт</Link>
            </div>
            <MyFooter />
        </div>
    );
};

export default RegistrationPage;