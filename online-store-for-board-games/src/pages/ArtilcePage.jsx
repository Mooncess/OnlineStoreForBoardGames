import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/ArticlePage.css'; // Подключаем стили

const ArticlePage = () => {
    const { id } = useParams();
    const [article, setArticle] = useState(null);
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const fetchArticle = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/article/${id}`);
                setArticle(response.data);
            } catch (error) {
                console.error('Ошибка при запросе информации о товаре:', error);
            }
        };

        const fetchComments = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/comment/article-comments/${id}`);
                setComments(response.data);
            } catch (error) {
                console.error('Ошибка при запросе комментариев:', error);
            }
        };

        fetchArticle();
        fetchComments();
    }, [id]);

    const addToBasket = async () => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/add-to-basket?articleId=${id}`);
            alert('Товар добавлен в корзину!');
        } catch (error) {
            console.error('Ошибка при добавлении товара в корзину:', error);
        }
    };

    const addToWishlist = async () => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/add-to-wishlist?articleId=${id}`);
            alert('Товар добавлен в избранное!');
        } catch (error) {
            console.error('Ошибка при добавлении товара в избранное:', error);
        }
    };

    if (!article) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <h2>{article.name}</h2>
                <div className="product-details">
                    <div className="product-image">
                        <img src={`http://localhost:8080/article/image/${id}`} alt={article.name} />
                        <div className="product-info">
                            <p>Старая цена: {article.oldPrice}</p>
                            <p>Новая цена: {article.actualPrice}</p>
                            <button onClick={addToBasket}>В корзину</button>
                            <button onClick={addToWishlist}>В избранное</button>
                        </div>
                    </div>
                    <hr />
                    <div>
                        <h3>Категория: {article.category[0].name}</h3>
                        <p>{article.description}</p>
                        <p>Producer: {article.producer}</p>
                        <p>Recommended Age: {article.recommendedAge}</p>
                    </div>
                </div>
                <hr />
                <div className="comments-section">
                    <h3>Комментарии:</h3>
                    {comments.map(comment => (
                        <div key={comment.id} className="comment">
                            <p><strong>{comment.author.firstName}</strong></p>
                            <p>{comment.commentDate}</p>
                            <p>{comment.content}</p>
                        </div>
                    ))}
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default ArticlePage;