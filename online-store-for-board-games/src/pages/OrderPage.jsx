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
            <div className='main-content'>
                <div className="order-info">
                    {orderInfo ? (
                        <>
                            <div>
                                <h2 className='user-order-h2'>Номер заказа: {orderInfo.orderNumber}</h2>
                                <p className="order-date">Дата заказа: {orderInfo.orderDate}</p>
                            </div>
                            <div>
                                Адрес доставки: {orderInfo.address}
                            </div>
                            <div>
                                <h3>Товары:</h3>
                                <table className='user-order-table'>
                                    <thead className='user-order-thead'>
                                        <tr>
                                            <th className='user-order-th'>Наименование товара</th>
                                            <th className='user-order-th'>Количество</th>
                                            <th className='user-order-th'>Цена</th>
                                            <th className='user-order-th'>Итого</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {orderInfo.orderItemList.map(item => (
                                            <tr key={item.id}>
                                                <td className='user-order-td'>{item.articleName}</td>
                                                <td className='user-order-td'>{item.quantity}</td>
                                                <td className='user-order-td'>{item.price} ₽</td>
                                                <td className='user-order-td'>{item.quantity * item.price} ₽</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                                <div>
                                    <p className="discount">
                                        Скидка: {Math.floor(100 - ((orderInfo.total / orderInfo.orderItemList.reduce((acc, item) => acc + item.quantity * item.price, 0)) * 100))} %
                                    </p>
                                    <p>
                                        Итоговая сумма заказа: <span className="total-price">{orderInfo.total} ₽</span>
                                    </p>
                                </div>
                            </div>
                            <div>
                                <p className="order-status">
                                    Текущий статус: {orderInfo.status.name}
                                </p>
                            </div>
                        </>
                    ) : (
                        <p>Загрузка информации о заказе...</p>
                    )}
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default OrderPage;