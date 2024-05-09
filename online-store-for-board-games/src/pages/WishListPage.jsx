import React, { useState, useEffect } from 'react';
import axiosInstance from '../utils/axiosInstance';
import MyFooter from '../components/MyFooter';
import MyNavbar from '../components/MyNavbar';
import { Link} from 'react-router-dom';
import '../styles/WishListPage.css';

const WishListPage = () => {
    const [wishListItems, setWishListItems] = useState(null);

    useEffect(() => {
        const fetchWishListData = async () => {
            try {
                const response = await axiosInstance.get('http://localhost:8080/action/get-user-wishlist', { withCredentials: true });
                setWishListItems(response.data);
            } catch (error) {
                console.error('Ошибка при запросе данных списка желаний:', error);
            }
        };

        fetchWishListData();
    }, []);

    const addToBasket = async (itemId) => {
        try {
            await axiosInstance.post(`http://localhost:8080/action/add-to-basket?articleId=${itemId}`, { withCredentials: true });
            alert('Товар добавлен в корзину!');
        } catch (error) {
            console.error('Ошибка при добавлении товара в корзину:', error);
        }
    };

    const removeFromWishList = async (itemId) => {
        try {
            await axiosInstance.delete(`http://localhost:8080/action/delete-from-wishlist?articleId=${itemId}`, { withCredentials: true });
            setWishListItems(wishListItems.filter(item => item.id !== itemId));
        } catch (error) {
            console.error('Ошибка при удалении товара из списка желаний:', error);
        }
    };

    return (
        <div>
            <MyNavbar />
            <div className="main-content">
                {wishListItems && (
                    <h1 className='wish-list-h1'>Список желаний</h1>
                )}
                <div className='wish-list-container'>
                {wishListItems && wishListItems.map(item => (
                    <div key={item.id} className="wishlist-item">
                        <h3 className='article-link'><Link to={`/article/${item.id}`}>{item.name}</Link></h3>
                        <p className='old-price'>{item.oldPrice}</p>
                        <p>{item.actualPrice} ₽</p>
                        <button onClick={() => addToBasket(item.id)}>В корзину</button>
                        <button onClick={() => removeFromWishList(item.id)}>Удалить</button>
                    </div>
                ))}
                </div>
            </div>
            <MyFooter />
        </div>
    );
};

export default WishListPage;