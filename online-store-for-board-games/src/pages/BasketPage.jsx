import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/BasketPage.css';

const BasketPage = () => {
    const [basketItems, setBasketItems] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const checkAdminStatus = async () => {
            try {
                const response = await axiosInstance.get(`${process.env.REACT_APP_JWT_SERVER_URL}/api/auth/is-admin`, { withCredentials: true });
                if (response.status === 200) {
                    navigate('/admin/admin-panel');
                }
            } catch (error) {
                console.error('Ошибка при проверке статуса администратора:', error);
            }
        };

        checkAdminStatus();

        const fetchBasketData = async () => {
            try {
                const response = await axiosInstance.get(`${process.env.REACT_APP_APP_SERVER_URL}/action/get-user-basket`);
                setBasketItems(response.data);
            } catch (error) {
                console.error('Ошибка при запросе данных корзины:', error);
            }
        };

        fetchBasketData();
    }, []);

    // Весь остальной код остается неизменным

    const decreaseQuantity = async (itemId) => {
        try {
            if (basketItems.find(item => item.article.id === itemId)?.quantity === 1) {
                removeItemFromBasket(itemId);
            } else {
                await axiosInstance.put(`${process.env.REACT_APP_APP_SERVER_URL}/action/decrease-count-of-basket-item?articleId=${itemId}`);
                const updatedItems = basketItems.map(item =>
                    item.article.id === itemId ? { ...item, quantity: item.quantity - 1 } : item
                );
                setBasketItems(updatedItems);
            }
        } catch (error) {
            console.error('Ошибка при уменьшении количества товара в корзине:', error);
        }
    };

    const increaseQuantity = async (itemId) => {
        try {
            await axiosInstance.put(`${process.env.REACT_APP_APP_SERVER_URL}/action/increase-count-of-basket-item?articleId=${itemId}`);
            const updatedItems = basketItems.map(item =>
                item.article.id === itemId ? { ...item, quantity: item.quantity + 1 } : item
            );
            setBasketItems(updatedItems);
        } catch (error) {
            console.error('Ошибка при увеличении количества товара в корзине:', error);
        }
    };
    
    const removeItemFromBasket = async (itemId) => {
        try {
            await axiosInstance.delete(`${process.env.REACT_APP_APP_SERVER_URL}/action/delete-from-basket?articleId=${itemId}`);
            const updatedItems = basketItems.filter(item => item.article.id !== itemId);
            setBasketItems(updatedItems);
            alert('Товар удален из корзины!');
        } catch (error) {
            console.error('Ошибка при удалении товара из корзины:', error);
        }
    };

    const getTotalPrice = () => {
        return basketItems.reduce((total, item) => total + item.article.actualPrice * item.quantity, 0);
    };

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <h2>Корзина</h2>
                {basketItems.map(item => (
                    <div key={item.id} className="basket-item">
                        <h3>{item.article.name}</h3>
                        <p><span className="old-price">{item.article.oldPrice}</span></p>
                        <p>{item.article.actualPrice} ₽</p>
                        <div className='quantity-set'>
                            <button onClick={() => decreaseQuantity(item.article.id)}> - </button>
                            <span>{item.quantity}</span>
                            <button onClick={() => increaseQuantity(item.article.id)}> + </button>
                        </div>
                        <p>Итого: {item.article.actualPrice * item.quantity}</p>
                        <button onClick={() => removeItemFromBasket(item.article.id)}>Удалить</button>
                    </div>
                ))}
                <div className="total-price">
                    <h3>Итоговая стоимость: {getTotalPrice()} ₽</h3>
                    <button onClick={() => navigate('/checkout')} disabled={basketItems.length === 0}>
                        Перейти к оформлению
                    </button>
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default BasketPage;