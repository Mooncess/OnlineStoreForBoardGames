import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/WishListPage.css';

const WishListPage = () => {
    const [wishListItems, setWishListItems] = useState([]);

    useEffect(() => {
        const fetchWishListData = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/action/get-user-wishlist');
                setWishListItems(response.data);
            } catch (error) {
                console.error('Ошибка при запросе данных списка желаний:', error);
            }
        };

        fetchWishListData();
    }, []);

    const addToBasket = async (itemId) => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/add-to-basket?articleId=${itemId}`);
            alert('Товар добавлен в корзину!');
        } catch (error) {
            console.error('Ошибка при добавлении товара в корзину:', error);
        }
    };

    const removeFromWishList = async (itemId) => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/remove-from-wishlist?articleId=${itemId}`);
            setWishListItems(wishListItems.filter(item => item.id !== itemId));
            alert('Товар удален из списка желаний!');
        } catch (error) {
            console.error('Ошибка при удалении товара из списка желаний:', error);
        }
    };

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <h2>Список желаний</h2>
                {wishListItems.map(item => (
                    <div key={item.id} className="wishlist-item">
                        <h3>{item.name}</h3>
                        <p>Старая цена: {item.oldPrice}</p>
                        <p>Новая цена: {item.actualPrice}</p>
                        <button onClick={() => addToBasket(item.id)}>В корзину</button>
                        <button onClick={() => removeFromWishList(item.id)}>Удалить</button>
                    </div>
                ))}
            </div>
            <MyFooter />
        </div>
    );
};

export default WishListPage;