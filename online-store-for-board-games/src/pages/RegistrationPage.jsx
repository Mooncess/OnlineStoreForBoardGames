import React, { useState } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { useNavigate } from 'react-router-dom';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/RegistrationPage.css';

const RegistrationPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    
    const navigate = useNavigate();

    const handleRegister = async () => {
        try {
            const response = await axiosInstance.post('http://localhost:8099/api/registration', { username, password, firstName, lastName, phoneNumber });

            if (response.status === 200) {
                console.log("Регистрация успешна");
                navigate('/profile');
            } else {
                console.log("Что-то пошло не так");
                // Обработка ошибки регистрации
            }
        } catch (error) {
            console.error('Ошибка при запросе на сервер:', error);
        }
    };

    return (
        <div>
        <MyNavbar />
        <div className="registration-container">
            <h2>Регистрация</h2>
            <input type="username" placeholder="Email" value={username} onChange={(e) => setUsername(e.target.value)} />
            <input type="password" placeholder="Пароль" value={password} onChange={(e) => setPassword(e.target.value)} />
            <input type="text" placeholder="Имя" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
            <input type="text" placeholder="Фамилия" value={lastName} onChange={(e) => setLastName(e.target.value)} />
            <input type="text" placeholder="Номер телефона" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} />
            <button onClick={handleRegister} className="black-button">Зарегистрироваться</button>
        </div>
        <MyFooter />
        </div>
    );
};

export default RegistrationPage;