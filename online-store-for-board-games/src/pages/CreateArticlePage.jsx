import React, { useState, useEffect } from 'react';
import axios from 'axios';
import axiosInstance from '../utils/axiosInstance';
import { useNavigate } from 'react-router-dom';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/CreateArticlePage.css';

const CreateArticlePage = () => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [quantityOfPlayers, setQuantityOfPlayers] = useState('');
    const [gameTime, setGameTime] = useState('');
    const [yearOfRelease, setYearOfRelease] = useState('');
    const [producer, setProducer] = useState('');
    const [recommendedAge, setRecommendedAge] = useState('');
    const [oldPrice, setOldPrice] = useState('');
    const [actualPrice, setActualPrice] = useState('');
    const [reserves, setReserves] = useState('');
    const [categories, setCategories] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [imageFile, setImageFile] = useState(null);
    const [errorNotification, setErrorNotification] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_APP_SERVER_URL}/category`);
                setCategories(response.data);
            } catch (error) {
                console.error('Ошибка при запросе категорий:', error);
            }
        };

        fetchCategories();
    }, []);

    const handleCreateArticle = async () => {
        // Проверка на заполнение всех полей
        if (name && description && quantityOfPlayers && gameTime && yearOfRelease && producer && recommendedAge && actualPrice && reserves && selectedCategories.length > 0) {
            const articleData = {
                name,
                description,
                quantityOfPlayers,
                gameTime,
                yearOfRelease,
                producer,
                recommendedAge,
                oldPrice,
                actualPrice,
                reserves,
                category: selectedCategories
            };
        
            const formData = new FormData();
            formData.append('articleCreateDTO', new Blob([JSON.stringify(articleData)], {
                type: "application/json"
            }));
            formData.append('image', imageFile);
        
            try {
                console.log('Отправляемые данные:');
                console.log(formData);
    
                const response = await axiosInstance.post(`${process.env.REACT_APP_APP_SERVER_URL}/article/admin/create`, formData, {
                    withCredentials: true,
                    headers: { 'Content-Type': 'multipart/form-data' }
                });
        
                if (response.status === 201) {
                    console.log("Товар успешно создан");
                    navigate('/admin/article-management'); // Перенаправление на страницу администратора
                } else {
                    console.log("Что-то пошло не так");
                    // Обработка ошибки создания товара
                }
            } catch (error) {
                console.error('Ошибка при создании товара:', error);
            }
        } else {
            setErrorNotification('Пожалуйста, заполните все обязательные поля.');
        }
    };

    return (
        <div>
            <MyNavbar />
            <div className='main-content'>
            <div className="create-article-container">
                <h2>Создать новый товар</h2>
                <label>Название:
                    <input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Название" />
                </label>
                <label>Описание:
                    <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Описание" />
                </label>
                <label>Количество игроков:
                    <input type="text" value={quantityOfPlayers} onChange={(e) => setQuantityOfPlayers(e.target.value)} placeholder="Количество игроков" />
                </label>
                <label>Время игры:
                    <input type="text" value={gameTime} onChange={(e) => setGameTime(e.target.value)} placeholder="Время игры" />
                </label>
                <label>Год выпуска:
                    <input type="text" value={yearOfRelease} onChange={(e) => setYearOfRelease(e.target.value)} placeholder="Год выпуска" />
                </label>
                <label>Производитель:
                    <input type="text" value={producer} onChange={(e) => setProducer(e.target.value)} placeholder="Производитель" />
                </label>
                <label>Рекомендуемый возраст:
                    <input type="number" value={recommendedAge} onChange={(e) => setRecommendedAge(parseInt(e.target.value))} placeholder="Рекомендуемый возраст" />
                </label>
                <label>Старая цена:
                    <input type="number" value={oldPrice} onChange={(e) => setOldPrice(parseFloat(e.target.value))} placeholder="Старая цена" />
                </label>
                <label>Актуальная цена:
                    <input type="number" value={actualPrice} onChange={(e) => setActualPrice(parseFloat(e.target.value))} placeholder="Актуальная цена" />
                </label>
                <label>Резервы:
                    <input type="number" value={reserves} onChange={(e) => setReserves(parseInt(e.target.value))} placeholder="Резервы" />
                </label>
                <h3>Категории</h3>
                <div className="category-checkboxes">
                    {categories.map(category => (
                        <label key={category.id}>
                            <input
                                type="checkbox"
                                value={category.id}
                                onChange={(e) => {
                                    if (e.target.checked) {
                                        setSelectedCategories(prevState => [...prevState, category.id]);
                                    } else {
                                        setSelectedCategories(prevState => prevState.filter(id => id !== category.id));
                                    }
                                }}
                            />
                            {category.name}
                        </label>
                    ))}
                </div>
                <h3>Изображение</h3>
                <input type="file" onChange={(e) => setImageFile(e.target.files[0])} />
                {errorNotification && <p style={{ color: 'red', textAlign: 'center' }}className="error-notification">{errorNotification}</p>}
                <button onClick={handleCreateArticle} className="black-button">Создать товар</button>
            </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default CreateArticlePage;