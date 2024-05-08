import React, { useState, useEffect } from 'react';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import axiosInstance from '../utils/axiosInstance';
import '../styles/OrderManagementPage.css';

const OrderManagementPage = () => {
    const [orders, setOrders] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [orderStatuses, setOrderStatuses] = useState([]);
    const [selectedStatus, setSelectedStatus] = useState(undefined);

    const fetchOrderStatuses = async () => {
        try {
            const response = await axiosInstance.get('http://localhost:8080/order-status', {
                withCredentials: true
            });
            setOrderStatuses(response.data);
        } catch (error) {
            console.error('Ошибка при запросе данных статусов заказа:', error);
        }
    };

    const handleSearch = async () => {
        try {
            const response = await axiosInstance.get(`http://localhost:8080/action/admin/order-by-username/${searchText}`, {
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
                const response = await axiosInstance.get('http://localhost:8080/action/get-all-order', {
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
            const response = await axiosInstance.put(`http://localhost:8080/action/admin/order/update-status?order=${orderId}&status=${statusId}`, {
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

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <div className="search-bar">
                    <input type="text" value={searchText} onChange={(e) => setSearchText(e.target.value)} />
                    <button onClick={handleSearch}>Искать по Email</button>
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
                                        <select value={selectedStatus} onChange={(e) => setSelectedStatus(Number(e.target.value))}>
                                            <option value="">Выберите статус</option>
                                            {orderStatuses.map(status => (
                                                <option key={status.id} value={status.id}>{status.name}</option>
                                            ))}
                                        </select>
                                    </td>
                                    <td>
                                        <button onClick={() => handleUpdateStatus(order.id, selectedStatus)}>Обновить статус</button>
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