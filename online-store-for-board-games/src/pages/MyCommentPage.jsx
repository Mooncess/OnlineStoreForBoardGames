import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/MyCommentPage.css';

const MyCommentPage = () => {
    const [comments, setComments] = useState([]);
    const [updateCommentId, setUpdateCommentId] = useState(null);
    const [newCommentContent, setNewCommentContent] = useState('');

    useEffect(() => {
        const fetchUserComments = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/comment/user-comments');
                setComments(response.data);
            } catch (error) {
                console.error('Error fetching user comments:', error);
            }
        };

        fetchUserComments();
    }, []);

    const handleDeleteComment = async (commentId) => {
        try {
            await axiosInstance.delete(`http://localhost:8080/comment/delete/${commentId}`);
            // Обновить список комментариев после удаления
            setComments(comments.filter(comment => comment.id !== commentId));
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    const handleUpdateComment = async () => {
        try {
            await axiosInstance.put(`http://localhost:8080/comment/update/${updateCommentId}?content=${newCommentContent}`);
            // Обновить список комментариев после успешного обновления
            fetchUserComments();
            setUpdateCommentId(null);
            setNewCommentContent('');
        } catch (error) {
            console.error('Error updating comment:', error);
        }
    };

    const fetchUserComments = async () => {
        try {
            const response = await axiosInstance.get('http://localhost:8080/comment/user-comments');
            setComments(response.data);
        } catch (error) {
            console.error('Error fetching user comments:', error);
        }
    };

    useEffect(() => {
        if (updateCommentId !== null) {
            const selectedComment = comments.find(comment => comment.id === updateCommentId);
            if (selectedComment) {
                setNewCommentContent(selectedComment.content);
            }
        }
    }, [updateCommentId, comments]);

    return (
        <div>
            <MyNavbar />
            <div className='main-content'>
                <div className="comments-container">
                    <h2>Мои отзывы</h2>
                    {comments.length > 0 ? (
                        comments.map(comment => (
                            <div key={comment.id} className="comment-block">
                                <p>Товар: {comment.article.name}</p>
                                <p className='pia'>{comment.commentDate}</p>
                                <p>{comment.content}</p>
                                <button className='button-comment' onClick={() => handleDeleteComment(comment.id)}>Удалить</button>
                                <button className='button-comment' onClick={() => setUpdateCommentId(comment.id)}>Обновить</button>
                                {updateCommentId === comment.id && (
                                    <div className="update-comment-panel">
                                        <textarea className='textarea-for-comment'
                                            value={newCommentContent}
                                            onChange={(e) => setNewCommentContent(e.target.value)}
                                        />
                                        <button className='button-comment' onClick={handleUpdateComment}>Подтвердить</button>
                                    </div>
                                )}
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

export default MyCommentPage;