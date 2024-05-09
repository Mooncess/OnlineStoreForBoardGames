import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import '../styles/ArticleItem.css';

const ArticleItem = ({ id, name, oldPrice, actualPrice, reserves, imageURN }) => {
    const [imageUrl, setImageUrl] = useState('');

    useEffect(() => {
    const fetchImage = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/article/image/${id}`, { responseType: 'blob' });
            const imageUrl = URL.createObjectURL(response.data);
            setImageUrl(imageUrl);
        } catch (error) {
            console.error('Ошибка при загрузке изображения:', error);
        }
    };

    fetchImage();
}, [id]);

return (
    <Link to={`/article/${id}`} style={{ textDecoration: 'none' }}>
        <div className={`article-item ${reserves === 0 ? 'no-reserves' : ''}`}>
            <div className="image-container">
                <img src={imageUrl} alt={name} />
            </div>
            <div className="name">{name}</div>
            <div className="prices">
                {reserves === 0 ? (
                    <span className="unavailable-text">Нет в наличии</span>
                ) : (
                    <>
                        <span className="old-price">{oldPrice}</span>
                        <span className="actual-price">{actualPrice} ₽</span>
                    </>
                )}
            </div>
        </div>
    </Link>
);
};

export default ArticleItem;