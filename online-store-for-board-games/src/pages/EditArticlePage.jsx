// Код для EditArticlePage.jsx с добавлением всех полей с заполнением из articleData

// Импорт компонентов и библиотек
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/EditArticlePage.css';
import axiosInstance from '../utils/axiosInstance';

const EditArticlePage = () => {
    const { id } = useParams();
    const [articleData, setArticleData] = useState({
        name: '',
        description: '',
        quantityOfPlayers: '',
        gameTime: '',
        yearOfRelease: '',
        producer: '',
        recommendedAge: '',
        oldPrice: '',
        actualPrice: '',
        reserves: '',
        category: []
    });
    const [categories, setCategories] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [imageFile, setImageFile] = useState(null);
    const navigate = useNavigate();
    const [errorNotification, setErrorNotification] = useState('');

    useEffect(() => {
        const fetchArticleData = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/article/${id}`);
                setArticleData(response.data);
                setSelectedCategories(response.data.category.map(category => category.id));
            } catch (error) {
                console.error('Error fetching article data:', error);
            }
        };
    
        const fetchCategories = async () => {
            try {
                const response = await axios.get('http://localhost:8080/category');
                setCategories(response.data);
            } catch (error) {
                console.error('Error fetching categories:', error);
            }
        };
    
        fetchArticleData();
        fetchCategories();
    }, [id]);

    const handleCategoryChange = (categoryId) => {
        if (selectedCategories.includes(categoryId)) {
            setSelectedCategories(selectedCategories.filter(id => id !== categoryId));
        } else {
            setSelectedCategories([...selectedCategories, categoryId]);
        }
    };

    const handleUpdateArticle = async () => {
        const { name, description, quantityOfPlayers, gameTime, yearOfRelease, producer, recommendedAge, actualPrice, reserves } = articleData;
        console.log(articleData);
    
        if (name && description && quantityOfPlayers && gameTime && yearOfRelease && producer && recommendedAge && actualPrice && reserves && selectedCategories.length > 0) {
            const articleUpdateData = {
                name,
                description,
                quantityOfPlayers,
                gameTime,
                yearOfRelease,
                producer,
                recommendedAge,
                oldPrice: articleData.oldPrice, // Не проверяем на пустоту
                actualPrice,
                reserves,
                category: selectedCategories
            };
    
            const formData = new FormData();
            formData.append('articleUpdateDTO', new Blob([JSON.stringify(articleUpdateData)], {
                type: "application/json"
            }));
            formData.append('image', imageFile);
    
            try {
                const response = await axiosInstance.put(`http://localhost:8080/article/admin/update/${id}`, formData, {
                    withCredentials: true,
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
    
                if (response.status === 200) {
                    console.log("Товар успешно обновлён");
                    navigate('/admin/article-management'); // Перенаправление на страницу администратора
                } else {
                    console.log("Что-то пошло не так");
                    // Обработка ошибки обновления товара
                }
            } catch (error) {
                console.error('Ошибка при обновлении товара:', error);
            }
        } else {
            setErrorNotification('Пожалуйста, заполните все обязательные поля.');
        }
    };

    return (
        <div>
            <MyNavbar />
            <div className='main-content'>
            <div className="edit-article-container">
                <h2>Редактировать товар</h2>
                
                <label>
                    Название:
                    <input type="text" value={articleData.name} onChange={(e) => setArticleData({...articleData, name: e.target.value})} placeholder="Название" />
                </label>

                <label>
                    Описание:
                    <input type="text" value={articleData.description} onChange={(e) => setArticleData({...articleData, description: e.target.value})} placeholder="Описание" />
                </label>

                <label>
                    Количество игроков:
                    <input type="number" value={articleData.quantityOfPlayers} onChange={(e) => setArticleData({...articleData, quantityOfPlayers: parseInt(e.target.value)})} placeholder="Количество игроков" />
                </label>

                <label>
                    Время игры:
                    <input type="text" value={articleData.gameTime} onChange={(e) => setArticleData({...articleData, gameTime: e.target.value})} placeholder="Время игры" />
                </label>

                <label>
                    Год выпуска:
                    <input type="text" value={articleData.yearOfRelease} onChange={(e) => setArticleData({...articleData, yearOfRelease: e.target.value})} placeholder="Год выпуска" />
                </label>

                <label>
                    Производитель:
                    <input type="text" value={articleData.producer} onChange={(e) => setArticleData({...articleData, producer: e.target.value})} placeholder="Производитель" />
                </label>

                <label>
                    Рекомендуемый возраст:
                    <input type="number" value={articleData.recommendedAge} onChange={(e) => setArticleData({...articleData, recommendedAge: parseInt(e.target.value)})} placeholder="Рекомендуемый возраст" />
                </label>

                <label>
                    Старая цена:
                    <input type="number" value={articleData.oldPrice} onChange={(e) => setArticleData({...articleData, oldPrice: parseFloat(e.target.value)})} placeholder="Старая цена" />
                </label>

                <label>
                    Актуальная цена:
                    <input type="number" value={articleData.actualPrice} onChange={(e) => setArticleData({...articleData, actualPrice: parseFloat(e.target.value)})} placeholder="Актуальная цена" />
                </label>

                <label>
                    Резервы:
                    <input type="number" value={articleData.reserves} onChange={(e) => setArticleData({...articleData, reserves: parseInt(e.target.value)})} placeholder="Резервы" />
                </label>
                <h3>Категории</h3>
                <div className='category-checkboxes'>
                    {categories.map(category => (
                        <label key={category.id}>
                            <input
                                type="checkbox"
                                checked={selectedCategories.includes(category.id)}
                                onChange={() => handleCategoryChange(category.id)}
                            />
                            {category.name}
                        </label>
                    ))}
                </div>
                <h3>Изображение</h3>
                <input type="file" onChange={(e) => setImageFile(e.target.files[0])} />
                {errorNotification && <p style={{ color: 'red', textAlign: 'center' }}className="error-notification">{errorNotification}</p>}
                <button onClick={handleUpdateArticle} className="black-button">Обновить</button>
            </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default EditArticlePage;