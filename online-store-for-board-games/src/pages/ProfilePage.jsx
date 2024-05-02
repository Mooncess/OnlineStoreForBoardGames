import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { Navigate } from 'react-router-dom';

const ProfilePage = () => {
    const [profileData, setProfileData] = useState(null);
    const [redirectToLogin, setRedirectToLogin] = useState(false);

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/action/profile');
                
                if (response.status === 200) {
                    setProfileData(response.data);
                } else {
                    console.log('Ошибка при получении данных профиля');
                    if (response.status === 500) {
                        setRedirectToLogin(true);
                    }
                }
            } catch (error) {
                console.error('Ошибка при запросе данных профиля на сервер:', error);
            }
        };

        fetchProfileData();
    }, []);

    if (redirectToLogin) {
        return <Navigate to="/login" />;
    }

    return (
        <div>
            <h2>Профиль</h2>
            {profileData ? (
                <div>
                    <p>Имя: {profileData.firstName}</p>
                    <p>Фамилия: {profileData.lastName}</p>
                    <p>Персональная скидка: {profileData.personalDiscount}%</p>
                    <p>Номер телефона: {profileData.phoneNumber}</p>
                </div>
            ) : (
                <p>Загрузка данных профиля...</p>
            )}
        </div>
    );
};

export default ProfilePage;