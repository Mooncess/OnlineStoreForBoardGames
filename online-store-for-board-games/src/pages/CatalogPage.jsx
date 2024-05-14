import React, { useState, useEffect } from 'react';
import axios from 'axios';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import ArticleItem from '../components/ArticleItem';
import '../styles/CatalogPage.css';

const CatalogPage = () => {
    const [articles, setArticles] = useState([]);
    const [categories, setCategories] = useState([]);
    const [searchText, setSearchText] = useState('');
    const [name, setName] = useState('');
    const [sort, setSort] = useState('');
    const [category, setCategory] = useState('');
    const [currentCategory, setCurrentCategory] = useState('Настольные игры');

    useEffect(() => {
        const fetchArticles = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_APP_SERVER_URL}/article/?category=${category}&sort=${sort}&name=${name}`);
                setArticles(response.data);
            } catch (error) {
                console.error('Ошибка при запросе товаров:', error);
            }
        };

        const fetchCategories = async () => {
            try {
                const response = await axios.get(`${process.env.REACT_APP_APP_SERVER_URL}/category`);
                setCategories([{ id: '', name: 'Настольные игры' }, ...response.data]);
            } catch (error) {
                console.error('Ошибка при запросе категорий:', error);
            }
        };

        if (category) {
            const selectedCategory = categories.find(cat => cat.id === category);
            setCurrentCategory(selectedCategory ? selectedCategory.name : 'Настольные игры');
        } else {
            setCurrentCategory('Настольные игры');
        }

        fetchArticles();
        fetchCategories();
    }, [name, sort, category]);

    const handleSearch = async () => {
        setName(searchText);
    };

    const handleSort = async (type) => {
        setSort(type);
    };

    const handleFilterByCategory = async (selectedCategory) => {
        setCategory(selectedCategory);
    };

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                <div className="search-bar">
                    <input type="text" placeholder="Поиск по имени" value={searchText} onChange={(e) => setSearchText(e.target.value)} />
                    <button className='search-bar-button' onClick={handleSearch}>Поиск</button>
                </div>
                <div className="current-category">{currentCategory}</div>
                <div className="sort-dropdown">
                    <select onChange={(e) => handleSort(e.target.value)}>
                        <option value="0">Сортировка...</option>
                        <option value="1">Сортировка по цене (по возрастанию)</option>
                        <option value="2">Сортировка по цене (по убыванию)</option>
                        <option value="3">По имени (по возрастанию)</option>
                        <option value="4">По имени (по убыванию)</option>
                    </select>
                </div>
                <div className="catalog-container">
                    <div className="category-panel">
                        <h3>Категории:</h3>
                        <ul>
                            {categories.map(category => (
                                <li key={category.id} onClick={() => handleFilterByCategory(category.id)}>{category.name}</li>
                            ))}
                        </ul>
                    </div>
                    <div className="article-list">
                        {articles.length === 0 ? (
                            <p>Ничего не найдено</p>
                        ) : (
                            articles.map(article => (
                                <ArticleItem
                                    key={article.id}
                                    id={article.id}
                                    name={article.name}
                                    oldPrice={article.oldPrice}
                                    actualPrice={article.actualPrice}
                                    reserves={article.reserves}
                                    imageURN={article.imageURN}
                                />
                            ))
                        )}
                    </div>
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default CatalogPage;