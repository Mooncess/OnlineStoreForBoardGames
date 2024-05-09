import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/ArticlePage.css'; // Подключаем стили

const ArticlePage = () => {
    const { id } = useParams();
    const [article, setArticle] = useState(null);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [showCommentPanel, setShowCommentPanel] = useState(false);
    const navigate = useNavigate();
    
    const fetchComments = async () => {
        try {
            const response = await axiosInstance.get(`http://localhost:8080/comment/article-comments/${id}`, {withCredentials: true});
            setComments(response.data);
        } catch (error) {
            console.error('Ошибка при запросе комментариев:', error);
        }
    };

    useEffect(() => {
        const fetchArticle = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/article/${id}`);
                setArticle(response.data);
            } catch (error) {
                console.error('Ошибка при запросе информации о товаре:', error);
            }
        };

        fetchArticle();
        fetchComments();
    }, [id]);

    const addToBasket = async () => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/add-to-basket?articleId=${id}`, {withCredentials: true});
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

    const handleAddComment = async () => {
        try {
            // Отправка POST запроса с новым комментарием
            await axiosInstance.post('http://localhost:8080/comment/create', {
                content: newComment,
                articleId: id
            }, { withCredentials: true });
            // Обновляем комментарии
            setNewComment('');
            setShowCommentPanel(false);
            fetchComments(); // Обновляем список комментариев
        } catch (error) {
            console.error('Ошибка при добавлении комментария:', error);
            if (error.response.status === 401) {
                navigate('/login');
            }
        }
    };

    if (!article) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <h2 className='article-name-h2'>{article.name}</h2>
                <div className="product-details">
                    <div className="product-image">
                        <img src={`http://localhost:8080/article/image/${id}`} alt={article.name} width="360" height="360" />
                        
                    </div>
                    <div className="product-info">
                        <p className="old-price">{article.oldPrice}</p>
                        <p className="actual-price">{article.actualPrice} ₽</p>
                        <div className="button-container">
                            <button className='button-container-b' onClick={addToBasket}>В корзину</button>
                            <button className='button-container-b' onClick={addToWishlist}>В избранное</button>
                        </div>
                    </div>                    
                </div>
                <div className='product-info-additional'>
                    <h3>Категории: {article.category.map((category, index) => (
                        index === article.category.length - 1 ? category.name : `${category.name}, `
                    ))}</h3>
                    <h2 className='description-artilce'>Описание</h2>
                    <p>{article.description}</p>
                    <p className='pia'>Производитель: {article.producer}</p>
                    <p className='pia'>Рекомендованный возраст: {article.recommendedAge}+</p>
                </div>
                <div className="comments-section">
                    <h3>Отзывы</h3>
                    <button className='write-comment' onClick={() => setShowCommentPanel(true)}>Написать отзыв</button>
                    {showCommentPanel && (
                        <div className="comment-panel">
                            <textarea
                                value={newComment}
                                onChange={(e) => setNewComment(e.target.value)}
                                placeholder="Напишите ваш отзыв здесь"
                            />
                            <button onClick={handleAddComment}>Подтвердить</button>
                        </div>
                    )}
                    {comments.length > 0 ? (
                        comments.map(comment => (
                            <div key={comment.id} className="comment">
                                <p><strong>{comment.author.firstName}</strong></p>
                                <p>{comment.commentDate}</p>
                                <p>{comment.content}</p>
                            </div>
                        ))
                    ) : <p>Отзывов еще нет</p>}
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default ArticlePage;
