import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { Navigate, Link } from 'react-router-dom';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import { useNavigate } from 'react-router-dom';

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
                if (error.response.status === 403) {
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
                console.error('Error fetching user orders:', error);
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
                navigate('/test'); // перенаправляем на главную страницу после выхода
            } else {
                console.log("Что-то пошло не так");
                // Обработка ошибки выхода
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
            {profileData && (
                <div>
                    <h2>Профиль</h2>
                    <p>Имя: {profileData.firstName}</p>
                    <p>Фамилия: {profileData.lastName}</p>
                    <p>Персональная скидка: {profileData.personalDiscount}%</p>
                    <p>Номер телефона: {profileData.phoneNumber}</p>
                    <button onClick={() => navigate('/profile/comments')}>Мои отзывы</button>
                    <button onClick={handleLogout}>Выход</button>
                </div>
            )}

            <h2>Мои заказы</h2>
            {Array.isArray(userOrders) && (
                <table>
                    <thead>
                        <tr>
                            <th>Order Number</th>
                            <th>Address</th>
                            <th>Order Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userOrders.map(order => (
                            <tr key={order.id}>
                                <td>
                                    <Link to={`/profile/order/${order.id}`}>{order.orderNumber}</Link>
                                </td>
                                <td>{order.address}</td>
                                <td>{order.orderDate}</td>
                                <td>{order.status.name}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
            <MyFooter />
        </div>
    );
};

export default ProfilePage;