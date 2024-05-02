import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { Navigate } from 'react-router-dom';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';

const ProfilePage = () => {
    const [profileData, setProfileData] = useState(null);
    const [redirectToLogin, setRedirectToLogin] = useState(false);
    const [redirectToAdminPanel, setRedirectToAdminPanel] = useState(false);

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/action/profile');

                if (response.status === 200) {
                    setProfileData(response.data);
                } else if (response.status === 403) {
                    setRedirectToAdminPanel(true);
                } else if (response.status === 500) {
                    setRedirectToLogin(true);
                }
            } catch (error) {
                console.error('Ошибка при запросе данных профиля на сервер:', error);
                setRedirectToLogin(true);
            }
        };

        fetchProfileData();
    }, []);

    if (redirectToLogin) {
        return <Navigate to="/login" replace />;
    }

    if (redirectToAdminPanel) {
        return <Navigate to="/admin/admin-panel" replace />;
    }

    return (
        <div>
            <MyNavbar />
            {profileData && (
                <div>
                    <h2>Профиль</h2>
                    <p>Имя: {profileData.firstName}</p>
                    <p>Фамилия: {profileData.lastName}</p>
                    <p>Персональная скидка: {profileData.personalDiscount}%</p>
                    <p>Номер телефона: {profileData.phoneNumber}</p>
                </div>
            )}
            <MyFooter />
        </div>
    );
};

export default ProfilePage;