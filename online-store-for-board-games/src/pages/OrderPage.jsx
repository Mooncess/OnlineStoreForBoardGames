import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import { useParams } from 'react-router-dom';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/OrderPage.css';

const OrderPage = () => {
    const { id } = useParams();
    const [orderInfo, setOrderInfo] = useState(null);

    useEffect(() => {
        const fetchOrderInfo = async () => {
            try {
                const response = await axiosInstance.get(`http://localhost:8080/action/order/${id}`);
                const updatedOrderInfo = { ...response.data };

                // Fetch article name for each item in the order
                const updatedOrderItemList = await Promise.all(
                    updatedOrderInfo.orderItemList.map(async item => {
                        const articleResponse = await axiosInstance.get(`http://localhost:8080/article/${item.articleId}`);
                        return { ...item, articleName: articleResponse.data.name };
                    })
                );

                updatedOrderInfo.orderItemList = updatedOrderItemList;
                setOrderInfo(updatedOrderInfo);
            } catch (error) {
                console.error('Error fetching order info:', error);
            }
        };

        fetchOrderInfo();
    }, [id]);

    return (
        <div>
            <MyNavbar />
            <div className="order-info">
                {orderInfo ? (
                    <>
                        <div>
                            <h2>Номер заказа: {orderInfo.orderNumber}</h2>
                            Дата заказа: {orderInfo.orderDate}
                        </div>
                        <div>
                            Адрес доставки: {orderInfo.address}
                        </div>
                        <div>
                            <h3>Товары:</h3>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Наименование товара</th>
                                        <th>Количество</th>
                                        <th>Цена</th>
                                        <th>Итого</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {orderInfo.orderItemList.map(item => (
                                        <tr key={item.id}>
                                            <td>{item.articleName}</td>
                                            <td>{item.quantity}</td>
                                            <td>{item.price}</td>
                                            <td>{item.quantity * item.price}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                            <div>
                                Скидка: {1 - (orderInfo.total / orderInfo.orderItemList.reduce((acc, item) => acc + item.quantity * item.price, 0))}
                                <br />
                                Итоговая сумма заказа: {orderInfo.total}
                            </div>
                        </div>
                        <div>
                            Текущий статус: {orderInfo.status.name}
                        </div>
                    </>
                ) : (
                    <p>Загрузка информации о заказе...</p>
                )}
            </div>
            <MyFooter />
        </div>
    );
};

export default OrderPage;