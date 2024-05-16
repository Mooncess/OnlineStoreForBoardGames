import React from 'react';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/AdminPanelPage.css';

const AdminPanelPage = () => {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const response = await axiosInstance.get(`${process.env.REACT_APP_JWT_SERVER_URL}/api/auth/logout`, { withCredentials: true });
            if (response.status === 204) {
                console.log("Успешный выход");
                localStorage.removeItem('access');
                navigate('/'); // перенаправляем на главную страницу после выхода
            } else {
                console.log("Что-то пошло не так");
                // Обработка ошибки выхода
            }
        } catch (error) {
            console.error('Ошибка при запросе на сервер:', error);
        }
    };
    
    return (
        <div>
            <MyNavbar />
            <div className='main-content'>
            <div className="admin-links">
                <Link to="/admin/article-management">Управление товарами</Link>
                <Link to="/admin/category-management">Управление категориями</Link>
                <Link to="/admin/order-management">Управление заказами</Link>
                <Link to="/admin/comment-management">Управление отзывами</Link>
                <Link to="/admin/user-management">Управление пользователями</Link>
                <a href="#" onClick={handleLogout}>Выход</a> {/* Ссылка для выхода */}
            </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default AdminPanelPage;