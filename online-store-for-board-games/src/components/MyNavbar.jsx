// MyNavbar.js
import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/MyNavbar.css';

const MyNavbar = () => {
    return (
        <nav className="navbar">
            <ul>
                <li><Link to="/">Каталог</Link></li>
                <li><Link to="/profile">Профиль</Link></li>
                <li><Link to="/wish-list">Избранное</Link></li>
                <li><Link to="/basket">Корзина</Link></li>
            </ul>
        </nav>
    );
};

export default MyNavbar;