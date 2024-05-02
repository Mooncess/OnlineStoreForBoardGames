import React from 'react';
import { Link } from 'react-router-dom';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';

const ArtilceManagementPage = () => {
    return (
        <div>
            <MyNavbar />
            <div className="admin-links">
                <Link to="/article-management">Управление статьями</Link>
                <Link to="/category-management">Управление категориями</Link>
                <Link to="/order-management">Управление заказами</Link>
                <Link to="/user-management">Управление пользователями</Link>
            </div>
            <MyFooter />
        </div>
    );
};

export default ArtilceManagementPage;