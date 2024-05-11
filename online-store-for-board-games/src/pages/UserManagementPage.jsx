import React, { useState, useEffect } from 'react';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import axiosInstance from '../utils/axiosInstance';
import '../styles/UserManagementPage.css';

const UserManagementPage = () => {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/action/get-all-users');
                setUsers(response.data);
            } catch (error) {
                console.error('Ошибка при запросе данных пользователей:', error);
            }
        };

        fetchUsers();
    }, []);

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <h2 className='user-management-h2'>Пользователи</h2>
                <table className='user-m-table'>
                    <thead className='user-management-thead'>
                        <tr>
                            <th>Имя</th>
                            <th>Фамилия</th>
                            <th>Номер телефона</th>
                            <th>Личная скидка</th>
                            <th>Почта</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td>{user.firstName}</td>
                                <td>{user.lastName}</td>
                                <td>{user.phoneNumber}</td>
                                <td>{user.personalDiscount}%</td>
                                <td>{user.username}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            <MyFooter />
        </div>
    );
};

export default UserManagementPage;