import React, { useState } from 'react';

const RegistrationPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');

    const handleRegister = async () => {
        try {
            const response = await fetch('http://localhost:8099/api/registration', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password, firstName, lastName, phoneNumber }),
            });
            if (response.ok) {
                // Обработка успешной регистрации
            } else {
                // Обработка ошибки регистрации
            }
        } catch (error) {
            console.error('Ошибка при запросе на сервер:', error);
        }
    };

    return (
        <div>
            <h2>Регистрация</h2>
            <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
            <input type="password" placeholder="Пароль" value={password} onChange={(e) => setPassword(e.target.value)} />
            <input type="text" placeholder="Имя" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
            <input type="text" placeholder="Фамилия" value={lastName} onChange={(e) => setLastName(e.target.value)} />
            <input type="text" placeholder="Номер телефона" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} />
            <button onClick={handleRegister}>Зарегистрироваться</button>
        </div>
    );
};

export default RegistrationPage;