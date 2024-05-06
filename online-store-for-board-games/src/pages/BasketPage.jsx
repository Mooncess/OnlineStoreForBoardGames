import React, { useState, useEffect } from 'react';
import axios from 'axios';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/BasketPage.css';

const BasketPage = () => {
    const [basketItems, setBasketItems] = useState([]);

    useEffect(() => {
        const fetchBasketData = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/action/get-user-basket');
                setBasketItems(response.data);
            } catch (error) {
                console.error('Ошибка при запросе данных корзины:', error);
            }
        };

        fetchBasketData();
    }, []);

    const decreaseQuantity = async (itemId) => {
        try {
            await axiosInstance.put(`http://localhost:8080/action/decrease-count-of-basket-item?articleId=${itemId}`);
            setBasketItems(prevState =>
                prevState.map(item =>
                    item.id === itemId ? { ...item, quantity: item.quantity - 1 } : item
                )
            );
        } catch (error) {
            console.error('Ошибка при уменьшении количества товара в корзине:', error);
        }
    };

    const increaseQuantity = async (itemId) => {
        try {
            await axiosInstance.put(`http://localhost:8080/action/increase-count-of-basket-item?articleId=${itemId}`);
            setBasketItems(prevState =>
                prevState.map(item =>
                    item.id === itemId ? { ...item, quantity: item.quantity + 1 } : item
                )
            );
        } catch (error) {
            console.error('Ошибка при увеличении количества товара в корзине:', error);
        }
    };

    const removeItemFromBasket = async (itemId) => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/remove-from-basket?articleId=${itemId}`);
            setBasketItems(basketItems.filter(item => item.id !== itemId));
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
                        <p>Старая цена: <span className="old-price">{item.article.oldPrice}</span></p>
                        <p>Новая цена: {item.article.actualPrice}</p>
                        <div>
                            <button onClick={() => decreaseQuantity(item.article.id)}> - </button>
                            <span>Quantity: {item.quantity}</span>
                            <button onClick={() => increaseQuantity(item.article.id)}> + </button>
                        </div>
                        <p>Итого: {item.article.actualPrice * item.quantity}</p>
                        <button onClick={() => removeItemFromBasket(item.article.id)}>Удалить</button>
                    </div>
                ))}
                <div className="total-price">
                    <h3>Итоговая стоимость: {getTotalPrice()}</h3>
                    <button onClick={() => alert('Перейти к оформлению')}>Перейти к оформлению</button>
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default BasketPage;