import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import '../styles/ArticleManagementPage.css';

const ArticleManagementPage = () => {
    const [articles, setArticles] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [sortType, setSortType] = useState(0);
    const navigate = useNavigate();

    const handleSearch = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/article?name=${searchText}`);
            setArticles(response.data);
        } catch (error) {
            console.error('Ошибка при запросе данных:', error);
        }
    };

    const handleSort = async (type) => {
        try {
            const response = await axios.get(`http://localhost:8080/article?sort=${type}`);
            setArticles(response.data);
            setSortType(type);
        } catch (error) {
            console.error('Ошибка при запросе данных:', error);
        }
    };

    const handleCreateNewArticle = () => {
        navigate('/admin/create-article');
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get('http://localhost:8080/article');
                setArticles(response.data);
            } catch (error) {
                console.error('Ошибка при запросе данных:', error);
            }
        };

        fetchData();
    }, []);

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
            <div className="search-bar">
                    <input type="text" value={searchText} onChange={(e) => setSearchText(e.target.value)} />
                    <button onClick={handleSearch}>Поиск</button>
                </div>
                <div className="sort-buttons">
                    <button onClick={() => handleSort(1)}>Сортировка по резервам (по возрастанию)</button>
                    <button onClick={() => handleSort(2)}>Сортировка по резервам (по убыванию)</button>
                    <button onClick={() => handleSort(0)}>Без сортировки</button>
                    <button onClick={handleCreateNewArticle} className="create-new-article-button">Создать новый товар</button>
                </div>
            <div className="article-table">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Имя</th>
                            <th>Старая цена</th>
                            <th>Актуальная цена</th>
                            <th>Резервы</th>
                            <th>Категории</th>
                            <th>Обновить</th>
                        </tr>
                    </thead>
                    <tbody>
                        {articles.map(article => (
                            <tr key={article.id}>
                                <td>{article.id}</td>
                                <td>{article.name}</td>
                                <td>{article.oldPrice}</td>
                                <td>{article.actualPrice}</td>
                                <td>{article.reserves}</td>
                                <td>{article.category.map(cat => cat.name).join(', ')}</td>
                                <td><Link to={`/UpdateArticlePage/${article.id}`}>Обновить</Link></td>
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

export default ArticleManagementPage;