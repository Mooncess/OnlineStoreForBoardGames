import React, { useState, useEffect } from 'react';
import axios from 'axios';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import { useNavigate } from 'react-router-dom';
import '../styles/CheckoutPage.css';

const CheckoutPage = () => {
    const [basketItems, setBasketItems] = useState([]);
    const [personalDiscount, setPersonalDiscount] = useState(0);
    const [deliveryAddress, setDeliveryAddress] = useState('');
    const [checkoutError, setCheckoutError] = useState(false);
    const [addressError, setAddressError] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchBasketData = async () => {
            try {
                const response = await axiosInstance.get(`${process.env.REACT_APP_APP_SERVER_URL}/action/get-user-basket`);
                setBasketItems(response.data);
            } catch (error) {
                console.error('Ошибка при запросе данных корзины:', error);
            }
        };

        fetchBasketData();

        const fetchProfileData = async () => {
            try {
                const response = await axiosInstance.get(`${process.env.REACT_APP_APP_SERVER_URL}/action/profile`);
                setPersonalDiscount(response.data.personalDiscount);
            } catch (error) {
                console.error('Ошибка при запросе данных о пользователе:', error);
            }
        };

        fetchProfileData();
    }, []);

    const getTotalPrice = () => {
        return basketItems.reduce((total, item) => total + item.article.actualPrice * item.quantity, 0) * (1 - personalDiscount / 100);
    };

    const handleCheckout = async () => {
        try {
            if (!deliveryAddress.trim()) {
                setAddressError(true); // Устанавливаем состояние ошибки для пустого адреса
            } else {
                setAddressError(false); // Сбрасываем состояние ошибки
                const response = await axiosInstance.post(`${process.env.REACT_APP_APP_SERVER_URL}/action/create-order?address=${deliveryAddress}`);
                if (response.status === 200) {
                    alert('Заказ успешно оформлен!');
                    navigate('/profile');
                } else {
                    alert('Ошибка при оформлении заказа');
                }
            }
        } catch (error) {
            console.error('Ошибка при оформлении заказа:', error);
            setCheckoutError(true);
        }
    };

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <h2 className="checkout-h2">Оформление заказа</h2>
                {basketItems.map(item => (
                    <div key={item.id} className="checkout-item">
                        <p>{item.article.name}, Цена: <span className="old-price">{item.article.oldPrice}</span> {item.article.actualPrice} ₽, Количество: {item.quantity}, Итого: {item.article.actualPrice * item.quantity}</p>
                    </div>
                ))}
                <div className="discount">
                    <p>Ваша персональная скидка: {personalDiscount}%</p>
                </div>
                <div className="total-price">
                    <h3>Итоговая стоимость заказа: {getTotalPrice()} ₽</h3>
                    <input
                        className='deliv-address'
                        type="text"
                        value={deliveryAddress}
                        onChange={(e) => setDeliveryAddress(e.target.value)}
                        placeholder="Введите адрес доставки"
                    />
                    <button className='chechout-button' onClick={handleCheckout}>Оформить</button>
                </div>
                {checkoutError && <p style={{ color: 'red' }}>Ошибка при формировании заказа</p>}
                {addressError && <p style={{ color: 'red' }}>Пожалуйста, введите адрес</p>}
            </div>
            <MyFooter />
        </div>
    );
};

export default CheckoutPage;