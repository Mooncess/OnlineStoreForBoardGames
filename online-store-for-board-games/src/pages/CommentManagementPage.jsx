import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/MyCommentPage.css';

const CommentManagementPage = () => {
    const [comments, setComments] = useState([]);

    useEffect(() => {
        const fetchUserComments = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/comment/all');
                setComments(response.data);
            } catch (error) {
                console.error('Error fetching user comments:', error);
            }
        };

        fetchUserComments();
    }, []);

    const handleDeleteComment = async (commentId) => {
        try {
            await axiosInstance.delete(`http://localhost:8080/comment/admin/delete/${commentId}`);
            // Обновить список комментариев после удаления
            setComments(comments.filter(comment => comment.id !== commentId));
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    return (
        <div>
            <MyNavbar />
            <div className='main-content'>
                <div className="comments-container">
                    <h2>Управление отзывами</h2>
                    {comments.length > 0 ? (
                        comments.map(comment => (
                            <div key={comment.id} className="comment-block">
                                <p>Товар: {comment.article.name}</p>
                                <p>Пользователь: {comment.author.username}</p>
                                <p className='pia'>{comment.commentDate}</p>
                                <p>{comment.content}</p>
                                <button className='button-comment' onClick={() => handleDeleteComment(comment.id)}>Удалить</button>
                            </div>
                        ))
                    ) : (
                        <p>Вы пока не оставляли отзывов</p>
                    )}
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default CommentManagementPage;