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

    const navigate = useNavigate();

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await axios.get('http://localhost:8080/category');
                setCategories(response.data);
            } catch (error) {
                console.error('Ошибка при запросе категорий:', error);
            }
        };

        fetchCategories();
    }, []);

    const handleCreateArticle = async () => {
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
        // formData.append('articleCreateDTO', JSON.stringify(articleData));
        formData.append('articleCreateDTO', new Blob([JSON.stringify(articleData)], {
            type: "application/json"
          }));
        formData.append('image', imageFile);
    
        try {
            console.log('Отправляемые данные:');
            console.log(formData);

            const response = await axiosInstance.post('http://localhost:8080/article/admin/create', formData, {
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
    };

    return (
        <div>
            <MyNavbar />
            <div className="create-article-container">
                <h2>Создать новый товар</h2>
                <input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Название" />
                <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Описание" />
                <input type="number" value={quantityOfPlayers} onChange={(e) => setQuantityOfPlayers(parseInt(e.target.value))} placeholder="Количество игроков" />
                <input type="text" value={gameTime} onChange={(e) => setGameTime(e.target.value)} placeholder="Время игры" />
                <input type="text" value={yearOfRelease} onChange={(e) => setYearOfRelease(e.target.value)} placeholder="Год выпуска" />
                <input type="text" value={producer} onChange={(e) => setProducer(e.target.value)} placeholder="Производитель" />
                <input type="number" value={recommendedAge} onChange={(e) => setRecommendedAge(parseInt(e.target.value))} placeholder="Рекомендуемый возраст" />
                <input type="number" value={oldPrice} onChange={(e) => setOldPrice(parseFloat(e.target.value))} placeholder="Старая цена" />
                <input type="number" value={actualPrice} onChange={(e) => setActualPrice(parseFloat(e.target.value))} placeholder="Актуальная цена" />
                <input type="number" value={reserves} onChange={(e) => setReserves(parseInt(e.target.value))} placeholder="Резервы" />
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
                <input type="file" onChange={(e) => setImageFile(e.target.files[0])} />
                <button onClick={handleCreateArticle} className="black-button">Создать товар</button>
            </div>
            <MyFooter />
        </div>
    );
};

export default CreateArticlePage;