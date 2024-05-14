// OrderManagementPage.js

import React, { useState, useEffect } from 'react';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import axiosInstance from '../utils/axiosInstance';
import '../styles/OrderManagementPage.css';

const OrderManagementPage = () => {
    const [orders, setOrders] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [orderStatuses, setOrderStatuses] = useState([]);
    const [selectedStatusesMap, setSelectedStatusesMap] = useState({});

    const fetchOrderStatuses = async () => {
        try {
            const response = await axiosInstance.get(`${process.env.REACT_APP_APP_SERVER_URL}/order-status`, {
                withCredentials: true
            });
            setOrderStatuses(response.data);
        } catch (error) {
            console.error('Ошибка при запросе данных статусов заказа:', error);
        }
    };

    const handleSearch = async () => {
        try {
            const response = await axiosInstance.get(`${process.env.REACT_APP_APP_SERVER_URL}/action/admin/order-by-username/${searchText}`, {
                withCredentials: true
            });
            setOrders(response.data);
        } catch (error) {
            console.error('Ошибка при запросе данных:', error);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axiosInstance.get(`${process.env.REACT_APP_APP_SERVER_URL}/action/get-all-order`, {
                    withCredentials: true
                });
                setOrders(response.data);
                fetchOrderStatuses();
            } catch (error) {
                console.error('Ошибка при запросе данных заказов:', error);
            }
        };

        fetchData();
    }, []);

    const handleUpdateStatus = async (orderId, statusId) => {
        try {
            const response = await axiosInstance.put(`${process.env.REACT_APP_APP_SERVER_URL}/action/admin/order/update-status?order=${orderId}&status=${statusId}`, {
                withCredentials: true
            });
            if (response.status === 200) {
                // Обновляем статус заказа в UI
                const updatedOrders = orders.map(order => {
                    if (order.id === orderId) {
                        return { ...order, status: { id: statusId, name: orderStatuses.find(status => status.id === statusId)?.name } };
                    }
                    return order;
                });
                setOrders(updatedOrders);
                console.log('Статус заказа успешно обновлен');
            } else {
                console.log('Ошибка при обновлении статуса заказа');
            }
        } catch (error) {
            console.error('Ошибка при обновлении статуса заказа:', error);
        }
    };

    const handleSelectStatus = (orderId, statusId) => {
        setSelectedStatusesMap({
            ...selectedStatusesMap,
            [orderId]: statusId,
        });
    };

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <div className="order-search-bar">
                    <input className="order-input" type="text" value={searchText} onChange={(e) => setSearchText(e.target.value)} />
                    <button className='order-management-button' onClick={handleSearch}>Искать по Email</button>
                </div>
                <div className="order-table">
                    <table>
                        <thead>
                            <tr>
                                <th>Номер заказа</th>
                                <th>Адрес</th>
                                <th>Дата заказа</th>
                                <th>Общая сумма</th>
                                <th>Статус</th>
                                <th>Покупатель</th>
                                <th>Обновить статус</th>
                                <th>Подтверждение</th>
                            </tr>
                        </thead>
                        <tbody>
                            {orders.map(order => (
                                <tr key={order.id}>
                                    <td>{order.orderNumber}</td>
                                    <td>{order.address}</td>
                                    <td>{order.orderDate}</td>
                                    <td>{order.total}</td>
                                    <td>{order.status.name}</td>
                                    <td>{order.buyer.username}</td>
                                    <td>
                                        <select className='select-status' value={selectedStatusesMap[order.id] || ''} onChange={(e) => handleSelectStatus(order.id, Number(e.target.value))}>
                                            <option value="">Выберите статус</option>
                                            {orderStatuses.map(status => (
                                                <option key={status.id} value={status.id}>{status.name}</option>
                                            ))}
                                        </select>
                                    </td>
                                    <td>
                                        <button className='order-management-button' onClick={() => handleUpdateStatus(order.id, selectedStatusesMap[order.id])}>Обновить статус</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default OrderManagementPage;