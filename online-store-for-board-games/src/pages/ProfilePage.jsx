import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { Navigate, Link } from 'react-router-dom';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import { useNavigate } from 'react-router-dom';
import '../styles/ProfilePage.css'; // Импорт стилей

const ProfilePage = () => {
    const [profileData, setProfileData] = useState(null);
    const [redirectToLogin, setRedirectToLogin] = useState(false);
    const [redirectToAdminPanel, setRedirectToAdminPanel] = useState(false);
    const [userOrders, setUserOrders] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/action/profile');

                if (response.status === 200) {
                    setProfileData(response.data);
                } else if (response.status === 403) {
                    console.log("Перенаправили на админ-панель УСТ");
                    setRedirectToAdminPanel(true);
                } else if (response.status === 500) {
                    setRedirectToLogin(true);
                }
            } catch (error) {
                console.error('Ошибка при запросе данных профиля на сервер:', error);
                if (error.response && error.response.status === 403) {
                    console.log("Перенаправили на админ-панель УСТ");
                    setRedirectToAdminPanel(true);
                }
                else {
                    setRedirectToLogin(true);
                }
            }
        };

        const fetchUserOrders = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/action/get-user-order');
                setUserOrders(response.data);
            } catch (error) {
                console.error('Ошибка при получении заказов пользователя:', error);
            }
        };

        fetchProfileData();
        fetchUserOrders();
    }, []);

    const handleLogout = async () => {
        try {
            const response = await axiosInstance.get('http://localhost:8099/api/auth/logout', { withCredentials: true });
            if (response.status === 204) {
                console.log("Успешный выход");
                navigate('/test');
            } else {
                console.log("Что-то пошло не так");
            }
        } catch (error) {
            console.error('Ошибка при запросе на сервер:', error);
        }
    };

    if (redirectToLogin) {
        return <Navigate to="/login" />;
    }

    if (redirectToAdminPanel) {
        console.log("Перенаправили на админ-панель");
        return <Navigate to="/admin/admin-panel" />;
    }

    return (
        <div>
            <MyNavbar />
            <div className='main-content'>
            {profileData && (
                <div className="profile-details">
                    <h1>Профиль</h1>
                    <div className="profile-container">
                        <div className="profile-column">
                            <p className="profile-name">Имя: {profileData.firstName}</p>
                            <p className="profile-surname">Фамилия: {profileData.lastName}</p>
                        </div>
                        <div className="profile-column">
                            <p className="profile-phone">Номер телефона: {profileData.phoneNumber}</p>
                            <p className="profile-discount">Персональная скидка: {profileData.personalDiscount}%</p>
                        </div>
                    </div>
                    <button onClick={() => navigate('/profile/comments')} className="black-button">Мои отзывы</button>
                    <button onClick={handleLogout} className="black-button">Выход</button>
                </div>
            )}
            
            {Array.isArray(userOrders) && (
                <div className="profile-orders">
                    <h2>Мои заказы</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Номер заказа</th>
                                <th>Адрес</th>
                                <th>Дата заказа</th>
                                <th>Статус</th>
                            </tr>
                        </thead>
                        <tbody>
                            {userOrders.map(order => (
                                <tr key={order.id}>
                                    <td className='order-link'>
                                        <Link to={`/profile/order/${order.id}`}>{order.orderNumber}</Link>
                                    </td>
                                    <td>{order.address}</td>
                                    <td>{order.orderDate}</td>
                                    <td>{order.status.name}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
            </div>
            <MyFooter />
        </div>
    );
};

export default ProfilePage;