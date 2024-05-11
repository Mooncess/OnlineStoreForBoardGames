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
    const [isInWishlist, setIsInWishlist] = useState(false);
    const [isInBasket, setIsInBasket] = useState(false);
    const [isAvailable, setIsAvailable] = useState(true); // Добавляем состояние для проверки наличия товара
    const navigate = useNavigate();
    
    const fetchComments = async () => {
        try {
            const response = await axiosInstance.get(`http://localhost:8080/comment/article-comments/${id}`, { withCredentials: true });
            setComments(response.data);
        } catch (error) {
            console.error('Ошибка при запросе комментариев:', error);
        }
    };

    const fetchData = async () => {
        try {
            const responseArticle = await axios.get(`http://localhost:8080/article/${id}`);
            setArticle(responseArticle.data);

            const responseWishlist = await axios.get(`http://localhost:8080/action/is-article-in-wishlist?articleId=${id}`, { withCredentials: true });
            const responseBasket = await axios.get(`http://localhost:8080/action/is-article-in-basket?articleId=${id}`, { withCredentials: true });

            setIsInWishlist(responseWishlist.status === 200);
            setIsInBasket(responseBasket.status === 200);

            const responseAvailability = await axios.get(`http://localhost:8080/action/check-reserves?articleId=${id}`, { withCredentials: true });
            setIsAvailable(responseAvailability.status === 200);
        } catch (error) {
            console.error('Ошибка при запросе информации о товаре:', error);
        }
    };

    useEffect(() => {
        fetchData();
        fetchComments();
    }, [id]);

    const addToBasket = async () => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/add-to-basket?articleId=${id}`, null, { withCredentials: true });
            alert('Товар добавлен в корзину!');
            // После успешного добавления в корзину делаем запрос для обновления информации
            fetchData();
        } catch (error) {
            console.error('Ошибка при добавлении товара в корзину:', error);
        }
    };

    const addToWishlist = async () => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/add-to-wishlist?articleId=${id}`, null, { withCredentials: true });
            alert('Товар добавлен в избранное!');
            // После успешного добавления в список желаемого делаем запрос для обновления информации
            fetchData();
        } catch (error) {
            console.error('Ошибка при добавлении товара в избранное:', error);
        }
    };

    const handleAddComment = async () => {
        try {
            await axiosInstance.post('http://localhost:8080/comment/create', {
                content: newComment,
                articleId: id
            }, { withCredentials: true });
            setNewComment('');
            setShowCommentPanel(false);
            fetchComments();
        } catch (error) {
            console.error('Ошибка при добавлении комментария:', error);
            if (error.response && error.response.status === 401) {
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
                            {isInBasket && !isAvailable ? (
                                <button className='button-container-b' disabled>Нет в наличии</button>
                            ) : isInBasket && isAvailable ? (
                                <button className='button-container-b' disabled>Уже в корзине</button>
                            ) : !isInBasket && isAvailable ? (
                                <button className='button-container-b' onClick={addToBasket}>В корзину</button>
                            ) : (
                                <button className='button-container-b' disabled>Нет в наличии</button>
                            )}
                            {isInWishlist ? <button className='button-container-b'>Уже в избранном</button> : <button className='button-container-b' onClick={addToWishlist}>В избранное</button>}
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
                                <p className='pia'>{comment.commentDate}</p>
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