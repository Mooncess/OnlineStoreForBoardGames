import React, { useState, useEffect } from 'react';
import axios from 'axios';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import axiosInstance from '../utils/axiosInstance';
import '../styles/CategoryManagementPage.css';

const CategoryManagementPage = () => {
    const [categories, setCategories] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [selectedCategoryId, setSelectedCategoryId] = useState(null);
    const [updateText, setUpdateText] = useState('');

    const fetchCategories = async () => {
        try {
            const response = await axios.get('http://localhost:8080/category');
            setCategories(response.data);
        } catch (error) {
            console.error('Ошибка при запросе данных категорий:', error);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    const handleCreateCategory = async () => {
        try {
            const response = await axiosInstance.post(`http://localhost:8080/category/admin/create?nameOfCategory=${searchText}`, {
                withCredentials: true,
            });
            if (response.status === 201) {
                // Обновляем список категорий после создания новой категории
                fetchCategories();
                console.log('Категория успешно создана');
                setSearchText(''); // Очистка текстового поля после создания
            } else {
                console.log('Что-то пошло не так при создании категории');
            }
        } catch (error) {
            console.error('Ошибка при создании категории:', error);
        }
    };

    const handleUpdateCategory = async (categoryId) => {
        setSelectedCategoryId(categoryId);
        setUpdateText(categories.find(cat => cat.id === categoryId).name);
    };

    const confirmUpdateCategory = async () => {
        try {
            const response = await axiosInstance.put(`http://localhost:8080/category/admin/update/${selectedCategoryId}?nameOfCategory=${updateText}`, {
                withCredentials: true,
            });
            if (response.status === 200) {
                fetchCategories();
                console.log('Категория успешно обновлена');
                setSelectedCategoryId(null);
                setUpdateText('');
            } else {
                console.log('Ошибка при обновлении категории');
            }
        } catch (error) {
            console.error('Ошибка при обновлении категории:', error);
        }
    };

    const handleDeleteCategory = async (categoryId) => {
        try {
            const response = await axiosInstance.delete(`http://localhost:8080/category/admin/delete/${categoryId}`, {
                withCredentials: true,
            });
            if (response.status === 204) {
                fetchCategories();
                console.log('Категория успешно удалена');
            } else {
                console.log('Ошибка при удалении категории');
            }
        } catch (error) {
            console.error('Ошибка при удалении категории:', error);
        }
    };

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <h2 className='category-management-h2'>Категории</h2>
                <div className="category-management-header">
                    <input className='category-management-input' type="text" value={searchText} onChange={(e) => setSearchText(e.target.value)} />
                    <button className='category-management-button' onClick={handleCreateCategory}>Создать категорию</button>
                </div>
                <div className="category-table">
                    <table>
                        <thead className='category-management-thead'>
                            <tr>
                                <th>Наименование</th>
                                <th>Обновить</th>
                                <th>Удалить</th>
                            </tr>
                        </thead>
                        <tbody>
                            {categories.map(category => (
                                <tr key={category.id}>
                                    <td>{category.id === selectedCategoryId ? <input type="text" value={updateText} onChange={(e) => setUpdateText(e.target.value)} /> : category.name}</td>
                                    <td>
                                        {category.id === selectedCategoryId ? <button className='category-management-button' onClick={confirmUpdateCategory}>Подтвердить</button> : <button className='category-management-button' onClick={() => handleUpdateCategory(category.id)}>Обновить</button>}
                                    </td>
                                    <td><button className='category-management-button' onClick={() => handleDeleteCategory(category.id)}>Удалить</button></td>
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

export default CategoryManagementPage;